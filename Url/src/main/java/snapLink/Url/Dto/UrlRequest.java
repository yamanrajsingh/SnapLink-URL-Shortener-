package snapLink.Url.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlRequest {
    @NotBlank(message = "Original URL is required")
    @Pattern(
            regexp = "^(https?://).+",
            message = "Please provide a valid HTTP or HTTPS URL"
    )
    private String originalUrl;

    private LocalDateTime expireAt;
}
