package snapLink.Url.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import snapLink.Url.Dto.LoginRequest;
import snapLink.Url.Dto.LoginResponse;
import snapLink.Url.Dto.UserRegisterRequest;
import snapLink.Url.Enity.User;
import snapLink.Url.Repository.UserRepository;
import snapLink.Url.Service.AuthService;

@Service
public class AuthServiceImpl  implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String register(UserRegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new RuntimeException("User already exist") ;
        }
        else {
            User user = new User();
            user.setName(registerRequest.getName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole("USER");
            userRepository.save(user);
        }
        return "User register successfully";
    }
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        if(!userRepository.existsByEmail(loginRequest.getEmail())){
            throw new RuntimeException("Email is not exist") ;
        }else {
            //

        }
    }
}
