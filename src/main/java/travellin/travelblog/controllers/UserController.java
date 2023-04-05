package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import travellin.travelblog.entities.User;
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
	public ResponseEntity<User> createUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
		try {
			User user = userService.createUser(username, email, password);
			return ResponseEntity.status(HttpStatus.CREATED).body(user);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username or email already exists", e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		try {
			User user = userService.getUserById(id);
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<List<User>> getUsersByUsernameContaining(@RequestParam String username) {
		List<User> users = userService.getUsersByUsernameContaining(username);
		if (users.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found with the provided username");
		}
		return ResponseEntity.ok(users);
	}

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

	@PutMapping("/{id}/password")
	public ResponseEntity<User> updateUserPassword(@PathVariable Long id, @RequestParam String password) {
		try {
			User user = userService.updateUserPassword(id, password);
			return ResponseEntity.ok(user);
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
