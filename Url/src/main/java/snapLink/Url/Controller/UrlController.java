package snapLink.Url.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snapLink.Url.Dto.DashboardResponse;
import snapLink.Url.Dto.UrlRequest;
import snapLink.Url.Dto.UrlResponse;
import snapLink.Url.Service.UrlService;
import java.net.URI;
import java.util.HashMap;
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
    public ResponseEntity<Page<UrlResponse>> getAllUrl(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<UrlResponse> urls = urlService.getAllUrls(page, size);

        Page<UrlResponse> response = urls.map(url -> {
            url.setShortUrl("http://localhost:8080/api/urls/" + url.getShortCode());
            return url;
        });

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Map<String, String>> deleteByShortCode(
            @PathVariable String shortCode) {

        urlService.deleteByShortCode(shortCode);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Short URL deleted successfully.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlResponse> getUrlByShortCode( @PathVariable String shortCode)
    {
        UrlResponse response = urlService.getUrlByShortCode(shortCode);
        response.setShortUrl("http://localhost:8080/api/urls" + "/" + response.getShortCode());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getUserDashboard()
    {
        DashboardResponse res = this.urlService.getUserDashboard();
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}
