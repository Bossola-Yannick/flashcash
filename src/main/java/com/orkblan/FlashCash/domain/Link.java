package com.orkblan.FlashCash.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long link_id;
    private Long user1;
    private Long user2;
}
// requête sql pour lister les amis =>
//SELECT u.email AS u1_mail, u.firstname AS u1_first, u2.email AS u2_mail, u2.firstname AS u2_first FROM user u
//JOIN link l ON l.user1 = u.user_id
//JOIN user u2 ON u2.user_id = l.user2
//WHERE u.user_id = 1
