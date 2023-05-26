package com.nals.tf7.domain;

import com.nals.tf7.enums.ClassUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chi_tiet_lop")
public class ClassUser
    extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ma_chi_tiet_lop")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ma_lop", nullable = false)
    private ClassEntity classEntity;

    @OneToOne
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "chuc_danh", nullable = false)
    private ClassUserRole role;
}
