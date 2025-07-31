package com.nocktice.nocktice.user.entity;

import com.nocktice.nocktice.common.support.SecurityStrategies;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID userId;

    private String username;
    private String password;
    private String email;

    // TODO: 인증 도입 후 Alarm - OneToMany 관계 선언

    @Column(name = "security_strategy")
    private SecurityStrategies securityStrategy;

//    private String provider_id;
//    private String provider_name;
//    private String provider_email;

    @PrePersist
    public void generateUUID() {
        if (this.userId == null) {
            this.userId = UUID.randomUUID();
        }
    }

    public static User of(String username, String password, String email) {
        User user = new User();
        user.username = username;
        user.password = password;
        user.email = email;
        return user;
    }
}
