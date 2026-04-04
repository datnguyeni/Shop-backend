package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.entity.Role;
import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.entity.enums.UserStatus;
import com.datnguyeni.shop_backend.repository.RoleRepository;
import com.datnguyeni.shop_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class CustomUserService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Autowired
    public CustomUserService(UserRepository userRepository,
                             RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // ================== LOGIN THƯỜNG ==================
    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email"));
    }

    // ================== LOGIN GOOGLE ==================
    // create or get user
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setStatus(UserStatus.ACTIVE);

//            user.setProvider("GOOGLE");
//
//            String randomPassword = UUID.randomUUID().toString();
//            user.setPassword(passwordEncoder.encode(randomPassword));
            user.setPassword("OAUTH2_USER_NO_PASSWORD");

            Role defaultRole = roleRepository.findByRoleName("USER").orElse(null);
            if (defaultRole != null) {
                user.getRoles().add(defaultRole);
            }

            userRepository.save(user);
        }

        return oAuth2User;
    }



}