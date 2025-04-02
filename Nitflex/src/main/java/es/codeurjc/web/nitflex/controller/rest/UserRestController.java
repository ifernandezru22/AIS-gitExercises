package es.codeurjc.web.nitflex.controller.rest;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.web.nitflex.dto.user.UserDTO;
import es.codeurjc.web.nitflex.dto.user.UserMapper;
import es.codeurjc.web.nitflex.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private UserService userService;

    private UserMapper userMapper;

    public UserRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/")
    public Collection<UserDTO> getUsers() {
        return userMapper.toDTOs(userService.getUsers());
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable long id) {
        return userMapper.toDTO(userService.getUser(id));
    }
    
}