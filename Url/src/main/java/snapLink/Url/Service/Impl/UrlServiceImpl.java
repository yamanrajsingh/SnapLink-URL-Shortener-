package snapLink.Url.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snapLink.Url.Dto.UrlRequest;
import snapLink.Url.Dto.UrlResponse;
import snapLink.Url.Enity.Url;
import snapLink.Url.Repository.UrlRepository;
import snapLink.Url.Service.UrlService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {
    @Autowired
    private final UrlRepository urlRepository;

    @Override
    public UrlResponse createShortUrl(UrlRequest request) {
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(request.getOriginalUrl());
        if (existingUrl.isPresent()) {
            return mapToResponse(existingUrl.get());
        }
        Url savedUrl = this.urlRepository.save(request.getOriginalUrl());
        return null;
    }

    @Override
    public UrlResponse getUrlByShortCode(String shortCode) {
        return null;
    }

    @Override
    public List<UrlResponse> getAllUrls() {
        return null;
    }

    @Override
    public void deleteByShortCode(String shortCode) {

    }

}
