package com.employeeApplication.auth.refreshtoken;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final Duration REFRESH_TOKEN_VALIDITY = Duration.ofDays(7);
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken createRefreshToken(String userName) {
        RefreshToken existingToken = getRefreshTokenByName(userName);

        if (existingToken != null) {
            existingToken.setToken(UUID.randomUUID().toString());
            existingToken.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_VALIDITY));
            return refreshTokenRepository.save(existingToken);
        }
        RefreshToken refreshToken = RefreshToken.builder()
                .userName(userName)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(REFRESH_TOKEN_VALIDITY))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken getRefreshTokenByName(String userName) {
        return refreshTokenRepository.getRefreshTokenByName(userName);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    public boolean isExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }
    public void deleteByUsername(String userName) {
        refreshTokenRepository.deleteByUsername(userName);
    }
}
