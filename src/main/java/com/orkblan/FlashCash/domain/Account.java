package com.orkblan.FlashCash.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long account_id;
    private Double solde;
    private String iban;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
