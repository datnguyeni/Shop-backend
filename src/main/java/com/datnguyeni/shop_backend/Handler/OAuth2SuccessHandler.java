package com.datnguyeni.shop_backend.Handler;

import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.repository.UserRepository;
import com.datnguyeni.shop_backend.service.JwtService;
import com.datnguyeni.shop_backend.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public OAuth2SuccessHandler(JwtService jwtService,  UserRepository userRepository) {
         this.jwtService = jwtService;
         this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1. Lấy email từ kết quả đăng nhập Google
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // 2. Chọc xuống DB lấy đúng thực thể User của hệ thống mình lên
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Không tìm thấy user dù đã qua bước lưu DB!")
        );

        String token = jwtService.generateToken(user);

        // 4. Điều hướng về trang chủ Frontend kèm theo Token
        String targetUrl = "http://127.0.0.1:5500/index.html?token=" + token;

        response.sendRedirect(targetUrl);
    }
}

