package com.nals.tf7.domain;

import com.nals.tf7.enums.GroupUserRole;
import com.nals.tf7.enums.GroupUserType;
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
@Table(name = "group_users")
public class GroupUser
    extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_type", nullable = false)
    private GroupUserType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private GroupUserRole groupUserRole;

    public GroupUser(final Long groupId, final Long userId, final GroupUserType type,
                     final GroupUserRole groupUserRole) {
        this.groupId = groupId;
        this.userId = userId;
        this.type = type;
        this.groupUserRole = groupUserRole;
    }
}
