package travellin.travelblog.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import travellin.travelblog.entities.User;

@Data
@Builder
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String password;

    public UserDto() {}

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    public static List<UserDto> fromEntityList(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(fromEntity(user));
        }
        return dtos;
    }
}
