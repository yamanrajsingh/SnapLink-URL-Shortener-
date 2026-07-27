package snapLink.Url.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlResponse {
    private Long id;

    private String originalUrl;

    private String shortCode;

    private String shortUrl;

    private LocalDateTime createdAt;

    private  Long clickCount;
}
