package travellin.travelblog.dto;

import java.util.ArrayList;
import java.util.List;

import travellin.travelblog.entities.User;

public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String password;

    public UserDto() {}

    public UserDto(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
