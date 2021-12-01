package sample.data.jpa.dto;


import lombok.Getter;
import lombok.Setter;
import sample.data.jpa.domain.Meeting;
import sample.data.jpa.domain.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeetingDTO {
    public MeetingDTO(Meeting meeting) {
        this.id = meeting.getId();
        this.organiser = meeting.getOrganiser();
        this.student = meeting.getStudent();
        this.startTime = meeting.getStartTime();
        this.endTime = meeting.getEndTime();
    }

    private Long id;

    private User organiser;

    private User student;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    Meeting toMeeting(){
        Meeting meeting = new Meeting(id);
        meeting.setOrganiser(organiser);
        meeting.setStudent(student);
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        return meeting;
    }
}
