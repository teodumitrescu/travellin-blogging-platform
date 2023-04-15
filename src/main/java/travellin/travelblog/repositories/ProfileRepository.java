package travellin.travelblog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import travellin.travelblog.entities.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

	Optional<Profile> findByUserId(Long userId);

}
