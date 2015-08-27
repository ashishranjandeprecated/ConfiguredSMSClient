package com.kony.service;

import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public abstract class BaseSMSServlet extends HttpServlet {

    /**
	 *  @author Ashish Ranjan
	 */
    private static final long serialVersionUID = 1083925980473415669L;
    
	public static String PARAM_LIST = "params";
	public static String PARAM_KEY = "params.key";
	public static String IS_AUTH_KEY = "isAuth";
	public static String INPUT_PARAM_KEY = "input.params";
	
	public static String params;
	public static String paramKey;
	public static String inputparams;
	public static boolean isAuthRequired = true;
	public static String authorizationString ;
	public static boolean initialized = false;
	public static String DEFAULT_USERNAME = "admin";
	public static String DEFAULT_PASSWORD = "admin";

    @Override
    public void init(ServletConfig config) throws ServletException {
    	
        if (!initialized) {
            super.init(config);
            PropertyConfigurator.configure(BaseSMSServlet.class.getResource("log4j.properties"));
            Logger logger = Logger.getLogger(BaseSMSServlet.class);
            logger.debug("Log4j initialized.");
            initialized = true;
            
            Properties clientProperties = new Properties();
            String username = null;
            String password = null;
            
    		try {
    			ServletContext servletContext = config.getServletContext();
    			clientProperties.load(servletContext.getResourceAsStream("/WEB-INF/params.properties"));
        		isAuthRequired = Boolean.getBoolean((String) clientProperties.get(IS_AUTH_KEY));
        		paramKey = (String) clientProperties.get(PARAM_KEY);
        		params = (String) clientProperties.get(PARAM_LIST);
        		inputparams = (String) clientProperties.get(INPUT_PARAM_KEY);
    			username = clientProperties.getProperty("username", DEFAULT_USERNAME);
    			password = clientProperties.getProperty("password", DEFAULT_PASSWORD);
    		} catch (Exception e) {
    			logger.error("Unable to params.properties file. Please provide params.properties file. ",e);
    			username = DEFAULT_USERNAME;
    			password = DEFAULT_PASSWORD;
    		} 
    		authorizationString = getAuthorizationString(username, password);
        } else {
            Logger logger = Logger.getLogger(BaseSMSServlet.class);
            logger.debug("Ignoring Log4j initialization.");
        }
        
    }
    
    private static String getAuthorizationString(String username, String password) {
    	return Base64.encode((username+":"+password).getBytes());
    }
}
