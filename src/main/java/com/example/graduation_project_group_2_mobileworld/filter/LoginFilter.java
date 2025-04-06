package com.example.graduation_project_group_2_mobileworld.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@WebFilter(urlPatterns = "/api/*") // Chỉ áp dụng cho các URL bắt đầu bằng /api/
@Order(1) // Đảm bảo filter chạy trước các filter khác
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Không tạo session mới

        // Các URL công khai không cần kiểm tra
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.startsWith("/tai-khoan/login") ||
                requestURI.startsWith("/tai-khoan/requestOtp") ||
                requestURI.startsWith("/tai-khoan/addTk")) {
            chain.doFilter(request, response);
            return;
        }

        // Kiểm tra xem session có tồn tại và người dùng đã đăng nhập chưa
        if (session == null || session.getAttribute("loggedInUser") == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            httpResponse.getWriter().write("Vui lòng đăng nhập để truy cập!");
            return;
        }

        // Nếu đã đăng nhập, tiếp tục xử lý request
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}