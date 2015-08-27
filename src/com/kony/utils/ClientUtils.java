package com.kony.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;

import com.kony.entities.KeyValue;

public class ClientUtils {
	
    public static Logger logger = Logger.getLogger(ClientUtils.class);
	
	public static String getParamsString(String paramKey, String params) {

		if(params == null) {
			return null;
		}
		String[] paramsArray = params.split(",");
		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		
		for(String param: paramsArray) {
			if(StringUtils.equals(param, "amount")) {
				Random rand = new Random();
				keyValueList.add(new KeyValue(param, String.valueOf(rand.nextInt(1500))));
			} else if(StringUtils.equals(param, "date")) {
				keyValueList.add(new KeyValue(param, (new Date()).toString()));
			} else {
				keyValueList.add(new KeyValue(param, UUID.randomUUID().toString()));
			}
			
		}
		
		return "{ \""+paramKey+"\":"+""+keyValueList.toString()+"}";
		//return keyValueList.toString();
	}

	public static String getErrorMessage(String requiredParams) {
		return "{ \"message\" : \"Missing Required Input Parameters. Required Parameters are : "+requiredParams+" \" }";
	}
	
	public static void main(String[] args) {
		//System.out.println(getParamsString("paramsConfig", "text,date,month,year"));
	}
	
}
