package com.webpagebytes.authgitkit;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.google.identitytoolkit.GitkitClient;
import com.google.identitytoolkit.GitkitClientException;
import com.google.identitytoolkit.GitkitUser;
import com.webpagebytes.cms.WPBAuthentication;
import com.webpagebytes.cms.WPBAuthenticationResult;
import com.webpagebytes.cms.exception.WPBException;
import com.webpagebytes.cms.exception.WPBIOException;

public class WPBGitkitAuthentication implements WPBAuthentication {

	public static final String TOKEN_COOKIE = "tokenCookie";
	public static final String LOGIN_PAGE_URL = "loginPageUrl";
	public static final String PROFILE_PAGE_URL = "profilePageUrl";
	public static final String LOGOUT_PAGE_URL = "logoutPageUrl";
	public static final String GITKIT_CLIENT_CONFIG_PATH = "gitkitClientConfigPath";
	public static final String ADMIN_EMAILS = "adminEmails"; 
	
	private Map<String, String> configs;
	private String tokenCookie;
	private String loginPageUrl;
	private String profilePageUrl;
	private String logoutPageUrl;
	private String gitkitClientConfigPath;
	private GitkitClient gitkitClient;
	private Set<String> adminEmails = new HashSet<String>();
	
	@Override
	public void initialize(Map<String, String> params) throws WPBException {
		configs = ConfigReader.getConfigs();
		if (configs == null || configs.size() == 0)
		{
			throw new WPBException("No configs for WPBGitkitAuthentication");
		}
		tokenCookie = configs.get(TOKEN_COOKIE);
		loginPageUrl = configs.get(LOGIN_PAGE_URL);
		profilePageUrl = configs.get(PROFILE_PAGE_URL);
		logoutPageUrl = configs.get(LOGOUT_PAGE_URL);
		gitkitClientConfigPath = configs.get(GITKIT_CLIENT_CONFIG_PATH);
		if (tokenCookie == null || tokenCookie.length() == 0 ||
			loginPageUrl == null || loginPageUrl.length() == 0 ||
			profilePageUrl == null || profilePageUrl.length() == 0 ||
			logoutPageUrl == null || logoutPageUrl.length() == 0 ||
			gitkitClientConfigPath == null || gitkitClientConfigPath.length() == 0)
		{
			throw new WPBException("Bad configs for WPBGitkitAuthentication");
		}
		try
		{
			gitkitClient = GitkitClient.createFromJson(gitkitClientConfigPath);
		} catch (Exception e)
		{
			throw new WPBException("Error creating the GitkitClient", e);
		}
		
		String adminEmailsStr = configs.get(ADMIN_EMAILS);
		if (adminEmailsStr != null)
		{
			StringTokenizer tokenizer = new StringTokenizer(adminEmailsStr, ";, ");
			while (tokenizer.hasMoreElements())
			{
				String email = tokenizer.nextToken();
				if (email.length()>0)
				{
					adminEmails.add(email);
				}
			}
		}
	}

	
	@Override
	public WPBAuthenticationResult checkAuthentication(
			HttpServletRequest request) throws WPBIOException {		
		WPBDefaultAuthenticationResult result = new WPBDefaultAuthenticationResult();
		result.setLoginLink(loginPageUrl);
		result.setLogoutLink(logoutPageUrl);
		result.setUserProfileLink(profilePageUrl);
		try
		{
			GitkitUser gitkitUser = gitkitClient.validateTokenInRequest(request);
			if (null != gitkitUser && adminEmails.contains(gitkitUser.getEmail()))
			{
				result.setUserIdentifier(gitkitUser.getEmail());
			}
		} catch (GitkitClientException e)
		{
			// we will consider the user as not authenticated
		}

		return result;
	}

}
