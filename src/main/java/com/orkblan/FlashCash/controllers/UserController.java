package com.orkblan.FlashCash.controllers;

import com.orkblan.FlashCash.domain.Account;
import com.orkblan.FlashCash.domain.Transfer;
import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.dto.UserLinkDto;
import com.orkblan.FlashCash.repositories.AccountRepository;
import com.orkblan.FlashCash.repositories.LinkRepository;
import com.orkblan.FlashCash.repositories.TransferRepository;
import com.orkblan.FlashCash.repositories.UserRepository;
import com.orkblan.FlashCash.services.AccountService;
import com.orkblan.FlashCash.services.PasswordCrypt;
import com.orkblan.FlashCash.services.UserConnected;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {


    private final UserRepository userRepository;
    private final PasswordCrypt passwordCrypt;
    private final UserConnected userConnected;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final LinkRepository linkRepository;
    private final AccountService accountService;

    public UserController(UserRepository userRepository, PasswordCrypt passwordCrypt, UserConnected userConnected, AccountRepository accountRepository, TransferRepository transferRepository, LinkRepository linkRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.passwordCrypt = passwordCrypt;
        this.userConnected = userConnected;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.linkRepository = linkRepository;
        this.accountService = accountService;
    }

    @RequestMapping("user/profil")
    public String profil(Authentication authentication, Model model) {
        User user = userConnected.userConnected(authentication);
        model.addAttribute("user", user);
        Account account = accountRepository.findAccountByUserid(user.getUser_id());
        model.addAttribute("account", account);
        List<Transfer> transfer = transferRepository.findTransferByAccount(account);
        model.addAttribute("transfers", transfer);
        List<UserLinkDto> userLinks = linkRepository.findLinkByUser1(user.getUser_id());
        model.addAttribute("userLinks", userLinks);
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
            user.setPassword(passwordCrypt.passwordCrypted(user.getPassword()));
            userRepository.save(user);
            accountService.initialCount(user);
            return "redirect:/login";
        }
        return "user/add";
    }

    @GetMapping("/user/update")
    public String userUpdate(Model model) {
        return "user/update";
    }
}
