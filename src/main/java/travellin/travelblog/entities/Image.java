package travellin.travelblog.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "url")
    private String url;

    @Column(name = "caption")
    private String caption;

    @Column(name = "alt_text")
    private String altText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private BlogPost post;

    public Image() {}

    public Image(String filename, String url, String caption, String altText, BlogPost post) {
        this.filename = filename;
        this.url = url;
        this.caption = caption;
        this.altText = altText;
        this.post = post;
    }

    // getters and setters
}

