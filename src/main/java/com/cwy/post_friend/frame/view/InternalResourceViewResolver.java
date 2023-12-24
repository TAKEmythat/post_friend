package com.cwy.post_friend.frame.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 视图解析器
 *
 * @projectName: post_friend
 * @package: com.cwy.post_friend.frame.view
 * @className: InternalResourceViewResolver
 * @author: LGJ
 * @description: TODO
 * @date: 2023/12/24 22:26
 * @version: 1.0
 */
public class InternalResourceViewResolver {
    private String prefix;
    private String suffix;

    public String getPrefix() {
        return prefix;
    }

    public void render(String where, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(prefix + where + suffix);
        requestDispatcher.forward(request,response);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public InternalResourceViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }
}
