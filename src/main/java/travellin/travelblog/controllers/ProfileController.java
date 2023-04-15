package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.dto.ProfileDto;
import travellin.travelblog.security.config.JwtService;
import travellin.travelblog.services.ProfileService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private ProfileService profileService;
    private JwtService jwtService;

    @Autowired
    public ProfileController(ProfileService profileService, JwtService jwtService) {
        this.profileService = profileService;
        this.jwtService = jwtService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ProfileDto> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getProfileById(@PathVariable Long id) {
        Optional<ProfileDto> optionalProfile = profileService.getProfileById(id);
        if (optionalProfile.isPresent()) {
            return ResponseEntity.ok(optionalProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user={userId}")
    public ResponseEntity<?> getProfileByUserId(@PathVariable Long userId) {
        Optional<ProfileDto> optionalProfile = profileService.getProfileByUserId(userId);
        if (optionalProfile.isPresent()) {
            return ResponseEntity.ok(optionalProfile.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ProfileDto> createProfile(@RequestBody ProfileDto profileDto,  @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));

        ProfileDto createdProfile = profileService.createProfile(profileDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDto> updateProfileById(@PathVariable Long id, @RequestBody ProfileDto profileDto) {
        Optional<ProfileDto> optionalProfile = profileService.getProfileById(id);
        if (optionalProfile.isPresent()) {
            ProfileDto updatedProfileDto = profileService.updateProfile(id, profileDto);
            return ResponseEntity.ok(updatedProfileDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteProfileById(@PathVariable Long id) {
        Optional<ProfileDto> optionalProfile = profileService.getProfileById(id);
        if (optionalProfile.isPresent()) {
            profileService.deleteProfileById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
