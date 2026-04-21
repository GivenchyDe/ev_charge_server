package org.example.ev_charge_server.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Admin {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Integer status;
    private LocalDateTime created_at;
}
