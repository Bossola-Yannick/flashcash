package com.orkblan.FlashCash.controllers;

import com.orkblan.FlashCash.domain.Account;
import com.orkblan.FlashCash.domain.Transfer;
import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.dto.UserLinkDto;
import com.orkblan.FlashCash.repositories.AccountRepository;
import com.orkblan.FlashCash.repositories.LinkRepository;
import com.orkblan.FlashCash.repositories.TransferRepository;
import com.orkblan.FlashCash.repositories.UserRepository;
import com.orkblan.FlashCash.services.PasswordCrypt;
import com.orkblan.FlashCash.services.UserConnected;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordCrypt passwordCrypt;

    @Autowired
    private UserConnected userConnected;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private LinkRepository linkRepository;

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
