package cat.tecnocampus.fgcstations.domain;


import cat.tecnocampus.fgcstations.domain.exceptions.SameOriginDestinationException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Journey {

    @EmbeddedId
    JourneyId id;

    @ManyToOne
    @JoinColumn(name = "origin_station")
    private Station origin;

    @ManyToOne
    @JoinColumn(name = "destination_station")
    private Station destination;

    public Journey(Station origin, Station destination) {
        if (origin.sameStation(destination)) {
            throw new SameOriginDestinationException();
        }

        this.id = new JourneyId(origin.getName(), destination.getName());
        this.origin = origin;
        this.destination = destination;
    }

    public Journey() {
    }

    public Station getOrigin() {
        return origin;
    }

    public Station getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "id=" + id +
                ", origin=" + getOrigin() +
                ", destination=" + getDestination() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Journey journey)) return false;

        return id.equals(journey.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public JourneyId getId() {
        return id;
    }
}
