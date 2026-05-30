package com.ulutman.model.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;

@Entity
@Table(name = "AdVertising")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdVersiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String imagePath;

    private boolean active;

    private String paymentReceipt;

    private String bank;

    private LocalDateTime lastBoostedTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private LocalDateTime lastBoostedAt;

    private LocalDateTime nextBoostTime;

    private String timeToNextBoost;

    public AdVersiting(String imagePath, boolean active, String paymentReceipt, String bank, User user) {
        this.imagePath = imagePath;
        this.active = active;
        this.paymentReceipt = paymentReceipt;
        this.bank = bank;
        this.user = user;
    }
}
