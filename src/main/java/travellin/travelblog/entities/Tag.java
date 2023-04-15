package travellin.travelblog.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name", nullable = false, length = 200, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<BlogPost> posts = new ArrayList<>();

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }
}