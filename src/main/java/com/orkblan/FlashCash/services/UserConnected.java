package com.orkblan.FlashCash.services;

import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserConnected {
    @Autowired
    private UserRepository userRepository;

    public User userConnected(Authentication authentication) {
        if (authentication == null) {
            return new User();
        }
        Object principal = authentication.getPrincipal();
        String email = null;

        if (principal instanceof UserDetails user) {
            email = user.getUsername();
        } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauthUser) {
            email = (String) oauthUser.getAttributes().get("email");

            if (email == null) {
                Object idObj = oauthUser.getAttribute("id");
                String id;
                if (idObj instanceof Integer) {
                    id = String.valueOf(idObj);
                } else if (idObj instanceof String) {
                    id = (String) idObj;
                } else if (idObj != null) {
                    id = idObj.toString();
                } else {
                    id = null;
                }
                email = "fb_" + id;
            }
        }
        if (email == null) {
            throw new IllegalStateException("Email Not Found");
        }
        String finalEmail = email;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User Not Found in Bdd : " + finalEmail));
        return user;
    }
}
