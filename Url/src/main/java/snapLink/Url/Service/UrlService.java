package snapLink.Url.Service;

import org.springframework.stereotype.Service;
import snapLink.Url.Dto.UrlRequest;
import snapLink.Url.Dto.UrlResponse;

import java.util.List;

@Service
public interface UrlService {
    UrlResponse createShortUrl(UrlRequest request);
    List<UrlResponse> getAllUrls();
    void deleteByShortCode(String shortCode);
    String getOriginalUrl(String shortCode);
    UrlResponse getUrlByShortCode(String shortCode);

}
