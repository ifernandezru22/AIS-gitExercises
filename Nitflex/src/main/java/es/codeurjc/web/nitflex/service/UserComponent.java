package es.codeurjc.web.nitflex.service;

import org.springframework.stereotype.Component;

import es.codeurjc.web.nitflex.model.User;
import es.codeurjc.web.nitflex.repository.UserRepository;

@Component
public class UserComponent {
    
    UserRepository userRepository;

    public UserComponent(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Returns always the same user for simplicity before adding authentication
     * @return User
     */
    public User getUser() {
        return userRepository.findAll().get(0);
    }
}
