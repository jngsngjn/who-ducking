package hello.repository.db;

import hello.entity.alarm.Alarm;
import hello.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("select count(a) from Alarm a where a.user = :user")
    int findCountByUser(@Param("user") User user);

    List<Alarm> findAllByUser(User user);
}