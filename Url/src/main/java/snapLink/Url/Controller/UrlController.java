package snapLink.Url.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import snapLink.Url.Dto.UrlRequest;
import snapLink.Url.Dto.UrlResponse;
import snapLink.Url.Service.UrlService;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    @Autowired
    private  UrlService urlService;


    @PostMapping
    public ResponseEntity<UrlResponse> createShortUrl(
            @Valid @RequestBody UrlRequest urlRequest) {

        UrlResponse response = urlService.createShortUrl(urlRequest);

        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();
        response.setShortUrl(baseUrl + "/" + response.getShortCode());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
