package travellin.travelblog.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travellin.travelblog.entities.Profile;
import travellin.travelblog.entities.User;
import travellin.travelblog.repositories.ProfileRepository;
import travellin.travelblog.repositories.UserRepository;
import travellin.travelblog.dto.ProfileDto;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public List<ProfileDto> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();
        return ProfileDto.fromEntityList(profiles);
    }

    public ProfileDto getProfileById(Long id) throws Exception {
        Optional<Profile> profile = profileRepository.findById(id);
        if (profile.isPresent()) {
            return ProfileDto.fromEntity(profile.get());
        } else {
            throw new Exception("Profile not found: " + id);
        }
    }

    public ProfileDto getProfileByUserId(Long userId) throws Exception {
        Optional<Profile> profile = profileRepository.findByUserId(userId);
        if (profile.isPresent()) {
            return ProfileDto.fromEntity(profile.get());
        } else {
            throw new Exception("Profile not found: " + userId);
        }
    }

    public ProfileDto createProfile(ProfileDto profileDto, Long userId) {
        Profile profile = new Profile();
        BeanUtils.copyProperties(profileDto, profile);
        User user = userRepository.findById(userId).get();
        profile.setUser(user);
        user.setProfile(profile);
        Profile savedProfile = profileRepository.save(profile);
        userRepository.save(user);
        return ProfileDto.fromEntity(savedProfile);
    }

    public ProfileDto updateProfile(Long id, ProfileDto newProfile) throws Exception {
        Optional<Profile> profile = profileRepository.findById(id);
        if(profile.isPresent()) {
            Profile profileToUpdate = profile.get();
            if (newProfile.getFirstName() != null) {
                profileToUpdate.setFirstName(newProfile.getFirstName());
            }
            if (newProfile.getLastName() != null) {
                profileToUpdate.setLastName(newProfile.getLastName());
            }
            if (newProfile.getBio() != null) {
                profileToUpdate.setBio(newProfile.getBio());
            }
            if (newProfile.getProfileImageUrl() != null) {
                profileToUpdate.setProfileImageUrl(newProfile.getProfileImageUrl());
            }
            Profile updatedProfile = profileRepository.save(profileToUpdate);
            return ProfileDto.fromEntity(updatedProfile);
        } else {
            throw new Exception("Profile not found: " + id);
        }
        
    }

    public void deleteProfile(Long id, Long userId) throws Exception {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            profileRepository.deleteById(id);
            User user = userRepository.findById(userId).get();
            user.setProfile(null);
            userRepository.save(user);
        } else {
            throw new Exception("Profile not found: " + id);
        }
    }
}
