package com.ordermanagement.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@Entity
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "USER_INFO")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String emailId;
    private String password;
}
