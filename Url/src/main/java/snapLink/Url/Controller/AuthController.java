package snapLink.Url.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import snapLink.Url.Dto.UserRegisterRequest;
import snapLink.Url.Service.AuthService;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        this.authService.register(userRegisterRequest);
        return  ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }


}
