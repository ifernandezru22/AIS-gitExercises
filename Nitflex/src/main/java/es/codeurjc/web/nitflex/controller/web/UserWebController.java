package es.codeurjc.web.nitflex.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.codeurjc.web.nitflex.service.UserComponent;
import es.codeurjc.web.nitflex.service.UserService;


@Controller
public class UserWebController {

    private UserComponent userComponent;

    private UserService userService;

    public UserWebController(UserComponent userComponent, UserService userService) {
        this.userComponent = userComponent;
        this.userService = userService;
    }

    @GetMapping("/users")
    public String index(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "users";
    }

    @PostMapping("/users/{id}/delete")
	public String removeUser(@PathVariable long id) {
        userService.deleteUser(id);
        return "redirect:/users";
	}
    

    @GetMapping("/users/favorites")
    public String favorites(Model model) {
        model.addAttribute("films", userComponent.getUser().getFavoriteFilms());
        return "favorites";
    }
    
}
