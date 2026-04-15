package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.dto.requestDTO.LoginRequest;
import com.datnguyeni.shop_backend.entity.InvalidatedToken;
import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.repository.InvalidatedTokenRepository;
import com.datnguyeni.shop_backend.repository.UserRepository;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Autowired
    public AuthService(JwtService jwtService, UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       InvalidatedTokenRepository invalidatedTokenRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    // LUỒNG LOGIN - CHUẨN BEST PRACTICE
    public String login(LoginRequest request) {
        // authenticationManager sẽ tự gọi xuống authenticationProvider và load user từ DB lên check password
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtService.generateToken(user);
    }

    // LUỒNG LOGOUT
    public void logout(String token) {
        try {
            // Khi User gửi API logout, token thường là Bearer ... -> Nhớ cắt chữ Bearer ở Controller trước khi truyền vào

            // Ở luồng logout, ta chỉ cần Parse token ra để lấy JTI và thời hạn thôi.
            // Không cần Verify chữ ký ở đây nữa vì API /logout chắc chắn đã được SecurityFilterChain bảo vệ rồi
            JWSObject jwsObject = JWSObject.parse(token);
            JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toString());

            String jti = claims.getJWTID();
            Date exp = claims.getExpirationTime();

            invalidatedTokenRepository.save(
                    InvalidatedToken.builder()
                            .id(jti)
                            .expiryTime(exp)
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException("Logout failed", e);
        }
    }


}