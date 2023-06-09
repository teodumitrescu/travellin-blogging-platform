package travellin.travelblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Destination;
import travellin.travelblog.entities.Tag;
import travellin.travelblog.entities.User;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByAuthor(User author);

	@Query("SELECT bp FROM BlogPost bp JOIN bp.tags t WHERE t = :tag")
    List<BlogPost> findByTags(Tag tag);

    @Query("SELECT bp FROM BlogPost bp WHERE bp.destination = :destination")
    List<BlogPost> findByDestination(Destination destination);

    List<BlogPost> findByAuthorId(Long id);

    List<BlogPost> findAllByDestinationId(Long destinationId);

    List<BlogPost> findAllByAuthorUsernameContainingIgnoreCase(String text);

    List<BlogPost> findAllByTitleContainingIgnoreCase(String text);
    
    List<BlogPost> findAllByDestinationNameContainingIgnoreCase(String text);

}
