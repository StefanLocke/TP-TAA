package sample.data.jpa.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "meetings")
@Getter
@Setter
public class Meeting {
    public Meeting() {

    }

    public Meeting(long id) {
        this.id = id;
    }


    public Meeting(User org, LocalDateTime startTime, LocalDateTime endTime) {
        organiser = org;
        this.startTime = startTime;
        this.endTime = endTime;
    }



    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User organiser;

    @OneToOne
    private User student;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
