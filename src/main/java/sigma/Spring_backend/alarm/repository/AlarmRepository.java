package sigma.Spring_backend.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.alarm.entity.Alarm;

import java.util.List;
import java.util.Optional;


@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByEmail(String email);
}
