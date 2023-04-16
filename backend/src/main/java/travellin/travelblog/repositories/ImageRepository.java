package travellin.travelblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import travellin.travelblog.entities.Image;

import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}