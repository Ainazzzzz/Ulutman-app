package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ulutman.model.enums.MailingStatus;
import com.ulutman.model.enums.MailingType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mailing")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Mailing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private MailingType mailingType;

    @Enumerated(EnumType.STRING)
    private MailingStatus mailingStatus;

    private String message;

    private String image;

    @Column(name = "promotion_start_date")
    private LocalDate promotionStartDate;

    @Column(name = "promotion_end_date")
    private LocalDate promotionEndDate;

    private LocalDate createDate;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "mailing_user",
            joinColumns = @JoinColumn(name = "mailing_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> recipients = new ArrayList<>();
}
