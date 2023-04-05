package travellin.travelblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import travellin.travelblog.entities.Destination;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    //List<Destination> findByPosts_BlogUser_Username(String username);

    List<Destination> findByRegion(String region);

    List<Destination> findByCountry(String country);

	List<Destination> findByNameContainingIgnoreCase(String name);

}
