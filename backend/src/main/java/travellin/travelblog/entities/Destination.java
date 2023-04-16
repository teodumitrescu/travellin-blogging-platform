package travellin.travelblog.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "destination_name", nullable = false, length = 200, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "region", nullable = false)
    private String region;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    private List<BlogPost> posts = new ArrayList<>();

    public Destination() {}

    public Destination(String name, String description, String country, String region) {
        this.name = name;
        this.description = description;
        this.country = country;
        this.region = region;
    }
}

