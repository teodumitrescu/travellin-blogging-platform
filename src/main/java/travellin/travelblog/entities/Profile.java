package travellin.travelblog.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "bio")
    private String bio;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Profile() {}

    public Profile(String firstName, String lastName, String bio, String profileImageUrl, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.user = user;
    }

    // getters and setters
}
