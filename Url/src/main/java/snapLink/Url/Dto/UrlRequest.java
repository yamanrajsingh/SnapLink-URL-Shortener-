package snapLink.Url.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UrlRequest {
    @NotBlank(message = "Original URL is required")
    @Pattern(
            regexp = "^(https?://).+",
            message = "Please provide a valid HTTP or HTTPS URL"
    )
    private String originalUrl;
}
