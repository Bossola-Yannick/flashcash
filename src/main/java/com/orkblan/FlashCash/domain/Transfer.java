package com.orkblan.FlashCash.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transfer_id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;
    private String type;
    private Double value;
    private Date date;
    // émetteur
    private String issuer; //sera mis en hidden et auto rempli dans le formulaire par l'email de la personne qui fait le dépot
    // récepteur
    private String recipient;
}
