package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.User;
import travellin.travelblog.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

	public User createUser(String username, String email, String password) throws Exception {
		if (userRepository.existsByUsername(username)) {
			throw new Exception("Username already exists: " + username);
		}
		if (userRepository.existsByEmail(email)) {
			throw new Exception("Email already exists: " + email);
		}
		User user = new User(username, email, password);
		return userRepository.save(user);
	}
	

    public User updateUserPassword(Long userId, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(password);
            return userRepository.save(user);
        } else {
            throw new Exception("User not found: " + userId);
        }
    }

    public User getUserById(Long userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new Exception("User not found: " + userId);
        }
    }

	public User getUserByUsername(String username) throws Exception {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isPresent()) {
			return userOptional.get();
		} else {
			throw new Exception("User not found with username: " + username);
		}
	}

	public List<User> getUsersByUsernameContaining(String username) {
        return userRepository.findAllByUsernameContaining(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
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

    public List<BlogPost> getBlogPostsForUser(Long userId) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<BlogPost> posts = user.getPosts();
            return posts;
        } else {
            throw new Exception("User not found: " + userId);
        }
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}

