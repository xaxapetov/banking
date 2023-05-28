package ru.iql.banking.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.iql.banking.models.BankingUser;
import ru.iql.banking.services.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.iql.banking.utils.Constants.USER_ROLE;

@Component("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceForBankingUser implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            BankingUser bankingUser = userService.getUserByEmailOrPhone(username);
        List<GrantedAuthority> authorities = buildUserAuthority();
        return buildUserForAuthentication(username, bankingUser, authorities);
    }

    private User buildUserForAuthentication(String username, BankingUser user, List<GrantedAuthority> authorities) {
        return new User(username, user.getPassword(), true, true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority() {
        Set<GrantedAuthority> setAuths = new HashSet<>();
            setAuths.add(new SimpleGrantedAuthority("ROLE_" + USER_ROLE));

        return new ArrayList<>(setAuths);
    }
}
