package travellin.travelblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import travellin.travelblog.entities.Destination;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    
}
