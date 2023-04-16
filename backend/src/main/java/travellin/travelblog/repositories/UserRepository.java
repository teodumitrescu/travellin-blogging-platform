package travellin.travelblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import travellin.travelblog.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllByUsernameContaining(String username);

	Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

	Object findByUsernameOrEmail(String username, String email);

}
