package snapLink.Url.Service;

import org.springframework.stereotype.Service;
import snapLink.Url.Dto.LoginRequest;
import snapLink.Url.Dto.LoginResponse;
import snapLink.Url.Dto.UserRegisterRequest;

@Service
public interface AuthService {
    String register(UserRegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
