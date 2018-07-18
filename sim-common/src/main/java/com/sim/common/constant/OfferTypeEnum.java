package com.sim.common.constant;

public enum OfferTypeEnum {
	
	WITHOUT_OFFER("No Offer"),
	
	WITH_OFFER("Offer");
	
	
	private String name;
	
	OfferTypeEnum(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}}
