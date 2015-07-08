package com.webpagebytes.authgitkit;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GitkitLogout extends HttpServlet {

private String loginPageUrl;
private String tokenCookie;

public void init() throws ServletException
{	
	Map<String, String> configs = ConfigReader.getConfigs();
	loginPageUrl = configs.get(WPBGitkitAuthentication.LOGIN_PAGE_URL);
	tokenCookie = configs.get(WPBGitkitAuthentication.TOKEN_COOKIE);
}

public void doGet(HttpServletRequest req, HttpServletResponse resp)
	     throws ServletException, java.io.IOException
{
	Cookie cookie = new Cookie(tokenCookie, "");
	cookie.setPath("/");
	cookie.setMaxAge(0);
	resp.addCookie(cookie);
	resp.sendRedirect(loginPageUrl);
}


}
