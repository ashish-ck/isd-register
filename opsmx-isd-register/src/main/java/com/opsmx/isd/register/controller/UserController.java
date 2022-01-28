package com.opsmx.isd.register.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.dto.OutputMessage;
import com.opsmx.isd.register.entities.User;
import com.opsmx.isd.register.repositories.UserRepository;
import com.opsmx.isd.register.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.opsmx.isd.register.dto.DatasourceResponseModel;
import com.opsmx.isd.register.dto.Message;
import com.opsmx.isd.register.service.AccountSetupService;
import com.opsmx.isd.register.service.SendMessage;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private SendMessage sendMessage;

    @Autowired
    private AccountSetupService accountSetupService;

    private final Long TIMEOUT_IN_SECONDS = 120L;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value="/index", method = RequestMethod.GET)
    public String showUserList(Model model, RedirectAttributes redirectAttrs, HttpServletRequest request){
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
        redirectAttrs.addFlashAttribute("businessEmail", user.getBusinessEmail());
        redirectAttrs.addFlashAttribute("contactNumber", user.getContactNumber());
        userRepository.save(Util.toUser(user));
        AtomicReference<Boolean> isSpinnakerSetupComplete = new AtomicReference<>(false);
        AtomicReference<DatasourceResponseModel> atomicReference = new AtomicReference<>();
        CompletableFuture.supplyAsync(() -> {
            atomicReference.set(accountSetupService.setup(user));
            return atomicReference;
        }).thenRun(() -> {
            DatasourceResponseModel responseModel = atomicReference.get();
            if(responseModel != null && responseModel.getEventProcessed()){
                log.info("Building spinnaker setup for user {} ", user.getBusinessEmail());
                isSpinnakerSetupComplete.set(true);
                // send message to redirect to login page.
                 sendMessage.sendMessageObject(new Message(user.getBusinessEmail(), "success"));
            }else {
                log.info("Error building spinnaker ");
                // send message to redirect to error page.
                 sendMessage.sendMessageObject(new Message(user.getBusinessEmail(), "failure"));
            }
        }).orTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS).thenRun(() -> {
             sendMessage.sendMessageObject(new Message(user.getBusinessEmail(), "failure"));
        });
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

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(final Message message) throws Exception {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }
}
