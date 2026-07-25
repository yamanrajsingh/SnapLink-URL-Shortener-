package snapLink.Url.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snapLink.Url.Dto.UrlRequest;
import snapLink.Url.Dto.UrlResponse;
import snapLink.Url.Enity.Url;
import snapLink.Url.Repository.UrlRepository;
import snapLink.Url.Service.UrlService;
import snapLink.Url.Util.ShortCodeGenerator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {
    @Autowired
    private final UrlRepository urlRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public UrlResponse createShortUrl(UrlRequest request) {

        // 1. first find the given url is exiting in database;
        // 2. if present return exiting URL
        // if not create the unique shortCode saved in the URL
        // then append the baseurl to shortCode and return response


        Optional<Url> existingUrl =
                urlRepository.findByOriginalUrl(request.getOriginalUrl());

        if (existingUrl.isPresent()) {

            UrlResponse response =
                    modelMapper.map(existingUrl.get(), UrlResponse.class);

            response.setShortUrl(
                    "http://localhost:8080/" + existingUrl.get().getShortCode());

            return response;
        }

        Url url = modelMapper.map(request, Url.class);

        String shortCode;

        do {
            shortCode = ShortCodeGenerator.generate(7);
        } while (urlRepository.existsByShortCode(shortCode));

        url.setShortCode(shortCode);

        Url savedUrl = urlRepository.save(url);

        UrlResponse response =
                modelMapper.map(savedUrl, UrlResponse.class);

        response.setShortUrl(
                "http://localhost:8080/" + savedUrl.getShortCode());

        return response;
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
