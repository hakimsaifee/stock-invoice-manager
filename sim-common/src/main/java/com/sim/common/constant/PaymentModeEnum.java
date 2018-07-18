package com.sim.common.constant;

import java.util.ArrayList;
import java.util.List;

public enum PaymentModeEnum {

	CASH("Cash"),
	
	CARD("Card"),
	
	PAYTM("Paytm");
	
	private String name;
	
	PaymentModeEnum(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static List<String> getValues() {
		List<String> paymentModes = new ArrayList<>();
		for (PaymentModeEnum enumValue : values()) {
			paymentModes.add(enumValue.getName());
		}
		return paymentModes;
	}
}
