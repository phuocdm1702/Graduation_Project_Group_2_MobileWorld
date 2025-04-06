package com.example.graduation_project_group_2_mobileworld.config;

import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import com.example.graduation_project_group_2_mobileworld.repository.tai_khoan.TaiKhoanRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/tai-khoan/login", "/tai-khoan/requestOtp", "/tai-khoan/addTk").permitAll() // Cho phép đăng nhập và đăng ký
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Chỉ ADMIN
                        .requestMatchers("/api/staff/**").hasRole("STAFF") // Chỉ STAFF
                        .requestMatchers("/api/user/**").hasRole("USER") // Chỉ USER
                        .requestMatchers("/api/**").authenticated() // Yêu cầu xác thực cho các API khác
                        .anyRequest().permitAll() // Các endpoint khác tạm thời cho phép
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService(TaiKhoanRepository taiKhoanRepository) {
        return username -> {
            TaiKhoan tk = taiKhoanRepository.findByTenDangNhap(username);
            if (tk == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            // Ánh xạ cap_quyen_han sang vai trò
            String role;
            int capQuyenHan = tk.getIdQuyenHan().getCapQuyenHan();
            switch (capQuyenHan) {
                case 1:
                    role = "ADMIN";
                    break;
                case 2:
                    role = "STAFF";
                    break;
                case 3:
                    role = "USER";
                    break;
                default:
                    role = "USER"; // Mặc định là USER nếu không xác định
            }
            return User.withUsername(tk.getTenDangNhap())
                    .password(tk.getMatKhau()) // Mật khẩu đã mã hóa
                    .roles(role) // Gán vai trò động
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}