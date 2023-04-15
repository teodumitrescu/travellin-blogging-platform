package travellin.travelblog.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import travellin.travelblog.entities.Profile;

@Data
@Builder
@AllArgsConstructor
public class ProfileDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private String profileImageUrl;
    private Long userId;

    public ProfileDto() {}

    public static ProfileDto fromEntity(Profile profile) {
        return new ProfileDto(
                profile.getId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getBio(),
                profile.getProfileImageUrl(),
                profile.getUser().getId()
        );
    }

    public static List<ProfileDto> fromEntityList(List<Profile> profiles) {
        List<ProfileDto> dtos = new ArrayList<>();
        for (Profile profile : profiles) {
            dtos.add(fromEntity(profile));
        }
        return dtos;
    }
}
