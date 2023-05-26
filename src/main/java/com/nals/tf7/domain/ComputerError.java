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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "may_tinh_loi")
public class ComputerError
    extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ma_loi")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ma_may", nullable = false)
    private Computer computer;

    @Column(name = "mo_ta", nullable = false)
    private String description;

    @Column(name = "trang_thai", nullable = false)
    private boolean fixed;
}
