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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long link_id;
    private Long user1;
    private Long user2;
}

