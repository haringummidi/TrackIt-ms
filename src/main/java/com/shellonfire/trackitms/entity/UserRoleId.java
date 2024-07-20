package com.shellonfire.trackitms.entity;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserRoleId implements Serializable {
    private static final long serialVersionUID = -3698374958705922716L;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;

    public UserRoleId(){}

    public UserRoleId(Long id, Long id1) {
        this.userId = id;
        this.roleId = id1;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}