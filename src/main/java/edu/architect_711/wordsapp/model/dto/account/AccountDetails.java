package edu.architect_711.wordsapp.model.dto.account;

import edu.architect_711.wordsapp.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * This object <b>MUST</b> be placed in the {@link org.springframework.security.core.Authentication} object
 * as the principal, because it saves from the extra SQL-request to the database, for example to get the
 * authenticated user id.
 * <p>
 * This is very helpful. You don't need to search for the authenticated user entity in the database,
 * because it's already in the Authentication, since a user went through the {@link org.springframework.security.web.SecurityFilterChain}
 * and loaded from the database by the {@link edu.architect_711.wordsapp.service.account.AccountDetailsService}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDetails implements UserDetails {
    private Account account;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public AccountDetails(Account account) {
        this.account = account;
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.authorities = List.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
