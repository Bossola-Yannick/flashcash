package com.orkblan.FlashCash.controllers;

import com.orkblan.FlashCash.domain.Account;
import com.orkblan.FlashCash.domain.Transfer;
import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.repositories.AccountRepository;
import com.orkblan.FlashCash.repositories.TransferRepository;
import com.orkblan.FlashCash.repositories.UserRepository;
import com.orkblan.FlashCash.services.UserConnected;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Optional;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserConnected userConnected;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransferRepository transferRepository;


    // TODO   ajout IBAN
    @RequestMapping("account/addiban")
    public String addIban(Model model, Authentication authentication) {

        model.addAttribute("user", userConnected.userConnected(authentication));
        model.addAttribute("account", new Account());

        return "account/addiban";
    }

    @RequestMapping("iban/validate")
    public String validateIban(@Validated Account account, BindingResult result, Authentication authentication) {
        if (!result.hasErrors()) {
            User user = userConnected.userConnected(authentication);
            account.setSolde(0.0);
            account.setUser(user);
            accountRepository.save(account);
            return "redirect:/user/profil";
        }
        return "account/addiban";
    }



}
