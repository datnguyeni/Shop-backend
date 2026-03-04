package com.datnguyeni.shop_backend.configuration;

import com.datnguyeni.shop_backend.entity.Role;
import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.entity.enums.UserStatus;
import com.datnguyeni.shop_backend.repository.RoleRepository;
import com.datnguyeni.shop_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Slf4j
@Configuration
public class ApplicationInitConfig {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ApplicationInitConfig(UserRepository userRepository,  RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // INIT ADMIN
    @Bean
    ApplicationRunner applicationRunner(PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                Role adminRole = roleRepository.findByRoleName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

                User user =  new User();
                user.setPassword(passwordEncoder.encode("admin"));
                user.setEmail("admin@gmail.com");
                user.setRoles(Set.of(adminRole));

                userRepository.save(user);

                log.info("Admin created when running application");
            }
        };
    }



}
