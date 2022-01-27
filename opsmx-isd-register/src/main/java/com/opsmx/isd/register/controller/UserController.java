package com.opsmx.isd.register.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.entities.User;
import com.opsmx.isd.register.repositories.UserRepository;
import com.opsmx.isd.register.service.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Slf4j
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    private GreetingService greetingService;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value="/index", method = RequestMethod.GET)
    public String showUserList(Model model, RedirectAttributes redirectAttrs, HttpServletRequest request){
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("email", (String)model.asMap().get("email"));
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }

    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public RedirectView save(@ModelAttribute("user") DatasourceRequestModel user, RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("firstName", user.getFirstName());
        redirectAttrs.addFlashAttribute("lastName", user.getLastName());
        redirectAttrs.addFlashAttribute("companyName", user.getCompanyName());
        redirectAttrs.addFlashAttribute("email", user.getEmail());
        redirectAttrs.addFlashAttribute("phone", user.getPhone());
        userRepository.save(toUser(user));
        greetingService.sendMessages();
        RedirectView redirectView = new RedirectView();
        redirectView.setContextRelative(true);
        redirectView.setUrl("/index");
        return redirectView;
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }
        userRepository.save(user);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/index";
    }

    private User toUser(DatasourceRequestModel requestModel){
        User user = new User();
        user.setEmail(requestModel.getEmail());
        user.setPhone(requestModel.getEmail());
        user.setFirstName(requestModel.getFirstName());
        user.setLastName(requestModel.getLastName());
        user.setCompanyName(requestModel.getCompanyName());
        return user;
    }
}
