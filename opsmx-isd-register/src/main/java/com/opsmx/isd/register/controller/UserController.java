package com.opsmx.isd.register.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.dto.DatasourceResponseModel;
import com.opsmx.isd.register.dto.Message;
import com.opsmx.isd.register.dto.OutputMessage;
import com.opsmx.isd.register.entities.User;
import com.opsmx.isd.register.repositories.UserRepository;
import com.opsmx.isd.register.service.AccountSetupService;
import com.opsmx.isd.register.service.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private AccountSetupService accountSetupService;

    @Autowired
    private SendMessage sendMessage;

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
    public String showSignUpForm(DatasourceRequestModel user) {
        return "add-user";
    }

    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public RedirectView save(@ModelAttribute("user") DatasourceRequestModel user, RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("user", user.getBusinessEmail());
        redirectAttrs.addFlashAttribute("firstName", user.getFirstName());
        redirectAttrs.addFlashAttribute("lastName", user.getLastName());
        redirectAttrs.addFlashAttribute("companyName", user.getCompanyName());
        redirectAttrs.addFlashAttribute("email", user.getBusinessEmail());
        redirectAttrs.addFlashAttribute("phone", user.getContactNumber());

        userRepository.save(toUser(user));

        CompletableFuture<DatasourceResponseModel> completableFuture =
                CompletableFuture.supplyAsync(() -> accountSetupService.setup(user));
        while (!completableFuture.isDone()) {
            log.info("Building spinnaker setup for user {} ", user.getBusinessEmail());
        }
        Boolean isSpinnakerSetupComplete = false;
        DatasourceResponseModel datasourceResponseModel = null;
        try {
            datasourceResponseModel = completableFuture.get();
            if(datasourceResponseModel.getEventProcessed() == true){
                isSpinnakerSetupComplete = true;
            }
        } catch (InterruptedException e) {
            log.info("Error building spinnaker {} " , e.getMessage());
        } catch (ExecutionException e) {
            log.info("Error building spinnaker {} " , e.getMessage());
        }

        if(isSpinnakerSetupComplete == true){
            // send message to redirect to login page.
        } else {
            // send message to redirect to error page.
        }
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
//        user.setUser(requestModel.getUser());
        user.setEmail(requestModel.getBusinessEmail());
        user.setPhone(requestModel.getBusinessEmail());
        user.setFirstName(requestModel.getFirstName());
        user.setLastName(requestModel.getLastName());
        user.setCompanyName(requestModel.getCompanyName());
        return user;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(final Message message) throws Exception {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }
}
