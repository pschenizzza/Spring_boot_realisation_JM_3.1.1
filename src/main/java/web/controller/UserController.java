package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user")
    public ModelAndView user(ModelAndView modelAndView) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("myPage");
        return modelAndView;
    }

    @GetMapping("/registration")
    public String pageNewUser(){
        return "regPage";
    }

    @PostMapping("/registration")
    public String addNewUser(@ModelAttribute("user") User userForm) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userService.getRoleById(2L));
        userForm.setRoles(roleSet);
        userService.saveUser(new User(userForm.getFirstName(),userForm.getLastName(),userForm.getAge(),
                userForm.getEmail(), userForm.getPassword(), userForm.getRoles()));
        return "redirect:/login";
    }
}
