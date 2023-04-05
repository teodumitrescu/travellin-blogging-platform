package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Destination;
import travellin.travelblog.repositories.DestinationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public Destination createDestination(String name, String description, String country, String region) {
        Destination destination = new Destination(name, description, country, region);
        return destinationRepository.save(destination);
    }

    public Destination updateDestination(Long id, String name, String description, String country, String region) throws Exception {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new Exception("Destination not found with id " + id));

        destination.setName(name);
        destination.setDescription(description);
        destination.setCountry(country);
        destination.setRegion(region);

        return destinationRepository.save(destination);
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Destination getDestinationById(Long id) throws Exception {
        return destinationRepository.findById(id)
                .orElseThrow(() -> new Exception("Destination not found with id " + id));
    }

    public List<BlogPost> getBlogPostsForDestination(Long destinationId) throws Exception {
        Optional<Destination> destination = destinationRepository.findById(destinationId);
        if (destination.isPresent()) {
            return destination.get().getPosts();
        } else {
            throw new Exception("Destination not found with id " + destinationId);
        }
    }

    public List<Destination> getDestinationsByCountry(String country) {
        return destinationRepository.findByCountry(country);
    }

    public List<Destination> getDestinationsByRegion(String region) {
        return destinationRepository.findByRegion(region);
    }

    public List<Destination> searchDestinations(String keyword) {
        return destinationRepository.findByNameContainingIgnoreCase(keyword);
    }

    public void deleteDestination(Long id) throws Exception {
        try {
            Destination destination = destinationRepository.findById(id)
                    .orElseThrow(() -> new Exception("Destination not found with id " + id));

            destinationRepository.delete(destination);
        } catch (DataAccessException e) {
            throw new Exception("Error deleting destination with id " + id, e);
        }
    }
}
