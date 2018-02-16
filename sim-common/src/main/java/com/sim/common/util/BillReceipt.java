package com.sim.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class BillReceipt {

	public static void main(String[] args) {
		
		Configuration configuration = new Configuration();
		
		try {
			Template template = configuration.getTemplate("example.ftl");
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", "This is invoice");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
