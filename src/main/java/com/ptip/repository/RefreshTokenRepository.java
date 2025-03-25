package com.ptip.repository;

import com.ptip.entity.RefreshTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Boolean existsByToken(String refreshToken);

    @Transactional
    void deleteByToken(String refreshToken);

}
