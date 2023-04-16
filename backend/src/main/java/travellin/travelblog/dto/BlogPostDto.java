package travellin.travelblog.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import travellin.travelblog.entities.BlogPost;

@Data
@Builder
@AllArgsConstructor
public class BlogPostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto author;
    private DestinationDto destination;
    private List<ImageDto> images;
    private List<TagDto> tags;

    public BlogPostDto() {}

	public static BlogPostDto fromEntity(BlogPost blogPost) {
        UserDto authorDto = UserDto.fromEntity(blogPost.getAuthor());
        DestinationDto destinationDto = blogPost.getDestination() != null
        ? DestinationDto.fromEntity(blogPost.getDestination())
        : null;
        List<ImageDto> imageDtos = blogPost.getImages()
                .stream()
                .map(ImageDto::fromEntity)
                .collect(Collectors.toList());
        List<TagDto> tagDtos = blogPost.getTags()
                .stream()
                .map(TagDto::fromEntity)
                .collect(Collectors.toList());

        return new BlogPostDto(
                blogPost.getId(),
                blogPost.getTitle(),
                blogPost.getContent(),
                blogPost.getCreatedAt(),
                blogPost.getUpdatedAt(),
                authorDto,
                destinationDto,
                imageDtos,
                tagDtos
        );
    }

    public static List<BlogPostDto> fromEntityList(List<BlogPost> blogPosts) {
        return blogPosts.stream()
        .map(BlogPostDto::fromEntity)
        .collect(Collectors.toList());
    }
}
