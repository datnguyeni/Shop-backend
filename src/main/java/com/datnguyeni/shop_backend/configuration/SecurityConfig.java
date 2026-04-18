//package com.datnguyeni.shop_backend.configuration;
//
//import com.datnguyeni.shop_backend.Handler.OAuth2SuccessHandler;
//import com.datnguyeni.shop_backend.repository.InvalidatedTokenRepository;
//import com.datnguyeni.shop_backend.service.CustomUserService;
//import jakarta.servlet.DispatcherType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
//import org.springframework.security.oauth2.core.OAuth2Error;
//import org.springframework.security.oauth2.core.OAuth2TokenValidator;
//import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
//import org.springframework.security.oauth2.jwt.*;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.crypto.spec.SecretKeySpec;
//
//@EnableMethodSecurity
//@Configuration
//public class SecurityConfig {
//
//    @Value("${jwt.secret}")
//    private String SIGNER_KEY;
//
//    private final CustomUserService customUserService;
//    private final InvalidatedTokenRepository invalidatedTokenRepository;
//    private final OAuth2SuccessHandler oAuth2SuccessHandler;
//
//    @Autowired
//    public SecurityConfig(CustomUserService customUserService,
//                          InvalidatedTokenRepository invalidatedTokenRepository,
//                          OAuth2SuccessHandler oAuth2SuccessHandler) {
//        this.customUserService = customUserService;
//        this.invalidatedTokenRepository = invalidatedTokenRepository;
//        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
//    }
//
//
//    // ==========================================
//    // CÁC BEAN DÙNG CHO LUỒNG LOGIN
//    // ==========================================
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // DÙNG CHO LOGIN: Cung cấp logic để check DB xem user/password có đúng không
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(customUserService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
//
//    // DÙNG CHO LOGIN: Người quản lý quá trình đăng nhập (Được gọi trong AuthService)
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    // ==========================================
//    // CÁC BEAN DÙNG CHO OAUTH2 RESOURCE SERVER (CHECK TOKEN KHI GỌI API)
//    // ==========================================
//
//    // DÙNG CHO API: Bóc tách chuỗi JWT ra để lấy danh sách Role
//    @Bean
//    public JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
//        converter.setAuthoritiesClaimName("roles");
//        converter.setAuthorityPrefix(""); // Vì bên JwtService em đã chủ động thêm tiền tố "ROLE_" rồi
//
//        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
//        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
//        return jwtConverter;
//    }
//
//
//    // DÙNG CHO API: Người gác cổng (Giải mã + Check hạn + Check Blacklist)
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HmacSHA256");
//        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
//
//        // 1. Validator mặc định của Spring (Check thời gian Expiration của token)
//        OAuth2TokenValidator<Jwt> defaultValidator = new JwtTimestampValidator();
//
//        // 2. Validator tự viết (Check trong sổ đen DB)
//        OAuth2TokenValidator<Jwt> blacklistValidator = jwt -> {
//            String jti = jwt.getId();
//
//            if (jti != null && invalidatedTokenRepository.existsById(jti)) {
//                return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Token đã bị đăng xuất", null));
//            }
//            return OAuth2TokenValidatorResult.success();
//        };
//
//        // Gộp cả 2 điều kiện lại: Token phải còn hạn VÀ không nằm trong blacklist
//
//        OAuth2TokenValidator<Jwt> delegatingValidator = new DelegatingOAuth2TokenValidator<>(defaultValidator, blacklistValidator);
//
//        jwtDecoder.setJwtValidator(delegatingValidator);
//        return jwtDecoder;
//    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/product/**").permitAll()
//                        .requestMatchers("/category/**").permitAll()
//                        .requestMatchers("/cart/**").permitAll()
//                        .requestMatchers("/user/**").permitAll()
//                        .requestMatchers("/order/**").permitAll()
//
//                        .requestMatchers("/oauth2/authorization/**").permitAll()
//                        .requestMatchers("/login/**").permitAll()
//
//                        .requestMatchers("/payments/vnpay-return").permitAll()
//                        .requestMatchers("/payments/vnpay-ipn").permitAll()
//
//                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
//                        .anyRequest().authenticated()
//                )
//                // LOGIN GOOGLE
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(customUserService)
//                        )
//                        .successHandler(oAuth2SuccessHandler)
//                )
//
////                .oauth2Login(org.springframework.security.config.Customizer.withDefaults())
//
//                // DÙNG JWT CHO API
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(jwt -> jwt
//                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                        )
//                )
//
//                .build();
//    }
//
//
//}

package com.datnguyeni.shop_backend.configuration;

import com.datnguyeni.shop_backend.Handler.OAuth2SuccessHandler;
import com.datnguyeni.shop_backend.repository.InvalidatedTokenRepository;
import com.datnguyeni.shop_backend.service.CustomUserService;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String SIGNER_KEY;

    private final CustomUserService customUserService;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Autowired
    public SecurityConfig(CustomUserService customUserService,
                          InvalidatedTokenRepository invalidatedTokenRepository,
                          OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.customUserService = customUserService;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Cho phép cổng của Live Server
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500", "http://localhost:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Cho phép mọi Header
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    // ==========================================
    // CÁC BEAN DÙNG CHO LUỒNG LOGIN
    // ==========================================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // ==========================================
    // CÁC BEAN DÙNG CHO OAUTH2 RESOURCE SERVER (CHECK TOKEN KHI GỌI API)
    // ==========================================

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("roles");
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HmacSHA256");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build();

        OAuth2TokenValidator<Jwt> defaultValidator = new JwtTimestampValidator();

        OAuth2TokenValidator<Jwt> blacklistValidator = jwt -> {
            String jti = jwt.getId();
            if (jti != null && invalidatedTokenRepository.existsById(jti)) {
                return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Token đã bị đăng xuất", null));
            }
            return OAuth2TokenValidatorResult.success();
        };

        OAuth2TokenValidator<Jwt> delegatingValidator = new DelegatingOAuth2TokenValidator<>(defaultValidator, blacklistValidator);
        jwtDecoder.setJwtValidator(delegatingValidator);
        return jwtDecoder;
    }

    // ==========================================
    // CẤU HÌNH PHÂN QUYỀN (AUTHORIZATION)
    // ==========================================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**", "/login/**", "/oauth2/authorization/**").permitAll()
                        .requestMatchers("/product/**", "/category/**").permitAll()
                        .requestMatchers("/user/create").permitAll()
                        .requestMatchers("/dashboard/**").permitAll()
                        .requestMatchers("/productVariant/**").permitAll()

                        .requestMatchers("/payments/vnpay-return", "/payments/vnpay-ipn").permitAll()

                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                        .requestMatchers("/cart/**").authenticated()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/order/**").authenticated()

                        .anyRequest().authenticated()
                )
                // LOGIN GOOGLE
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customUserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                )


                // DÙNG JWT CHO API
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }
}