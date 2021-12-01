package sample.data.jpa.dto;

import lombok.Getter;
import lombok.Setter;
import sample.data.jpa.domain.User;

@Getter
@Setter
public class UserDTO {
    public UserDTO(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.id = user.getId();
    }

    long id;
    String name;
    String email;


    User toUser() {
        User user = new User(id);
        user.setName(name);
        user.setEmail(email);
        return user;

    }

}
