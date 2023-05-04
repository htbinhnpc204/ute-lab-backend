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
@Table(name = "nguoi_dung")
public class User
    extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ma_nguoi_dung")
    private Long id;

    @Column(name = "ma_sinh_vien", length = 13, unique = true)
    private String studentId;

    @Column(name = "ten_nguoi_dung")
    private String name;

    @Column(name = "ngon_ngu", nullable = false, columnDefinition = "default 'EN'")
    private String langKey;

    @Column(name = "hinh_anh")
    private String avatar;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "mat_khau", nullable = false, length = 60)
    private String password;

    @Column(name = "so_dien_thoai", length = 20)
    private String phone;

    @Column(name = "dia_chi", length = 500)
    private String address;

    @Enumerated(STRING)
    @Column(name = "gioi_tinh", length = 20, nullable = false, columnDefinition = "default 'NAM'")
    private Gender gender;

    @Column(name = "ngay_sinh")
    private Instant dob;

    @Column(name = "trang_thai", nullable = false, columnDefinition = "default 'true'")
    private boolean activated;

    @OneToOne
    @JoinColumn(name = "ma_phan_quyen", nullable = false)
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
