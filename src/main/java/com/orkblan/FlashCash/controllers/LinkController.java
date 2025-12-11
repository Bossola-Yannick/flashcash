package com.orkblan.FlashCash.controllers;


import com.orkblan.FlashCash.domain.Link;
import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.dto.UserLinkDto;
import com.orkblan.FlashCash.repositories.LinkRepository;
import com.orkblan.FlashCash.repositories.UserRepository;
import com.orkblan.FlashCash.services.UserConnected;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@Controller
public class LinkController {

    private final LinkRepository linkRepository;
    private final UserConnected userConnected;
    private final UserRepository userRepository;

    public LinkController(LinkRepository linkRepository, UserConnected userConnected, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userConnected = userConnected;
        this.userRepository = userRepository;
    }

    // TODO:    ajout link
    @RequestMapping("link/addnew")
    public String addLink(Model model) {
        model.addAttribute("user", new UserLinkDto());
        return "link/addnew";
    }

    @RequestMapping("link/addNewLink")
    public String addNewLink(@Validated User user, BindingResult result, Authentication authentication,Model model) {
        if (!result.hasErrors()) {
            User userAddLink = userConnected.userConnected(authentication);

            // vérif if user exist
            Optional<User> userExist = userRepository.findByEmail(user.getEmail());
            if (userExist.isEmpty()) {
                model.addAttribute("errorMessage", "User Tag not found !");
                return "link/addnew";
            }
            Long userIdLink = userExist.get().getUser_id();
            Link link = new Link();
            link.setUser1(userAddLink.getUser_id());
            link.setUser2(userIdLink);
            linkRepository.save(link);
            return "redirect:/user/profil";
        }
        return "link/addnew";
    }


// TODO:   delete link
}
