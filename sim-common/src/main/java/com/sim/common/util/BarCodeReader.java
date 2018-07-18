package com.sim.common.util;

public class BarCodeReader {

	public static String readBarcode(String code) {
		try {
			if (code != null && !code.isEmpty()) {
				if(code.endsWith("\t")) {
					System.out.println("tab");
				}
				String barCode = code.substring(1, code.length());
				System.out.println(barCode);
				return barCode;
			}
		} catch (Exception e) {
		}
		return null;

	}

	public static boolean isReadByBarcodeScanner(String barcode) {
		if (barcode != null && !barcode.isEmpty() && StringUtils.startWithCharacter(barcode)) {
			return true;
		}
		return false;
	}
}
