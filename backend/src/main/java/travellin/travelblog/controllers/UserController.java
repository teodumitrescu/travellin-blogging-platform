package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.dto.UserDto;
import travellin.travelblog.security.config.JwtService;
import travellin.travelblog.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserDto userResponse = userService.getUserById(id);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<UserDto>> getUsersByUsernameContaining(@RequestParam String username) {
        List<UserDto> usersResponse = userService.getUsersByUsernameContaining(username);
        if (usersResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usersResponse);
        }
        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
        List<UserDto> usersResponse = userService.getAllUsers();
        return ResponseEntity.ok(usersResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long id, @RequestParam String password, 
                                  @RequestHeader("Authorization") String authorizationHeader) throws Exception {
    String token = authorizationHeader.replace("Bearer ", "");
    Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));

    if (id.equals(userId)) {
        UserDto userResponse = userService.updateUserPassword(id, password);
        return ResponseEntity.ok(userResponse);
    } else {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Message", "You are not allowed to change the password for this user");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMap);
    }
}


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", "user " + id + " deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorMap);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
        }
    }
}
