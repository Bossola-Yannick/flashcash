package com.orkblan.FlashCash.dto;

import lombok.Data;

@Data
public class UserLinkDto {
    private Long id;
    private String email;
    private String name;

    public UserLinkDto() {
    };

    public UserLinkDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
