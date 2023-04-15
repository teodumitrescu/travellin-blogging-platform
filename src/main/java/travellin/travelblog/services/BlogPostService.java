package travellin.travelblog.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travellin.travelblog.dto.BlogPostDto;
import travellin.travelblog.dto.DestinationDto;
import travellin.travelblog.dto.TagDto;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Destination;
import travellin.travelblog.entities.Image;
import travellin.travelblog.entities.Tag;
import travellin.travelblog.entities.User;
import travellin.travelblog.repositories.BlogPostRepository;
import travellin.travelblog.repositories.ImageRepository;
import travellin.travelblog.repositories.TagRepository;
import travellin.travelblog.repositories.UserRepository;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final TagRepository tagRepository;
	private final UserRepository userRepository;
	private final ImageRepository imageRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, TagRepository tagRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.blogPostRepository = blogPostRepository;
        this.tagRepository = tagRepository;
		this.userRepository = userRepository;
		this.imageRepository = imageRepository;
    }

    public BlogPostDto createBlogPost(BlogPostDto blogPostDto, Long userId) {
        User author = userRepository.findById(userId).get();
		List<Tag> emptyTagsList = new ArrayList<>();
		List<Image> emptyImagesList = new ArrayList<>();

        BlogPost blogPost = new BlogPost(
			blogPostDto.getTitle(),
			blogPostDto.getContent(),
			LocalDateTime.now(),
			LocalDateTime.now(),
			author, null);
        blogPost.setTags(emptyTagsList);
        blogPost.setImages(emptyImagesList);

        BlogPost savedBlogPost = blogPostRepository.save(blogPost);

        return BlogPostDto.fromEntity(savedBlogPost);
    }

    public BlogPost updateBlogPost(Long id, String title, String content, LocalDateTime updatedAt, List<Image> images, Destination destination, List<Tag> tags) throws Exception {
		BlogPost blogPost = blogPostRepository.findById(id)
		.orElse(null);

	if (blogPost == null) {
		throw new Exception("BlogPost not found: " + id);
}        blogPost.setTitle(title);
        blogPost.setContent(content);
        blogPost.setUpdatedAt(updatedAt);
        blogPost.setImages(images);
        blogPost.setTags(tags);
        blogPost.setDestination(destination);
        return blogPostRepository.save(blogPost);
    }

    public List<BlogPostDto> getAllBlogPostsByUserId(Long userId) {
        List<BlogPost> blogPosts = blogPostRepository.findByAuthorId(userId);
        return BlogPostDto.fromEntityList(blogPosts);
    }

	public BlogPostDto getBlogPostById(Long id) throws Exception {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new Exception("BlogPost not found: " + id));
        return BlogPostDto.fromEntity(blogPost);
    }

    public void addTagToBlogPost(Long blogPostId, Long tagId) throws Exception {
        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new Exception("Blog post not found: " + blogPostId));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new Exception("Tag not found: " + tagId));

        blogPost.addTag(tag);
        blogPostRepository.save(blogPost);
    }
	
	public void removeTagFromBlogPost(Long blogPostId, Long tagId) throws Exception {
		BlogPost blogPost = blogPostRepository.findById(blogPostId)
				.orElseThrow(() -> new Exception("Blog post not found with id " + blogPostId));
	
		Tag tag = tagRepository.findById(tagId)
				.orElseThrow(() -> new Exception("Tag not found with id " + tagId));
	
		blogPost.removeTag(tag);
		blogPostRepository.save(blogPost);
	}

	public void addImageToBlogPost(Long blogPostId, Long imageId) {
		BlogPost post = blogPostRepository.findById(blogPostId).get();
		List<Image> images = post.getImages();
		images.add(imageRepository.findById(imageId).get());
		post.setImages(images);
		blogPostRepository.save(post);
	}
	
	// public List<BlogPostDto> getAllBlogPostsByTagName(String tagName) {
	// 	Tag tag = tagRepository.findByName(tagName)
	// 			.orElseThrow(() -> new RuntimeException("Tag not found: " + tagName));
	// 	List<BlogPost> blogPosts = blogPostRepository.findByTags(tag);
	// 	return blogPosts.stream()
	// 			.map(BlogPostDto::fromEntity)
	// 			.collect(Collectors.toList());
	// }
	
	// public List<BlogPostDto> getAllBlogPostsByDestinationName(String destinationName) {
	// 	Destination destination = destinationRepository.findByName(destinationName)
	// 			.orElseThrow(() -> new RuntimeException("Destination not found: " + destinationName));
	// 	List<BlogPost> blogPosts = blogPostRepository.findByDestination(destination);
	// 	return blogPosts.stream()
	// 			.map(BlogPostDto::fromEntity)
	// 			.collect(Collectors.toList());
	// }
	
}
