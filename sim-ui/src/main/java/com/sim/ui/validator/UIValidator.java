package com.sim.ui.validator;

import org.springframework.stereotype.Component;

import com.sim.common.util.StringUtils;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

@Component
public class UIValidator {

	private HBox messagePanel;

	public UIValidator() {
		// TODO Auto-generated constructor stub
	}

	public UIValidator(HBox messageBox) {
		this.messagePanel = messageBox;
	}

	public HBox getMessagePanel() {
		return messagePanel;
	}

	public void setMessagePanel(HBox messagePanel) {
		this.messagePanel = messagePanel;
	}

	public static boolean validateEmptyTextField(TextInputControl textField) {
		if (textField == null || textField.getText() == null || textField.getText().isEmpty()) {
			return true;
		}
		return false;
	}

	public void showMessage(String message, MessageTypeEnum messageType) {
		if(messagePanel.getChildren() != null) {
			for (Node child : messagePanel.getChildren()) {
				if(child.getClass().getName().equals( ImageView.class.getName())) {
					((ImageView) child).setImage(new Image("images/" + messageType.getImage()));
				}else if(child.getClass().getName().equals(Label.class.getTypeName())) {
					((Label) child).setText(message);
					break;
				}
			}
		}
		messagePanel.setVisible(true);
	}

	public void hidePanel() {
		messagePanel.setVisible(false);
	}

	public static boolean isNumeric(String value) {
		return StringUtils.isNumeric(value);
	}

	public static boolean isGreaterThan(String lessValue, String greaterValue) {
		return Double.valueOf(lessValue).compareTo(Double.valueOf(greaterValue)) == 1;
	}
	public static boolean isLessThan(String lessValue, String greaterValue) {
		return Double.valueOf(lessValue).compareTo(Double.valueOf(greaterValue)) == -1;
	}

}
