package com.kony.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;

import com.kony.utils.ClientUtils;

/**
 * Servlet implementation class JSONServlet
 */
public class JSONServlet extends BaseSMSServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JSONServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processSMS(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processSMS(request, response);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processSMS(HttpServletRequest request, HttpServletResponse response) {
		Map headerMap = new HashMap();
		
		Enumeration headerNames = request.getHeaderNames();
		List<String> headerKeys = new ArrayList<String>();
		Map<String,String> paramMap = new HashMap<String,String>();
		
		try {
			StringBuilder reqBody = new StringBuilder();
	        BufferedReader br = request.getReader();
	        String str;
	        while ((str = br.readLine()) != null) {
	            reqBody.append(str);
	        }
	        String[] kvPairs = reqBody.toString().split("&");
	        for(String kvPair: kvPairs) {
        	   String[] kv = kvPair.split("=");
        	   String key = kv[0];
        	   String value = kv[1];
        	   paramMap.put(key, value);
	        }
		} catch (Exception e) {
			// TODO
		}
		
		String[] inputParamsArr = inputparams.split(",");
		for(String inputParam : inputParamsArr) {
			if(!paramMap.containsKey(inputParam)) {
        		response.setContentType("application/json");
				try {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().print(ClientUtils.getErrorMessage(inputparams));
					return;
				} catch (IOException e) {
					// TODO 
				}
			}
		}
        
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            headerKeys.add(key);
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }
        
        boolean isAuthorized = false;
        if(isAuthRequired) {
        	if(headerKeys.contains("authorization")) {
            	if((request.getHeader("authorization") != null) && request.getHeader("authorization").contains("Basic")) {
            		if ((request.getHeader("authorization").split("Basic")[1]).trim().equals(authorizationString) ) {
            			isAuthorized = true;
            		}
            	}
            }
        } else {
        	isAuthorized = true;
        }
        
        
        if(isAuthorized) {
        	try {
        		response.setContentType("application/json");
				response.getWriter().print(ClientUtils.getParamsString(paramKey, params));
			} catch (IOException e) {
				// TODO 
			}
        } else {
        	response.setStatus(Response.SC_UNAUTHORIZED);
        	response.setHeader("WWW-Authenticate", "Basic realm=\"SMS Reader\"");
        }
	}

}
