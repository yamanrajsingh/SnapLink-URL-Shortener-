package snapLink.Url.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import snapLink.Url.Config.Security.CustomUserDetailsService;
import snapLink.Url.Dto.LoginRequest;
import snapLink.Url.Dto.LoginResponse;
import snapLink.Url.Dto.UserRegisterRequest;
import snapLink.Url.Enity.User;
import snapLink.Url.Repository.UserRepository;
import snapLink.Url.Service.AuthService;
import snapLink.Url.Util.JwtService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl  implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtService jwtService;

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
     authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
           UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());

           String token = jwtService.generateToken(userDetails);
            return LoginResponse.builder().token(token).type("USER").build();
        }
    }
}
