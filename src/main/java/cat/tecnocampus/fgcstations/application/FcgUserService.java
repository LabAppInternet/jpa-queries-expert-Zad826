package cat.tecnocampus.fgcstations.application;

import cat.tecnocampus.fgcstations.application.DTOs.*;
import cat.tecnocampus.fgcstations.application.exception.UserDoesNotExistsException;
import cat.tecnocampus.fgcstations.application.mapper.MapperHelper;
import cat.tecnocampus.fgcstations.domain.FavoriteJourney;
import cat.tecnocampus.fgcstations.domain.Journey;
import cat.tecnocampus.fgcstations.domain.User;
import cat.tecnocampus.fgcstations.persistence.DayTimeStartRepository;
import cat.tecnocampus.fgcstations.persistence.FavoriteJourneyRepository;
import cat.tecnocampus.fgcstations.persistence.JourneyRepository;
import cat.tecnocampus.fgcstations.persistence.UserRepository;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FcgUserService {
    private final UserRepository userRepository;
    private final FavoriteJourneyRepository favoriteJourneyRepository;
    private final JourneyRepository journeyRepository;
    private final DayTimeStartRepository dayTimeStartRepository;
    private final FgcStationService fgcStationService;
    private final ModelMapper modelMapper;

    public FcgUserService(UserRepository userRepository, FavoriteJourneyRepository favoriteJourneyRepository, JourneyRepository journeyRepository, DayTimeStartRepository dayTimeStartRepository, FgcStationService fgcStationService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.favoriteJourneyRepository = favoriteJourneyRepository;
        this.journeyRepository = journeyRepository;
        this.dayTimeStartRepository = dayTimeStartRepository;
        this.fgcStationService = fgcStationService;
        this.modelMapper = modelMapper;
    }

    public UserDTO getUserDTO(String username) {
        //TODO 10.0: get the user (domain) given her username.
        User user = getDomainUser(username);

        // TODO 11.0: get the user's favorite journeys
        user.setFavoriteJourneyList(getFavoriteJourneys(username));

        //domain users are mapped to DTOs
        return MapperHelper.userToUserDTO(user);
    }

    public User getDomainUser(String username) throws UserDoesNotExistsException {
        // TODO 10.1: get the user (domain) given her username. If the user does not exist, throw a UserDoesNotExistsException
        //  You can solve this exercise without leaving this file
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserDoesNotExistsException(username));
    }


    public UserDTOnoFJ getUserDTOWithNoFavoriteJourneys(String username)throws UserDoesNotExistsException {
        // TODO 12: get the user (UserDTOnoFJ) given her username. If the user does not exist, throw a UserDoesNotExistsException
        User user = getDomainUser(username);
        if(favoriteJourneyRepository.existsFavoriteJourneyByUser(user)) return null;
        return modelMapper.map(user, UserDTOnoFJ.class);
    }

    public UserDTOInterface getUserDTOInterface(String username) {
        // TODO 13: get the user (UserDTOInterface) given her username. If the user does not exist, throw a UserDoesNotExistsException
        User user = getDomainUser(username);
        return modelMapper.map(user, UserDTOInterface.class);
    }

    public List<UserDTO> getUsers() {
        //TODO 14: get all users (domain). You can solve this exercise without leaving this file
        List<User> users = new ArrayList<>(); //feed this list with the users

        //get the users' favorite journeys
        users.forEach(u -> u.setFavoriteJourneyList(getFavoriteJourneys(u.getUsername())));

        return users.stream().map(MapperHelper::userToUserDTO).toList();
    }

    // TODO 22: This method updates a user. Make sure the user is saved without explicitly calling the save method
    public void updateUser(UserDTOnoFJ userDTO) {
        User user = getDomainUser(userDTO.username());
        user.setName(userDTO.name());
        user.setSecondName(userDTO.secondName());
        user.setEmail(userDTO.email());
    }

    public List<UserTopFavoriteJourney> getTop3UsersWithMostFavoriteJourneys() {
        Pageable top3 = PageRequest.of(0,3);
        return favoriteJourneyRepository.findTop3UsersWithMostFavoriteJourneys(top3);
    }

    public List<UserDTOInterface> getUsersByNameAndSecondName(String name, String secondName) {
        // TODO 17: get the users (UserDTOInterface) given their name and second name. Try not to use any sql (jpql) query
        return userRepository.findByNameAndSecondName(name,secondName).stream().map(user -> modelMapper.map(user, UserDTOInterface.class)).toList();
    }

    public List<FavoriteJourney> getFavoriteJourneys(String username) {
        User user = getDomainUser(username);

        // TODO 11.1: get the user's favorite journeys given the User (domain object)
        List<FavoriteJourney> favoriteJourneys = new ArrayList<>(); //feed this list with the favorite journeys
        favoriteJourneys = favoriteJourneyRepository.findFavoriteJourneysByUser(user);
        return favoriteJourneys;
    }

    public List<FavoriteJourneyDTO> getFavoriteJourneysDTO(String username) {
        User user = getDomainUser(username);
        List<FavoriteJourney> favoriteJourneys = new ArrayList<>(); //Same as TODO 11.1: feed this list with the favorite journeys
        favoriteJourneys = favoriteJourneyRepository.findFavoriteJourneysByUser(user);
        return favoriteJourneys.stream().map(MapperHelper::favoriteJourneyToFavoriteJourneyDTO).toList();
    }

    //Adding a favorite journey to the user. We need to save a favorite journey that didn't exist before
    public void addUserFavoriteJourney(String username, FavoriteJourneyDTO favoriteJourneyDTO) {
        FavoriteJourney favoriteJourney = convertFavoriteJourneyDTO(username, favoriteJourneyDTO);
        journeyRepository.save(favoriteJourney.getJourney());
        dayTimeStartRepository.saveAll(favoriteJourney.getDayTimeStarts());
        favoriteJourneyRepository.save(favoriteJourney);
        // TODO 19: explicitly save the journey. You can solve this exercise without leaving this file
        // TODO 20: explicitly save the favorite journey. You can solve this exercise without leaving this file
        // TODO 21: explicitly save all the dayTimeStarts of the favorite journey. You can solve this exercise without leaving this file
    }

    private FavoriteJourney convertFavoriteJourneyDTO(String username, FavoriteJourneyDTO favoriteJourneyDTO) {
        FavoriteJourney favoriteJourney = new FavoriteJourney();
        favoriteJourney.setUser(getDomainUser(username));
        favoriteJourney.setId(UUID.randomUUID().toString());
        Journey journey = new Journey(fgcStationService.getStation(favoriteJourneyDTO.getOrigin()),
                fgcStationService.getStation(favoriteJourneyDTO.getDestination()));
        favoriteJourney.setJourney(journey);

        return favoriteJourney;
    }

    public List<PopularDayOfWeek> getPopularDayOfWeek() {
        // TODO 18: get the most popular day of the week (PopularDayOfWeek) among the dayTimeStarts
        return dayTimeStartRepository.findPopularDays();
    }
}
