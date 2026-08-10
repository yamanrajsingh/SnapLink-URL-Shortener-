package snapLink.Url.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import snapLink.Url.Dto.UrlRequest;
import snapLink.Url.Dto.UrlResponse;

@Service
public interface UrlService {
    UrlResponse createShortUrl(UrlRequest request);
    Page<UrlResponse> getAllUrls(int page, int size);
    void deleteByShortCode(String shortCode);
    String getOriginalUrl(String shortCode);
    UrlResponse getUrlByShortCode(String shortCode);
}
