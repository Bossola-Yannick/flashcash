package com.orkblan.FlashCash.services;

import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oauthUser = super.loadUser(userRequest);

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
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

        if (email == null) {
            // Pour Facebook/Github Si pas de transmission email (autorisation non donnée) on prend l"id" (unique) pour remplir le champ "email" pour passer le "nullable=false""
            email = "fb_" + id;
        }

        // Vérification si utilisateur dans la base pour éviter les doublons d'inscription
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // inscription automatique lors de la 1ere connection avec un lien OAuth2
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword("OAUTH2_USER"); // valeur neutre, pas utilisée

            userRepository.save(user);
        }

        return oauthUser;
    }
}