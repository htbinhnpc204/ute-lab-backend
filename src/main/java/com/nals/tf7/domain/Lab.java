package com.nals.tf7.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "phong_may")
public class Lab
    extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ma_phong")
    private Long id;

    @Column(name = "ten_phong_may", nullable = false, unique = true)
    private String name;

    @Column(name = "mo_ta", length = 500)
    private String description;

    @Column(name = "hinh_anh")
    private String avatar;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_nhan_vien", nullable = false)
    private User manager;

    @OneToMany(mappedBy = "lab")
    private List<Computer> computers;

    public Lab(final Long id, final String name,
               final String avatar, final String description) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.description = description;
    }
}
