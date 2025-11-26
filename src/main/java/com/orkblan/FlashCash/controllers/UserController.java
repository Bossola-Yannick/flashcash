package com.orkblan.FlashCash.controllers;

import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.repositories.UserRepository;
import com.orkblan.FlashCash.services.PasswordCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private PasswordCrypt passwordCrypt;

    @RequestMapping("user/profil")
    public String profil(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        User user =userRepository.findByEmail(email).get();
        model.addAttribute("user", user);
        return "user/profil";
    }

    @GetMapping("/user/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Validated User user, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            user.setPassword(passwordCrypt.passwordCrypted(user.getPassword()));;
            userRepository.save(user);
            model.addAttribute("users", userRepository.findAll());
            return "redirect:/login";
        }
        return "user/add";
    }

    @GetMapping("/user/update")
    public String userUpdate(Model model) {
        return "user/update";
    }
}
