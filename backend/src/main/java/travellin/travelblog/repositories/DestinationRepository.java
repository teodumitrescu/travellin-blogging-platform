package travellin.travelblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import travellin.travelblog.entities.Destination;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    List<Destination> findByCountry(String country);

	List<Destination> findByNameContainingIgnoreCase(String name);

    Optional<Destination> findByNameAndCountry(String name, String country);


}
