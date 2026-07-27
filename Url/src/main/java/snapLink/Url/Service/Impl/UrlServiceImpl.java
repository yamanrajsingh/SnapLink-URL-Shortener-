package snapLink.Url.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snapLink.Url.Dto.UrlRequest;
import snapLink.Url.Dto.UrlResponse;
import snapLink.Url.Enity.Url;
import snapLink.Url.Exception.ResourceNotFoundException;
import snapLink.Url.Repository.UrlRepository;
import snapLink.Url.Service.UrlService;
import snapLink.Url.Util.ShortCodeGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                    "http://localhost:8080" + existingUrl.get().getShortCode());

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
                "http://localhost:8080/api/urls" + savedUrl.getShortCode());

        return response;
    }


    @Override
    public List<UrlResponse> getAllUrls() {

        List<Url> urls = this.urlRepository.findAll();
        return urls.stream().map(url -> this.modelMapper.map(url,UrlResponse.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteByShortCode(String shortCode) {
        Optional<Url> url = this.urlRepository.findByShortCode(shortCode);
        if(url.isPresent()) {
            this.urlRepository.deleteById(url.get().getId());
        }
        else {
            throw new  ResourceNotFoundException("Url not found");
        }

    }
    @Override
    public String getOriginalUrl(String shortCode)
    {
        Url url  = this.urlRepository.findByShortCode(shortCode).orElseThrow(()-> new ResourceNotFoundException("Short URL is Not Found"));
        url.setClickCount(url.getClickCount()+1);
        urlRepository.save(url);
        return url.getOriginalUrl();
    }

    @Override
    public UrlResponse getUrlByShortCode(String shortCode) {
        Url url = this.urlRepository.findByShortCode(shortCode).orElseThrow(()-> new ResourceNotFoundException("Short URL is Not Found"));
        return this.modelMapper.map(url, UrlResponse.class);
    }

}
