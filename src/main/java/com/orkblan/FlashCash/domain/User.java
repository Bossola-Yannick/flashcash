package com.orkblan.FlashCash.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;
    private String firstname;
    private String lastname;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @ManyToMany
    @JoinTable(name = "link", joinColumns = @JoinColumn(name = "user1"), inverseJoinColumns = @JoinColumn(name = "user2"))
    private Set<User> linkedUser = new HashSet<>();
}
