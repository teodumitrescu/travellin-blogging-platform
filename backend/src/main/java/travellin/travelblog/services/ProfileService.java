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

    public Optional<ProfileDto> getProfileById(Long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        if (profile.isPresent()) {
            return Optional.of(ProfileDto.fromEntity(profile.get()));
        }
        return Optional.empty();
    }

    public Optional<ProfileDto> getProfileByUserId(Long userId) {
        Optional<Profile> profile = profileRepository.findByUserId(userId);
        if (profile.isPresent()) {
            return Optional.of(ProfileDto.fromEntity(profile.get()));
        }
        return Optional.empty();
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

    public ProfileDto updateProfile(Long id, ProfileDto newProfile) {
        Profile profileToUpdate = profileRepository.findById(id).get();
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
    }

    public void deleteProfile(Long id, Long userId) {
        profileRepository.deleteById(id);
        User user = userRepository.findById(userId).get();
        user.setProfile(null);
        userRepository.save(user);
    }
}
