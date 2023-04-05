package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import travellin.travelblog.dto.UserDto;
import travellin.travelblog.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        try {
            UserDto userResponse = userService.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username or email already exists", e);
        }
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
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> usersResponse = userService.getAllUsers();
        return ResponseEntity.ok(usersResponse);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<UserDto> updateUserPassword(@PathVariable Long id, @RequestParam String password) {
        try {
            UserDto userResponse = userService.updateUserPassword(id, password);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user with id: " + id + ". User not found", e);
        }
    }
}
