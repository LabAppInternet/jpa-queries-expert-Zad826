package cat.tecnocampus.fgcstations.persistence;

import cat.tecnocampus.fgcstations.application.DTOs.UserTopFavoriteJourney;
import cat.tecnocampus.fgcstations.domain.FavoriteJourney;
import cat.tecnocampus.fgcstations.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface FavoriteJourneyRepository extends JpaRepository<FavoriteJourney, String> {

    //TODO optional: Try to implement the query to get the FavoriteJourneysDTO of a user with its list of DayTimeStartDTO.
    // Is it possible to do it with a single query?

    boolean existsFavoriteJourneyByUser(User user);

    @Query("SELECT " +
            "u.username AS username, " +
            "u.name AS name, " +
            "u.secondName AS secondName, " +
            "u.email AS email, " +
            "COUNT(fj) AS numberOfFavoriteJourneys " +
            "FROM FavoriteJourney fj " +
            "JOIN fj.user u " +
            "GROUP BY u.username, u.name, u.secondName, u.email " +
            "ORDER BY COUNT(fj) DESC")
    List<UserTopFavoriteJourney> findTop3UsersWithMostFavoriteJourneys(Pageable pageable);

    @Query("SELECT fj FROM FavoriteJourney fj " +
            "WHERE fj.user =: user")
    List<FavoriteJourney> findFavoriteJourneysByUser(User user);
}