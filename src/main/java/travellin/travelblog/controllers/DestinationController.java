package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.dto.DestinationDto;
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
    public ResponseEntity<List<DestinationDto>> getAllDestinations() {
        List<DestinationDto> destinations = destinationService.getAllDestinations();
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationDto> getDestinationById(@PathVariable Long id) throws Exception {
        DestinationDto destination = destinationService.getDestinationById(id);
        return new ResponseEntity<>(destination, HttpStatus.OK);
    }

    // @GetMapping("/{id}/posts")
    // public ResponseEntity<List<BlogPost>> getBlogPostsForDestination(@PathVariable Long id) throws Exception {
    //     List<BlogPost> blogPosts = destinationService.getBlogPostsForDestination(id);
    //     return new ResponseEntity<>(blogPosts, HttpStatus.OK);
    // }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<DestinationDto>> getDestinationsByCountry(@PathVariable String country) {
        List<DestinationDto> destinations = destinationService.getDestinationsByCountry(country);
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<DestinationDto>> getDestinationsByRegion(@PathVariable String region) {
        List<DestinationDto> destinations = destinationService.getDestinationsByRegion(region);
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DestinationDto>> searchDestinations(@RequestParam String keyword) {
        List<DestinationDto> destinations = destinationService.searchDestinations(keyword);
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DestinationDto> createDestination(@RequestBody DestinationDto destinationDto) {
        DestinationDto createdDestination = destinationService.createDestination(destinationDto);
        return new ResponseEntity<>(createdDestination, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinationDto> updateDestination(@PathVariable Long id, @RequestBody DestinationDto destinationDto) throws Exception {
        DestinationDto updatedDestination = destinationService.updateDestination(id, destinationDto);
        return new ResponseEntity<>(updatedDestination, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) throws Exception {
        destinationService.deleteDestination(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
