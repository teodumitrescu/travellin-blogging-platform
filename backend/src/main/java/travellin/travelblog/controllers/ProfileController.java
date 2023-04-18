package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.dto.ProfileDto;
import travellin.travelblog.security.config.JwtService;
import travellin.travelblog.services.ProfileService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<ProfileDto>> getAllProfiles() {
        try {
            return ResponseEntity.ok(profileService.getAllProfiles());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getProfileById(@PathVariable Long id) {
        try {
            ProfileDto profile = profileService.getProfileById(id);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
        }
    }

    @GetMapping("/user={userId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getProfileByUserId(@PathVariable Long userId) {
        try {
            ProfileDto profile = profileService.getProfileByUserId(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ProfileDto> createProfile(@RequestBody ProfileDto profileDto,  @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));
            ProfileDto createdProfile = profileService.createProfile(profileDto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> updateProfileById(@PathVariable Long id, @RequestBody ProfileDto profileDto) {
        try {
            ProfileDto updatedProfileDto = profileService.updateProfile(id, profileDto);
            return ResponseEntity.ok(updatedProfileDto);
        }catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id,   @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));
    
            profileService.deleteProfile(id, userId);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", "Profile " + id + " deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorMap);
            
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
        }

    }
}
