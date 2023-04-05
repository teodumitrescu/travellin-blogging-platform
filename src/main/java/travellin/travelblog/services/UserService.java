package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import travellin.travelblog.dto.UserDto;
import travellin.travelblog.entities.User;
import travellin.travelblog.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto userDto) throws Exception {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new Exception("Username already exists: " + userDto.getUsername());
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new Exception("Email already exists: " + userDto.getEmail());
        }
        User user = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        user = userRepository.save(user);
        return new UserDto(user);
    }

    public UserDto updateUserPassword(Long userId, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(password);
            user = userRepository.save(user);
            return new UserDto(user);
        } else {
            throw new Exception("User not found: " + userId);
        }
    }

    public UserDto getUserById(Long userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserDto(user);
        } else {
            throw new Exception("User not found: " + userId);
        }
    }

    public UserDto getUserByUsername(String username) throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserDto(user);
        } else {
            throw new Exception("User not found with username: " + username);
        }
    }

    public List<UserDto> getUsersByUsernameContaining(String username) {
        List<User> users = userRepository.findAllByUsernameContaining(username);
        return users.stream().map(UserDto::new).collect(Collectors.toList());
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDto::new).collect(Collectors.toList());
    }

    public void deleteUserById(Long userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new Exception("User not found: " + userId);
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

	public UserDto findByUsername(String username) throws Exception {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			return new UserDto(user);
		} else {
            throw new Exception("User not found: " + username);
		}
	}
	
	public UserDto findByEmail(String email) throws Exception {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			return new UserDto(user);
		} else {
            throw new Exception("User not found: " + email);
		}
	}
}
	

