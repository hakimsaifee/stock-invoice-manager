package com.sim.ui.controller;

import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyEvents {

	public static boolean isEnterKeyPressed(Event event) {
		if (isKeyTyped(event)) {
			return isEnterKeyEvent(event);
		} else {
			return true;
		}
	}

	public static boolean isKeyTyped(Event event) {
		if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
			return true;
		}
		return false;
	}

	public static boolean isEnterKeyEvent(Event event) {
		if (((KeyEvent) event).getCode().equals(KeyCode.ENTER)) {
			return true;
		}
		return false;
	}
}
