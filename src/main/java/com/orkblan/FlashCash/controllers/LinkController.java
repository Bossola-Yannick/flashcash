package com.orkblan.FlashCash.controllers;


import com.orkblan.FlashCash.domain.Link;
import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.dto.UserLinkDto;
import com.orkblan.FlashCash.repositories.LinkRepository;
import com.orkblan.FlashCash.repositories.UserRepository;
import com.orkblan.FlashCash.services.UserConnected;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class LinkController {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private UserConnected userConnected;
    @Autowired
    private UserRepository userRepository;

    // TODO:    ajout link
    @RequestMapping("link/addnew")
    public String addLink(Model model) {
        model.addAttribute("user", new UserLinkDto());
        return "link/addnew";
    }

    @RequestMapping("link/addNewLink")
    public String addNewLink(@Validated User user, BindingResult result, Authentication authentication){
        if (!result.hasErrors()){
            User userAddLink = userConnected.userConnected(authentication);
            Link link = new Link();
            link.setUser1(userAddLink.getUser_id());
            Long userIdLink = userRepository.findByEmail(user.getEmail()).get().getUser_id();
            link.setUser2(userIdLink);
            linkRepository.save(link);
            return "redirect:/user/profil";
        }
        return "link/addnew";
    }


// TODO:   delete link
}
