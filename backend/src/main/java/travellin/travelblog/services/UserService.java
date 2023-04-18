package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import travellin.travelblog.dto.UserDto;
import travellin.travelblog.entities.User;
import travellin.travelblog.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto updateUserPassword(Long userId, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(password));
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
            return UserDto.fromEntity(user);
        } else {
            throw new Exception("User not found: " + userId);
        }
    }

    public UserDto getUserByUsername(String username) throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return UserDto.fromEntity(user);
        } else {
            throw new Exception("User not found with username: " + username);
        }
    }

    public List<UserDto> getUsersByUsernameContaining(String username) {
        List<User> users = userRepository.findAllByUsernameContaining(username);
        return UserDto.fromEntityList(users);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserDto.fromEntityList(users);
    }

    public void deleteUserById(Long userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new Exception("User not found: " + userId);
        }
    }

	public UserDto findByUsername(String username) throws Exception {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			return UserDto.fromEntity(user);
		} else {
            throw new Exception("User not found: " + username);
		}
	}
	
}
	

