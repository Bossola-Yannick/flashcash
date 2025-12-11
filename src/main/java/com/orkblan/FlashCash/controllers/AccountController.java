package com.orkblan.FlashCash.controllers;

import com.orkblan.FlashCash.domain.Account;
import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.repositories.AccountRepository;
import com.orkblan.FlashCash.repositories.TransferRepository;
import com.orkblan.FlashCash.repositories.UserRepository;
import com.orkblan.FlashCash.services.AccountService;
import com.orkblan.FlashCash.services.UserConnected;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class AccountController {


    private final AccountRepository accountRepository;
    private final UserConnected userConnected;
    private final UserRepository userRepository;
    private final TransferRepository transferRepository;
    private final AccountService accountService;

    public AccountController(AccountRepository accountRepository, UserConnected userConnected, UserRepository userRepository, TransferRepository transferRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.userConnected = userConnected;
        this.userRepository = userRepository;
        this.transferRepository = transferRepository;
        this.accountService = accountService;
    }

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
            accountService.addIban(account, user);
//            accountRepository.save(account);
            return "redirect:/user/profil";
        }
        return "account/addiban";
    }
}
