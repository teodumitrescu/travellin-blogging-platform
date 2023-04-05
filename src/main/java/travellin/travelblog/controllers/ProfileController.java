package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travellin.travelblog.entities.Profile;
import travellin.travelblog.entities.User;
import travellin.travelblog.services.ProfileService;
import travellin.travelblog.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        Optional<Profile> optionalProfile = profileService.getProfileById(id);
        if (optionalProfile.isPresent()) {
            return ResponseEntity.ok(optionalProfile.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

	@PostMapping
	public ResponseEntity<Profile> createProfile(@RequestBody Profile profile) throws Exception {
		User user = userService.getUserById(profile.getUser().getId());
		if (user == null) {
			throw new IllegalArgumentException("Invalid user ID");
		} else {
			Profile createdProfile = profileService.createProfile(
					profile.getFirstName(),
					profile.getLastName(),
					profile.getBio(),
					profile.getProfileImageUrl(),
					user
			);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
		}
	}
	
	

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfileById(@PathVariable Long id, @RequestBody Profile profile) {
        Optional<Profile> optionalProfile = profileService.getProfileById(id);
        if (optionalProfile.isPresent()) {
            Profile existingProfile = optionalProfile.get();
            existingProfile.setFirstName(profile.getFirstName());
            existingProfile.setLastName(profile.getLastName());
            existingProfile.setBio(profile.getBio());
            existingProfile.setProfileImageUrl(profile.getProfileImageUrl());
			Profile updatedProfile = profileService.updateProfile(existingProfile, profile.getFirstName(), profile.getLastName(), profile.getBio(), profile.getProfileImageUrl());
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileById(@PathVariable Long id) {
        Optional<Profile> optionalProfile = profileService.getProfileById(id);
        if (optionalProfile.isPresent()) {
            profileService.deleteProfileById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
