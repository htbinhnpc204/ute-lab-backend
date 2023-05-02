package com.nals.tf7.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lich_su_dung")
public class Schedule
    extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_lich_su_dung")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_phong", nullable = false)
    private Lab lab;

    @ManyToOne
    @JoinColumn(name = "ma_lop")
    private ClassEntity classEntity;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private User user;

    @Column(name = "thoi_gian_bat_dau", nullable = false)
    private Instant timeStart;

    @Column(name = "thoi_gian_su_dung", nullable = false)
    private Integer timeUse;
}
