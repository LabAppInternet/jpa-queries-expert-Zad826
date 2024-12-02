package cat.tecnocampus.fgcstations.application;


import cat.tecnocampus.fgcstations.application.DTOs.FriendUserDTO;
import cat.tecnocampus.fgcstations.application.DTOs.UserFriendsDTO;
import cat.tecnocampus.fgcstations.application.DTOs.UserTopFriend;
import cat.tecnocampus.fgcstations.application.mapper.MapperHelper;
import cat.tecnocampus.fgcstations.domain.Friend;
import cat.tecnocampus.fgcstations.domain.User;
import cat.tecnocampus.fgcstations.persistence.FriendRepository;
import cat.tecnocampus.fgcstations.persistence.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FgcFriendService {
    private final FriendRepository friendRepository;
    private final FcgUserService fcgUserService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public FgcFriendService(FriendRepository friendRepository, FcgUserService fcgUserService, UserRepository userRepository, ModelMapper modelMapper) {
        this.friendRepository = friendRepository;
        this.fcgUserService = fcgUserService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserFriendsDTO getUserFriends(String username) {
        User user = fcgUserService.getDomainUser(username);

        // TODO 20: find all the friends of a user given her username. You can solve this exercise without any sql query
        //feed the list with the friends of the user
        List<Friend> friends = new ArrayList<>(friendRepository.findByUser(user));
        return MapperHelper.listOfAUserFriendsToUserFriendsDTO(friends);
    }

    public List<UserFriendsDTO> getAllUserFriends() {
        // TODO 21: find all the friends (domain) of all users. You can solve this exercise without leaving this file
        //  note that domain objects are mapped to DTOs

        List<User> users = userRepository.findAll();
        List<Friend> friends = users.stream()
                .flatMap(user -> friendRepository.findByUser(user).stream())
                .toList();
        return MapperHelper.allUserFriendListToListUserFriendsDTO(friends);


    }

    public void saveFriends(UserFriendsDTO userFriendsDTO) {
        User user = fcgUserService.getDomainUser(userFriendsDTO.getUsername());
        friendRepository.saveAll(MapperHelper.friendsDTOToUserListOfFriends(user, userFriendsDTO));
    }

    public List<UserTopFriend> getTop3UsersWithMostFriends() {
        // TODO 22: find the top 3 users with the most friends.
        Pageable top3 = PageRequest.of(0,3);
        return friendRepository.findTop3UsersWithMostFavoriteJourneys(top3);
    }

    // Find all users whose friends have a certain name
    public List<FriendUserDTO> getUsersByFriend(String friendName) {
        // TODO 23: find all users whose friends have a certain name.
        List<Friend> friends = friendRepository.findFriendsByFriendName(friendName);
        return friends.stream().map(friend -> modelMapper.map(friend, FriendUserDTO.class)).toList();
    }

}
