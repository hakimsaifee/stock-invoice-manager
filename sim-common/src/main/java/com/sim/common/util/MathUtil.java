package com.sim.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {

	public static double preciseDouble(double value, int preciseScale) {
		return new BigDecimal(String.valueOf(value)).setScale(preciseScale, RoundingMode.HALF_UP).doubleValue();
	}
}
