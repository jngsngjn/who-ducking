package hello.entity.request;

import hello.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Request() {
        this.status = RequestStatus.RECEIVED;
    }
}