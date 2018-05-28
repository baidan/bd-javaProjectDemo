package com.baidan.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//解决post方式请求参数和响应编码问题的过滤器
public class SetCharacterEncodingFilter implements Filter {
    private FilterConfig filterConfig;
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        String encoding = filterConfig.getInitParameter("encoding");//用户可能忘记了配置该参数
        if(encoding==null){
            encoding = "UTF-8";//默认编码
        }

        request.setCharacterEncoding(encoding);//只能解决POST请求参数的中文问题
        response.setCharacterEncoding(encoding);//输出流编码
        response.setContentType("text/html;charset="+encoding);//输出流编码，通知了客户端应该使用的编码
        chain.doFilter(request, response);
    }

    public void destroy() {

    }

}
