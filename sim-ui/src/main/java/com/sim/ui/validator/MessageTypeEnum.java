package com.sim.ui.validator;

public enum MessageTypeEnum {

	INFO("info.png"), ERROR("error.png"), WARNING("warning.png");

	private String image;

	private MessageTypeEnum(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
