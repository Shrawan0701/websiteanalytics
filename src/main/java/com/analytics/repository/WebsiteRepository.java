package com.analytics.repository;

import com.analytics.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long> {

    List<Website> findByUserId(Long userId);

    Optional<Website> findByWebsiteId(UUID websiteId);

    Optional<Website> findByWebsiteIdAndUserId(UUID websiteId, Long userId);

    @Query("SELECT w FROM Website w WHERE w.user.id = :userId AND w.websiteId = :websiteId")
    Optional<Website> findByUserIdAndWebsiteId(@Param("userId") Long userId, @Param("websiteId") UUID websiteId);

    @Query("SELECT COUNT(w) FROM Website w WHERE w.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    @Query("SELECT w FROM Website w WHERE w.domain LIKE %:domain%")
    List<Website> findByDomainContaining(@Param("domain") String domain);

    Boolean existsByWebsiteIdAndUserId(UUID websiteId, Long userId);
}
