package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import travellin.travelblog.dto.UserDto;
import travellin.travelblog.security.config.JwtService;
import travellin.travelblog.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            UserDto userResponse = userService.getUserById(id);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> getUsersByUsernameContaining(@RequestParam String username) {
        List<UserDto> usersResponse = userService.getUsersByUsernameContaining(username);
        if (usersResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found with the provided username");
        }
        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> usersResponse = userService.getAllUsers();
        return ResponseEntity.ok(usersResponse);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long id, @RequestParam String password, 
                                  @RequestHeader("Authorization") String authorizationHeader) throws Exception {
    String token = authorizationHeader.replace("Bearer ", "");
    Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));

    if (id.equals(userId)) {
        UserDto userResponse = userService.updateUserPassword(id, password);
        return ResponseEntity.ok(userResponse);
    } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user with id: " + id + ". User not found", e);
        }
    }
}
