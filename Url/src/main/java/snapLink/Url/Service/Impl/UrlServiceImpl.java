package snapLink.Url.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import snapLink.Url.Dto.DashboardResponse;
import snapLink.Url.Dto.UrlRequest;
import snapLink.Url.Dto.UrlResponse;
import snapLink.Url.Enity.Url;
import snapLink.Url.Enity.User;
import snapLink.Url.Exception.LinkExpiredException;
import snapLink.Url.Exception.ResourceNotFoundException;
import snapLink.Url.Repository.UrlRepository;
import snapLink.Url.Repository.UserRepository;
import snapLink.Url.Service.UrlService;
import snapLink.Url.Util.ShortCodeGenerator;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {
    @Autowired
    private final UrlRepository urlRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ModelMapper modelMapper;


// Extract the Current User

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("No authenticated user");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }



    @Override
    @CachePut(value = "urlResponses", key = "#result.shortCode")
    public UrlResponse createShortUrl(UrlRequest request) {
        Optional<Url> existingUrl =
                urlRepository.findByOriginalUrl(request.getOriginalUrl());

        if (existingUrl.isPresent()) {

            UrlResponse response =
                    modelMapper.map(existingUrl.get(), UrlResponse.class);

            response.setShortUrl(
                    "http://localhost:8080" + existingUrl.get().getShortCode());

            return response;
        }

        Url url = modelMapper.map(request, Url.class);

        String shortCode;

        do {
            shortCode = ShortCodeGenerator.generate(7);
        } while (urlRepository.existsByShortCode(shortCode));

        url.setShortCode(shortCode);
        url.setStatus("ACTIVE");

        if (request.getExpireAt() != null) {
            url.setExpiresAt(request.getExpireAt());
        } else {
            url.setExpiresAt(LocalDateTime.now().plusMonths(1));
        }

        User current = getCurrentUser();
        url.setUser(current);

        Url savedUrl = urlRepository.save(url);

        UrlResponse response =
                modelMapper.map(savedUrl, UrlResponse.class);

        response.setShortUrl(
                "http://localhost:8080/api/urls" + savedUrl.getShortCode());

        return response;
    }

    @Override
    public Page<UrlResponse> getAllUrls(int page , int size) {
        User current = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);

        Page<Url> url = this.urlRepository.findByUser(current,pageable);

        return url.map(url1 -> modelMapper.map(url1, UrlResponse.class));
    }

    @Override
    @CacheEvict(value = "urls", key = "#shortCode")
    public void deleteByShortCode(String shortCode) {
        User current = getCurrentUser();
        Optional<Url> url =  this.urlRepository.findByShortCodeAndUser(shortCode,current);
        if(url.isPresent()) {
            this.urlRepository.deleteById(url.get().getId());
        }
        else {
            throw new  ResourceNotFoundException("Url not found");
        }

    }
    @Override
    @Cacheable(value = "originalUrl", key = "#shortCode")
    public String getOriginalUrl(String shortCode)
    {
        Url url  = this.urlRepository.findByShortCode(shortCode).orElseThrow(()-> new ResourceNotFoundException("Short URL is Not Found"));
        if (url.getExpiresAt() != null &&
                LocalDateTime.now().isAfter(url.getExpiresAt())) {
            url.setStatus("Expire");
            this.urlRepository.save(url);

            throw new LinkExpiredException("This short link has expired.");
        }
        url.setClickCount(url.getClickCount()+1);
        urlRepository.save(url);
        return url.getOriginalUrl();
    }

    @Override
    public UrlResponse getUrlByShortCode(String shortCode) {
        User curr = getCurrentUser();
        Url url = this.urlRepository.findByShortCodeAndUser(shortCode,curr).orElseThrow(()-> new ResourceNotFoundException("Short URL is Not Found"));
        return this.modelMapper.map(url, UrlResponse.class);
    }

    @Override
    public DashboardResponse getUserDashboard(){
        User user = getCurrentUser();

        long totalUrl = this.urlRepository.countByUser(user);
        long totalClick = this.urlRepository.totalClickCountByUser(user);
        long totalActiveUrl = this.urlRepository.countByUserAndStatus(user,"ACTIVE");
        long totalExpireUrl = this.urlRepository.countByUserAndStatus(user,"EXPIRED");
     return new DashboardResponse(totalUrl,totalClick,totalActiveUrl,totalExpireUrl);
    }





}
