package com.project.security.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "user")
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "address")
    private String address;
    @Column(name = "role")
    private String role;
    @Column(name = "phone_no")
    private String phoneNo;
    @Transient
    private List<? extends GrantedAuthority> authorities;

    @Builder
    public User(String userId, String name, String password, String address, String role, String phoneNo) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.address = address;
        this.role = role;
        this.phoneNo = phoneNo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
