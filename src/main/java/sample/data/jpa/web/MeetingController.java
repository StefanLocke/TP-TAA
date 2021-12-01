package sample.data.jpa.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sample.data.jpa.domain.Meeting;
import sample.data.jpa.domain.User;
import sample.data.jpa.dto.MeetingDTO;
import sample.data.jpa.service.MeetingDao;
import sample.data.jpa.service.UserDao;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/meeting")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class MeetingController {

    @Autowired
    private MeetingDao meetingDao;


    @RequestMapping("/create")
    @ResponseBody
    public String create(long userId, String startTime, String endTime,HttpServletRequest request){
        logInfo("Create Meeting - started",request);
        String id = "";
        try {
            User org = new User(userId);
            Meeting meeting = new Meeting(org, LocalDateTime.parse(startTime),LocalDateTime.parse(endTime));
            meetingDao.save(meeting);
            id = meeting.getId() +"";
        }catch (Exception ex){
            logWarn("Create Meeting - failed",request);
            return "Could not create meeting." + ex;
        }
        logInfo("Create Meeting - completed",request);
        return "Meeting Created with id = " + id;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(long meetingId,HttpServletRequest request){
        logInfo("Delete Meeting - started",request);
        try {
            Meeting meeting = new Meeting(meetingId);
            meetingDao.delete(meeting);
        }catch (Exception ex) {
            logWarn("Delete Meeting - failed",request);
            return "Error deleting meeting with id = " + meetingId;
        }
        logInfo("Delete Meeting - completed",request);
        return "Deleted meeting";
    }

    @RequestMapping("/join")
    @ResponseBody
    public String join(long meetingId,long userId,HttpServletRequest request){
        logInfo("Join Meeting - started",request);
        try {
           Meeting meeting = meetingDao.findById(meetingId).get();
            User user = new User(userId);
            if (meeting.getOrganiser().getId() == user.getId()) {
                logInfo("Join Meeting - failed",request);
                return "Cant join : The given user is the organiser";
            }
            if (meeting.getStudent() != null) {
                logInfo("Join Meeting - failed",request);
                return "Cant Join : The meeting is already full";
            }
            meeting.setStudent(user);
            meetingDao.save(meeting);

        }catch (Exception ex) {
            logWarn("Join Meeting - failed",request);
            return "Cant Join : Error occured";
        }
        logInfo("Join Meeting - completed",request);
        return "Joined meeting";
    }

    @RequestMapping("/leave")
    @ResponseBody
    public String leave(long meetingId,long userId,HttpServletRequest request){
        logInfo("Leave Meeting - started",request);
        try {
            Meeting meeting = meetingDao.findById(meetingId).get();
            User user = new User(userId);
            if (meeting.getOrganiser().getId() == user.getId()) {
                logInfo("Leave Meeting - failed",request);
                return "Cant leave : The given user is the organiser";
            }
            if (meeting.getStudent().getId() != user.getId()) {
                logInfo("Leave Meeting - failed",request);
                return "Cant leave : The given user is not part of the meeting";
            }
            meeting.setStudent(null);
            meetingDao.save(meeting);
        }catch (Exception ex) {
            logWarn("Leave Meeting - failed",request);
            return "Cant leave : Error occured";
        }
        logInfo("Leave Meeting - completed",request);
        return "Left meeting";
    }

    @RequestMapping("/all")
    @ResponseBody
    public List<MeetingDTO> getAllMeetings(HttpServletRequest request) {
        logInfo("All Meeting - started",request);
        try {
            List list = meetingDao.findAll().stream().map((meeting -> new MeetingDTO(meeting))).collect(Collectors.toList());
            logInfo("All Meeting - completed",request);
            return list;
        } catch (Exception ex) {
            logWarn("All Meeting - failed",request);
            return new LinkedList<>();
        }
    }

    private void logInfo(String message, HttpServletRequest request){
        log.info("["+new Date() +"] " + request.getRemoteAddr() + " - " + message);
    }
    private void logWarn(String message, HttpServletRequest request){
        log.warn("["+new Date() +"] " + request.getRemoteAddr() + " - " + message);
    }
}
