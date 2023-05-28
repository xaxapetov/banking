package ru.iql.banking.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.iql.banking.dtos.auth.AuthRequest;
import ru.iql.banking.dtos.auth.AuthResponse;
import ru.iql.banking.models.BankingUser;
import ru.iql.banking.services.UserService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public AuthResponse authenticate(AuthRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());

        authenticationManager.authenticate(authToken);
        BankingUser bankingUser = userService.getUserByEmailOrPhone(request.getUsername());
        String jwtToken = jwtServiceImpl.generateToken(bankingUser.getId().toString());
        return AuthResponse.builder() .accessToken(jwtToken).build();
    }
}
