package cat.tecnocampus.fgcstations.persistence;

import cat.tecnocampus.fgcstations.application.DTOs.UserDTOInterface;
import cat.tecnocampus.fgcstations.application.DTOs.UserDTOnoFJ;
import cat.tecnocampus.fgcstations.application.DTOs.UserTopFavoriteJourney;
import cat.tecnocampus.fgcstations.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u " +
            "WHERE u.name =: name " +
            "AND u.secondName =: secondName")
    List<User> findByNameAndSecondName(@Param("name") String name, @Param("secondName") String secondName);
}
