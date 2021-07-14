package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String blogMain(Model model) {
        Iterable<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin/home";
    }

    @GetMapping("/adduser")
    public String addUser() {
        return "addUser";
    }

    @PostMapping("/adduser")
    public String addUserPost(@ModelAttribute("user") User userForm, @RequestParam(name = "role", required = false) String role) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userService.getRoleById(2L));
        if ((role != null) && (role.equals("admin"))) {
            roleSet.add(userService.getRoleById(1L));
        }
        userForm.setRoles(roleSet);
        userService.saveUser(new User(userForm.getFirstName(),userForm.getLastName(),userForm.getAge(),
                userForm.getEmail(), userForm.getPassword(), userForm.getRoles()));
        return "redirect:/admin/home";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editPage(@PathVariable("id") long id, ModelAndView modelAndView) {
        User userById = userService.getUserById(id);
        modelAndView.setViewName("editUser");
        modelAndView.addObject("user", userById);
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public String updateUserPost(@ModelAttribute("user") User userForm,
                                 @RequestParam(name = "role", required = false) String role) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userService.getRoleById(2L));
            if ((role != null) && (role.equals("admin"))) {
            roleSet.add(userService.getRoleById(1L));
        }
        userForm.setRoles(roleSet);
        userService.editUser(userForm);
        return "redirect:/admin/home";
    }
}
