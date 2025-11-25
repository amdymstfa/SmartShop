package com.microtech.smartshop.entity ;

import com.microtech.smartshop.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id ;

    @Column(nullable = false)
    private String name ;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password ;

    @Column(nullable = false)
    private String address ;

    @Column(nullable = false)
    private int phone ;

    @Column(nullable = false)
    private boolean status ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role ;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt ;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updateAt ;
}