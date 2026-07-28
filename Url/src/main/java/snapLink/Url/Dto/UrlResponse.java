package snapLink.Url.Dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UrlResponse implements Serializable {
    private Long id;

    private String originalUrl;

    private String shortCode;

    private String shortUrl;

    private  Long clickCount;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
