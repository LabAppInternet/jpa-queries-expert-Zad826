package cat.tecnocampus.fgcstations.persistence;

import cat.tecnocampus.fgcstations.application.DTOs.UserTopFriend;
import cat.tecnocampus.fgcstations.domain.Friend;
import cat.tecnocampus.fgcstations.domain.FriendId;
import cat.tecnocampus.fgcstations.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {
    @Query("SELECT f FROM Friend f " +
            "WHERE f.user =: user")
    List<Friend> findByUser(User user);

    @Query("SELECT " +
            "u.username AS username, " +
            "u.name AS name, " +
            "u.secondName AS secondName, " +
            "u.email AS email, " +
            "CONT(f) AS numberOfFriends " +
            "FROM Friend f " +
            "JOIN f.user u " +
            "GROUP BY u.username, u.name, u.secondName, u.email " +
            "ORDER BY COUNT (f) DESC ")
    List<UserTopFriend> findTop3UsersWithMostFavoriteJourneys(Pageable pageable);

    @Query("SELECT f FROM Friend f WHERE f.id.friend = :friendName")
    List<Friend> findFriendsByFriendName(@Param("friendName") String friendName);

}
