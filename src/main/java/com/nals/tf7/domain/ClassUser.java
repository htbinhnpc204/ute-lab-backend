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
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lop_nguoi_dung")
public class ClassUser
    extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "ma_lop", nullable = false)
    private Long classId;

    @Column(name = "ma_nguoi_dung", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ClassUserRole classUserRole;

    public ClassUser(final Long classId, final Long userId,
                     final ClassUserRole classUserRole) {
        this.classId = classId;
        this.userId = userId;
        this.classUserRole = classUserRole;
    }
}
