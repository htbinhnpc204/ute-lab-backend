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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quyen")
public class Role
    extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_quyen")
    private Long id;

    @Column(name = "ten_quyen", length = 50, nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(name = "chi_tiet_quyen",
        joinColumns = @JoinColumn(name = "ma_quyen"),
        inverseJoinColumns = @JoinColumn(name = "ma_hanh_dong"))
    private List<Permission> permissions;

    public Role(final String name) {
        this.name = name;
    }
}
