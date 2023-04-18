package travellin.travelblog.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travellin.travelblog.dto.BlogPostDto;
import travellin.travelblog.dto.DestinationDto;
import travellin.travelblog.dto.ImageDto;
import travellin.travelblog.dto.TagDto;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Destination;
import travellin.travelblog.entities.Image;
import travellin.travelblog.entities.Tag;
import travellin.travelblog.entities.User;
import travellin.travelblog.repositories.BlogPostRepository;
import travellin.travelblog.repositories.DestinationRepository;
import travellin.travelblog.repositories.ImageRepository;
import travellin.travelblog.repositories.TagRepository;
import travellin.travelblog.repositories.UserRepository;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final TagRepository tagRepository;
	private final UserRepository userRepository;
	private final ImageRepository imageRepository;
	private final DestinationService destinationService;
	private final ImageService imageService;
	private final TagService tagService;
	private final DestinationRepository destinationRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, TagRepository tagRepository, UserRepository userRepository, ImageRepository imageRepository, DestinationService destinationService, TagService tagService,  DestinationRepository destinationRepository, ImageService imageService) {
        this.blogPostRepository = blogPostRepository;
        this.tagRepository = tagRepository;
		this.userRepository = userRepository;
		this.imageRepository = imageRepository;
		this.destinationService = destinationService;
		this.tagService = tagService;
		this.destinationRepository = destinationRepository;
		this.imageService = imageService;

    }

    public BlogPostDto createBlogPost(BlogPostDto blogPostDto, Long userId) throws Exception {
        User author = userRepository.findById(userId).get();
		List<Tag> TagsList = new ArrayList<>();
		List<Image> ImagesList = new ArrayList<>();

		//create basic blgpost
        BlogPost blogPost = new BlogPost(
			blogPostDto.getTitle(),
			blogPostDto.getContent(),
			LocalDateTime.now(),
			LocalDateTime.now(),
			author, null);
        blogPost.setTags(TagsList);
        blogPost.setImages(ImagesList);

		//get newly created blogpost
        BlogPost savedBlogPost = blogPostRepository.save(blogPost); //aici primeste un id
		Long newPostId = savedBlogPost.getId();

		//create and add images
		List<ImageDto> createdImagesDtos = new ArrayList<>();
        for (ImageDto imageDto : blogPostDto.getImages()) {
			ImageDto createdImage = imageService.createImage(newPostId, imageDto);
			addImageToBlogPost(newPostId, createdImage.getId());
			createdImagesDtos.add(createdImage);
        }

		//create and add tags
		List<TagDto> createdTagsDtos = new ArrayList<>(); 
		for (TagDto tagDto : blogPostDto.getTags()) {
			TagDto createdTag = tagService.createTag(tagDto);
			addTagToBlogPost(newPostId, createdTag.getId());
			createdTagsDtos.add(createdTag);
		}
		
		DestinationDto createdDestinationDto = destinationService.createDestination(blogPostDto.getDestination());
		BlogPost finalBlogPost = blogPostRepository.findById(newPostId).get();
		finalBlogPost.setDestination(destinationRepository.findById(createdDestinationDto.getId()).get());
		addBlogPostToDestination(newPostId, createdDestinationDto.getId());

        return BlogPostDto.fromEntity(blogPostRepository.save(blogPost));
    }

    public BlogPostDto updateBlogPost(Long id, BlogPostDto postDto) throws Exception {
		BlogPost blogPost = blogPostRepository.findById(id)
		.orElseThrow(() -> new Exception("BlogPost not found with id " + id));

		if (postDto.getTitle() != null) {
			blogPost.setTitle(postDto.getTitle());
		}
		if(postDto.getContent() != null) {
			blogPost.setContent(postDto.getContent());
		}
        
		if(postDto.getImages() != null) {
			//delete current images
			List<Image> images = blogPost.getImages();
			for (Image image : images) {
				removeImageFromBlogPost(blogPost.getId(), image.getId());
			}

			//create and add images
			List<ImageDto> createdImagesDtos = new ArrayList<>();
			for (ImageDto imageDto : postDto.getImages()) {
				ImageDto createdImage = imageService.createImage(id, imageDto);
				addImageToBlogPost(id, createdImage.getId());
				createdImagesDtos.add(createdImage);
			}
		}
		
		if (postDto.getTags() != null) {
			//remove current tags
			List<Tag> tags = blogPost.getTags();
			for(Tag tag : tags) {
				List<BlogPost> posts = tag.getPosts();
				posts.remove(blogPost);
				tag.setPosts(posts);
				tagRepository.save(tag);
			}

			//create and add tags
			List<TagDto> createdTagsDtos = new ArrayList<>(); 
			for (TagDto tagDto : postDto.getTags()) {
				TagDto createdTag = tagService.createTag(tagDto);
				addTagToBlogPost(id, createdTag.getId());
				createdTagsDtos.add(createdTag);
			}
		}
		
		if (postDto.getDestination() != null) {
			//remove current destination
			Destination destination = blogPost.getDestination();
			if(destination != null) {
				List<BlogPost> posts = destination.getPosts();
				posts.remove(blogPost);
				destination.setPosts(posts);
				destinationRepository.save(destination);
			}
			
			//add new destination
			DestinationDto createdDestinationDto = destinationService.createDestination(postDto.getDestination());
			BlogPost finalBlogPost = blogPostRepository.findById(id).get();
			finalBlogPost.setDestination(destinationRepository.findById(createdDestinationDto.getId()).get());
			addBlogPostToDestination(id, createdDestinationDto.getId());
			blogPost.setUpdatedAt(LocalDateTime.now());
		}
		
        return BlogPostDto.fromEntity(blogPostRepository.save(blogPost));
    }

    public List<BlogPostDto> getAllBlogPostsByUserId(Long userId) {
        List<BlogPost> blogPosts = blogPostRepository.findByAuthorId(userId);
        return BlogPostDto.fromEntityList(blogPosts);
    }

	public List<BlogPostDto> getAllBlogPosts() {
        List<BlogPost> blogPosts = blogPostRepository.findAll();
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

		List<Tag> tags = blogPost.getTags();
		tags.add(tag);
		blogPost.setTags(tags);
		blogPost.setUpdatedAt(LocalDateTime.now());
		blogPostRepository.save(blogPost);

		List<BlogPost> posts = tag.getPosts();
		posts.add(blogPost);
		tag.setPosts(posts);
        tagRepository.save(tag);
    }
	
	public void removeTagFromBlogPost(Long blogPostId, Long tagId) throws Exception {
		BlogPost blogPost = blogPostRepository.findById(blogPostId)
				.orElseThrow(() -> new Exception("Blog post not found with id " + blogPostId));
	
		Tag tag = tagRepository.findById(tagId)
				.orElseThrow(() -> new Exception("Tag not found with id " + tagId));
	
		
		List<Tag> tags = blogPost.getTags();
		tags.remove(tag);
		blogPost.setTags(tags);
		blogPost.setUpdatedAt(LocalDateTime.now());
		blogPostRepository.save(blogPost);
	}

	public void addImageToBlogPost(Long blogPostId, Long imageId) {
		BlogPost post = blogPostRepository.findById(blogPostId).get();
		List<Image> images = post.getImages();
		images.add(imageRepository.findById(imageId).get());
		post.setImages(images);
		post.setUpdatedAt(LocalDateTime.now());
		blogPostRepository.save(post);
	}

	public void removeImageFromBlogPost(Long blogPostId, Long imageId) throws Exception {
		BlogPost post = blogPostRepository.findById(blogPostId)
				.orElseThrow(() -> new Exception("Blog post not found with id " + blogPostId));
	
		Image image = imageRepository.findById(imageId)
				.orElseThrow(() -> new Exception("Image not found with id " + imageId));
		
		List<Image> images = post.getImages();
		images.remove(image);
		post.setImages(images);
		post.setUpdatedAt(LocalDateTime.now());
		imageRepository.delete(image);
		blogPostRepository.save(post);
	}

	public void addBlogPostToDestination(Long blogPostId, Long destinationId) throws Exception {
		BlogPost post = blogPostRepository.findById(blogPostId)
		.orElseThrow(() -> new Exception("Blog post not found with id " + blogPostId));;
		Destination destination = destinationRepository.findById(destinationId)
		.orElseThrow(() -> new Exception("Destination not found with id " + destinationId));
		post.setDestination(destination);
		post.setUpdatedAt(LocalDateTime.now());
		blogPostRepository.save(post);

		
		List<BlogPost> posts = destination.getPosts();
		posts.add(post);
		destination.setPosts(posts);
		destinationRepository.save(destination);
	}

	public void removeBlogPostFromDestination(Long blogPostId, Long destinationId) throws Exception {
		BlogPost post = blogPostRepository.findById(blogPostId)
				.orElseThrow(() -> new Exception("Blog post not found with id " + blogPostId));
	
		Destination destination = destinationRepository.findById(destinationId)
				.orElseThrow(() -> new Exception("Destination not found with id " + destinationId));
		
		List<BlogPost> posts = destination.getPosts();
		posts.remove(post);
		destination.setPosts(posts);
		destinationRepository.save(destination);
		post.setDestination(null);
		post.setUpdatedAt(LocalDateTime.now());
		blogPostRepository.save(post);
	}
	
	public void deleteBlogPost(Long id) throws Exception {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new Exception("BlogPost not found: " + id));
		
		// Remove blogPost from destination
		Destination destination = post.getDestination();
		if(destination != null) {
			List<BlogPost> posts = destination.getPosts();
			posts.remove(post);
			destination.setPosts(posts);
			destinationRepository.save(destination);
		}

		// Remove blogPost from tags
		List<Tag> tags = post.getTags();
		for(Tag tag : tags) {
			List<BlogPost> posts = tag.getPosts();
			posts.remove(post);
			tag.setPosts(posts);
			tagRepository.save(tag);
		}

		//delete associated images
		List<Image> images = post.getImages();
		for (Image image : images) {
			removeImageFromBlogPost(post.getId(), image.getId());
		}
		
        blogPostRepository.deleteById(id);
    }

	public void removeDestinationfromBlogPosts(Long destId) {
		List<BlogPost> posts = blogPostRepository.findAllByDestinationId(destId);
		for (BlogPost post : posts) {
			post.setDestination(null);
			post.setUpdatedAt(LocalDateTime.now());
			blogPostRepository.save(post);
		}
	}
	
}
