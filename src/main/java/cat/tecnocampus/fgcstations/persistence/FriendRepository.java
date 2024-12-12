package cat.tecnocampus.fgcstations.persistence;

import cat.tecnocampus.fgcstations.application.DTOs.FriendUserDTO;
import cat.tecnocampus.fgcstations.application.DTOs.UserTopFriend;
import cat.tecnocampus.fgcstations.domain.Friend;
import cat.tecnocampus.fgcstations.domain.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    List<Friend> findByIdUsername(String username);

    @Query("SELECT u.username AS username, u.name AS name, u.secondName AS secondName, u.email AS email, COUNT(f) AS numberOfFriends " +
            "FROM User u LEFT JOIN Friend f ON u = f.user " +
            "GROUP BY u.username, u.name, u.secondName, u.email " +
            "ORDER BY COUNT(f) DESC")
    List<UserTopFriend> findTop3UsersWithMostFriends(Pageable pageable);

    @Query("SELECT f.user.username AS userUsername, f.user.name AS userName " +
            "FROM Friend f WHERE f.id.friend = :friendName")
    List<FriendUserDTO> findUsersByFriendName(String friendName);

}
