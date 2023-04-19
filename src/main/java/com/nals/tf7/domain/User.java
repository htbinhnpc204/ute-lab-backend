package com.nals.tf7.domain;

import com.nals.tf7.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.time.Instant;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User
    extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "google_id", length = 50)
    private String googleId;

    @Column(nullable = false)
    private String name;

    @Column(name = "lang_key", nullable = false)
    private String langKey;

    @Column
    private String avatar;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 60)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(length = 500)
    private String address;

    @Enumerated(STRING)
    @Column(length = 20, nullable = false)
    private Gender gender;

    @Column
    private Instant dob;

    @Column(nullable = false)
    private boolean activated;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User(final Long id, final String email,
                final String name, final String phone,
                final String address, final Gender gender,
                final Instant dob, final String avatar, final Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.avatar = avatar;
        this.role = role;
    }
}
