package org.example.security;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.hibernate.StudentHibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
public class UserPrincipal implements UserDetails {


    private StudentHibernate studentHibernate;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    private List<GrantedAuthority> authorities=new ArrayList<>();

    public UserPrincipal(StudentHibernate userByName) {
        this.studentHibernate=userByName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return studentHibernate.getEncrypted_password();
    }

    @Override
    public String getUsername() {
        return studentHibernate.getUsername();
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
