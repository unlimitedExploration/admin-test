package com.example.admin.config;

import com.example.admin.bean.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义验证码过滤器
 */
@Component
public class VerificationCodeFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if ("POST".equals(req.getMethod()) && "/doLogin".equals(req.getServletPath())){
            String code = req.getParameter("code");
            String verifyCode = (String) req.getSession().getAttribute("verify_code");
            if (code == null || verifyCode == null || "".equals(code) || !verifyCode.toLowerCase().equals(code.toLowerCase())){
                //验证码不正确
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                out.write(new ObjectMapper().writeValueAsString(RespBean.error("验证码错误")));
                out.flush();
                out.close();
                return;
            } else {
                filterChain.doFilter(req,resp);
            }
        } else {
            filterChain.doFilter(req,resp);
        }
    }
}
