package snapLink.Url.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import snapLink.Url.Dto.UrlRequest;
import snapLink.Url.Dto.UrlResponse;
import snapLink.Url.Enity.Url;
import snapLink.Url.Enity.User;

import java.util.List;

@Service
public interface UrlService {
    UrlResponse createShortUrl(UrlRequest request);
    Page<UrlResponse> getAllUrls(int page, int size);
    void deleteByShortCode(String shortCode);
    String getOriginalUrl(String shortCode);
    UrlResponse getUrlByShortCode(String shortCode);



}
