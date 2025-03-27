package com.ptip.auth.repository;

import com.ptip.auth.entity.RefreshTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    RefreshTokenEntity findByUserId(String refreshToken);

    Boolean existsByToken(String refreshToken);

    @Transactional
    void deleteByToken(String refreshToken);

}
