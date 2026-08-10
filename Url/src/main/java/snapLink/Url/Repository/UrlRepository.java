package snapLink.Url.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
