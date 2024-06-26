package github.maxsplawski.realworld.domain.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    default User findByUsernameOrThrow(String username) {
        return this.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(username));
    }
}
