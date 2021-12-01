package sample.data.jpa.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import sample.data.jpa.domain.Meeting;
import sample.data.jpa.domain.User;
import sample.data.jpa.dto.UserDTO;
import sample.data.jpa.service.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserController {
  /**
   * GET /create  --> Create a new user and save it in the database.
   */
  @RequestMapping("/create")
  @ResponseBody
  public String create(String email, String name, HttpServletRequest request) {
    logInfo("Create User - started",request);
    String userId = "";
    try {
      User user = new User(email, name);
      System.out.println(email);
      if (email == null || email == "") {
        user.setEmail("No e-mail");
      }
      if (name == null || name == "") {
        user.setName("No name");
      }
      userDao.save(user);
      userId = String.valueOf(user.getId());
    }
    catch (Exception ex) {
      logWarn("Create User - failed",request);
      return "Error creating the user: " + ex.toString();
    }
    logInfo("Create User - completed",request);
    return "User succesfully created with id = " + userId;
  }
  
  /**
   * GET /delete  --> Delete the user having the passed id.
   */
  @RequestMapping("/delete")
  @ResponseBody
  public String delete(long id, HttpServletRequest request) {
    logInfo("Delete User - started",request);
    try {
      User user = new User(id);
      userDao.delete(user);
    }
    catch (Exception ex) {
      logWarn("Delete User - failed",request);
      return "Error deleting the user:" + ex.toString();
    }
    logInfo("Delete User - completed",request);
    return "User succesfully deleted!";
  }
  
  /**
   * GET /get-by-email  --> Return the id for the user having the passed
   * email.
   */
  @RequestMapping("/get-by-email/{email}")
  @ResponseBody
  public String getByEmail(@PathVariable("email") String email, HttpServletRequest request) {
    logInfo("Get by email User - started",request);
    String userId = "";
    try {
      User user = userDao.findByEmail(email);
      userId = String.valueOf(user.getId());
    }
    catch (Exception ex) {
      logWarn("Get by email User - failed",request);
      return "User not found";
    }
    logInfo("Get by email User - completed",request);
    return "The user id is: " + userId;
  }

  /**
   * GET /get-by-email  --> Return the id for the user having the passed
   * email.
   */
  @RequestMapping("/get")
  @ResponseBody
  public User getById(long id, HttpServletRequest request) {
    logInfo("Get by id User - started",request);
    User user = null;
    try {
      user = userDao.findById(id).get();
    }catch (Exception ex ) {
      logWarn("Get by id User User - failed",request);
      return user;
    }
    logInfo("Get by id User - completed",request);
    return user;
  }
  
  /**
   * GET /update  --> Update the email and the name for the user in the 
   * database having the passed id.
   */
  @RequestMapping("/update")
  @ResponseBody
  public String updateUser(long id, String email, String name, HttpServletRequest request) {
    logInfo("Update User - started",request);
    try {
      User user = userDao.findById(id).get();
      user.setEmail(email);
      user.setName(name);
      userDao.save(user);
    }
    catch (Exception ex) {
      logWarn("Update User - failed",request);
      return "Error updating the user: " + ex.toString();
    }
    logInfo("Update User - completed",request);
    return "User succesfully updated!";
  }

  @RequestMapping("/all")
  @ResponseBody
  public List<UserDTO> getAllUsers(HttpServletRequest request) {
    logInfo("All User - started",request);
    try {
      List<UserDTO> list = userDao.findAll().stream().map((user -> new UserDTO(user))).collect(Collectors.toList());
      logInfo("All User - completed",request);
      return list;
    }catch (Exception ex) {
      logWarn("All User - failed",request);
      return new LinkedList<>();
    }
  }



  // Private fields

  @Autowired
  private UserDao userDao;

  private void logInfo(String message, HttpServletRequest request){
    log.info("["+new Date() +"] " + request.getRemoteAddr() + " - " + message);
  }
  private void logWarn(String message, HttpServletRequest request){
    log.warn("["+new Date() +"] " + request.getRemoteAddr() + " - " + message);
  }
  
}