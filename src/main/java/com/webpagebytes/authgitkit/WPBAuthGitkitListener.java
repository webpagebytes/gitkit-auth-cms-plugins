package com.webpagebytes.authgitkit;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class WPBAuthGitkitListener implements ServletContextListener {

public static final String AUTH_GITKIT_CONFIG_FILE = "wpbAuthGitkitConfigFile";
@Override
public void contextInitialized(ServletContextEvent servletContext) {
	String configPath = servletContext.getServletContext().getInitParameter(AUTH_GITKIT_CONFIG_FILE);
	if (null == configPath)
	{
		throw new RuntimeException("There is no wpbAuthGitkitConfigFile parameter defined on WPBAuthGitkitListener context initialized "); 
	}
	try
	{
		ConfigReader.readConfig(configPath);
	} catch (IOException e)
	{
		throw new RuntimeException(" Exception reading wpbAuthGitkitConfigFile", e);
	}

}

@Override
public void contextDestroyed(ServletContextEvent sce) {
}

}
