package snapLink.Url.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snapLink.Url.Enity.Url;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url,Long> {
    Optional<Url> FindByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    Optional<Url> findByOriginalUrl(String originalUrl);
}
