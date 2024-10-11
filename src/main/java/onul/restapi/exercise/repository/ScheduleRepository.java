package onul.restapi.exercise.repository;

import onul.restapi.exercise.entity.Schedule;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByMemberAndWeekTypeAndDayAndPart(Members member, String weekType, String day, String part);
}
