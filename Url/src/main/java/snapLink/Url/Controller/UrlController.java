package snapLink.Url.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import snapLink.Url.Dto.UrlRequest;
import snapLink.Url.Dto.UrlResponse;
import snapLink.Url.Enity.Url;
import snapLink.Url.Service.UrlService;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    @Autowired
    private  UrlService urlService;


    @PostMapping
    public ResponseEntity<UrlResponse> createShortUrl(
            @Valid @RequestBody UrlRequest urlRequest) {

        UrlResponse response = urlService.createShortUrl(urlRequest);

        response.setShortUrl("http://localhost:8080/api/urls" + "/" + response.getShortCode());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl( @PathVariable String shortCode) {
    String originalUrl = this.urlService.getOriginalUrl(shortCode);
     return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }

    @GetMapping()
    public ResponseEntity<List<UrlResponse>> getAllUrl()
    {
        List<UrlResponse> urls = this.urlService.getAllUrls();

        urls.forEach(url ->
                url.setShortUrl("http://localhost:8080/api/urls" + "/" + url.getShortCode()));
        return ResponseEntity.status(HttpStatus.OK).body(urls);
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Map<String, String>> deleteByShortCode(
            @PathVariable String shortCode) {

        urlService.deleteByShortCode(shortCode);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Short URL deleted successfully.");

        return ResponseEntity.ok(response);
    }
}
