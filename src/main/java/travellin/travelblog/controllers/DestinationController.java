package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Destination;
import travellin.travelblog.services.DestinationService;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    @Autowired
    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public ResponseEntity<List<Destination>> getAllDestinations() {
        List<Destination> destinations = destinationService.getAllDestinations();
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) throws Exception {
        Destination destination = destinationService.getDestinationById(id);
        return new ResponseEntity<>(destination, HttpStatus.OK);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<BlogPost>> getBlogPostsForDestination(@PathVariable Long id) throws Exception {
        List<BlogPost> blogPosts = destinationService.getBlogPostsForDestination(id);
        return new ResponseEntity<>(blogPosts, HttpStatus.OK);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<Destination>> getDestinationsByCountry(@PathVariable String country) {
        List<Destination> destinations = destinationService.getDestinationsByCountry(country);
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<Destination>> getDestinationsByRegion(@PathVariable String region) {
        List<Destination> destinations = destinationService.getDestinationsByRegion(region);
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Destination>> searchDestinations(@RequestParam String keyword) {
        List<Destination> destinations = destinationService.searchDestinations(keyword);
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Destination> createDestination(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String country,
            @RequestParam String region
    ) {
        Destination destination = destinationService.createDestination(name, description, country, region);
        return new ResponseEntity<>(destination, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destination> updateDestination(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String country,
            @RequestParam String region
    ) throws Exception {
        Destination destination = destinationService.updateDestination(id, name, description, country, region);
        return new ResponseEntity<>(destination, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) throws Exception {
        destinationService.deleteDestination(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
