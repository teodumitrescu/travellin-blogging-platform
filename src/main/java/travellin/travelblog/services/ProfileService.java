package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travellin.travelblog.entities.Profile;
import travellin.travelblog.entities.User;
import travellin.travelblog.repositories.ProfileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public Profile createProfile(String firstName, String lastName, String bio, String profileImageUrl, User user) {
        Profile profile = new Profile(firstName, lastName, bio, profileImageUrl, user);
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Profile existingProfile, String firstName, String lastName, String bio, String profileImageUrl) {
        if (firstName != null) {
            existingProfile.setFirstName(firstName);
        }
        if (lastName != null) {
            existingProfile.setLastName(lastName);
        }
        if (bio != null) {
            existingProfile.setBio(bio);
        }
        if (profileImageUrl != null) {
            existingProfile.setProfileImageUrl(profileImageUrl);
        }
        
        return profileRepository.save(existingProfile);
    }

    public void deleteProfileById(Long id) {
        profileRepository.deleteById(id);
    }
}
