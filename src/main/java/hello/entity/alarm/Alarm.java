package hello.entity.alarm;

import hello.entity.animation.Animation;
import hello.entity.board.Board;
import hello.entity.request.Request;
import hello.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alarm_type")
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "animation_id")
    private Animation animation;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
}