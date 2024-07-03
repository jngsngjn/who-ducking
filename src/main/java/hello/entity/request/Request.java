package hello.entity.request;

import hello.entity.alarm.Alarm;
import hello.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String response;

    @CreationTimestamp
    @Column(name = "write_date")
    private LocalDate writeDate;

    @Column(name = "response_date")
    private LocalDate responseDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alarm> alarms = new ArrayList<>();

    public Request() {
        this.status = RequestStatus.RECEIVED;
    }
}