package cat.tecnocampus.fgcstations.application;

import cat.tecnocampus.fgcstations.application.DTOs.StationDTO;
import cat.tecnocampus.fgcstations.application.DTOs.StationTopFavoriteJourney;
import cat.tecnocampus.fgcstations.application.exception.StationDoesNotExistsException;
import cat.tecnocampus.fgcstations.application.mapper.MapperHelper;
import cat.tecnocampus.fgcstations.domain.Station;
import cat.tecnocampus.fgcstations.persistence.StationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FgcStationService {
    private final StationRepository stationRepository;
    private final ModelMapper modelMapper;
    public FgcStationService(StationRepository stationRepository, ModelMapper modelMapper) {
        this.stationRepository = stationRepository;
        this.modelMapper = modelMapper;
    }

    public List<StationDTO> getStationsDTO() {
        return stationRepository.findAll().stream().map(station -> modelMapper.map(station, StationDTO.class)).toList();
    }

    public List<Station> getStationsDomain() {
        //TODO 2: get all stations (see you return a domain Station). Actually you don't need to leave this file
        // in order to complete this exercise
        return stationRepository.findAll();
    }

    public Station getStation(String name) throws StationDoesNotExistsException {
        // TODO 3: get a station by name (see the returned type). If the station does not exist, throw a StationDoesNotExistsException
        //  you won't need to write any sql (jpql) query
        return stationRepository.findByName(name)
                .orElseThrow(()-> new StationDoesNotExistsException(name));
    }

    public StationDTO getStationDTO(String name) throws StationDoesNotExistsException {
        // TODO 4: get a station by name (see the returned type). If the station does not exist, throw a StationDoesNotExistsException
        Station station = stationRepository.findByName(name)
                .orElseThrow(()-> new StationDoesNotExistsException(name));
        return modelMapper.map(station, StationDTO.class);
    }

    public List<StationTopFavoriteJourney> getStationsOrderedByFavoriteJourneysAsEitherOriginOrDestination() {
        // TODO 5: this is an optional exercise because is quite tricky. You need to use a native query because is no possible to
        //  have a derived table (subquery).
        //  You first need to use a 'UNION' to get the stations either as origin or destination of a Journey. Then you need to group and count
        return null;
    }
}
