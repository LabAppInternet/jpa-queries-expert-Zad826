package cat.tecnocampus.fgcstations.persistence;

import cat.tecnocampus.fgcstations.application.DTOs.PopularDayOfWeek;
import cat.tecnocampus.fgcstations.domain.DayTimeStart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DayTimeStartRepository extends JpaRepository<DayTimeStart, String> {

    @Query("SELECT d.dayOfWeek AS dayOfWeek, COUNT(d) AS count " +
            "FROM DayTimeStart d " +
            "GROUP BY d.dayOfWeek " +
            "ORDER BY COUNT(d) DESC")
    List<PopularDayOfWeek> findPopularDays();
}
