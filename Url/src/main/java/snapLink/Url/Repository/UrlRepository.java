package snapLink.Url.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import snapLink.Url.Enity.Url;
import snapLink.Url.Enity.User;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url,Long> {
    Optional<Url> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    Optional<Url> findByOriginalUrl(String originalUrl);
    Page<Url> findByUser(User user, Pageable pageable);
    Optional<Url> findByShortCodeAndUser(String shortCode, User user);
    long countByUser(User user);
    @Query("""
    SELECT COALESCE(SUM(u.clickCount), 0)
    FROM Url u
    WHERE u.user = :user
""")
    long totalClickCountByUser(@Param("user") User user);
    long countByUserAndStatus(User user, String status);

}
