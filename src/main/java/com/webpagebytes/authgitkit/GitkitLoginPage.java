package com.webpagebytes.authgitkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GitkitLoginPage extends HttpServlet {

private static final long serialVersionUID = 1L;
public static final String LOGIN_PAGE_FILE = "loginPageFilePath";
private String loginPageContent; 

public void init() throws ServletException
{	
	Map<String, String> configs = ConfigReader.getConfigs();
	if (!configs.containsKey(LOGIN_PAGE_FILE))
	{
		throw new ServletException("No 'loginPagePath' config set for GitkitLoginPage servlet");
	}
	String loginPagePath = configs.get(LOGIN_PAGE_FILE);
	try
	{
		byte[] bytes = Files.readAllBytes(Paths.get(loginPagePath));
		loginPageContent = new String(bytes, "UTF-8");
	} catch (IOException e)
	{
		throw new ServletException("Reading file from 'loginPageFilePath'", e);
	}
}

public void doGet(HttpServletRequest req, HttpServletResponse resp)
	     throws ServletException, java.io.IOException
{
	resp.setContentType("text/html");
	resp.getOutputStream().write(loginPageContent.getBytes("UTF-8"));
	resp.getOutputStream().flush();
}

}
