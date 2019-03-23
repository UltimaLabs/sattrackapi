package com.ultimalabs.sattrackapi.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Rounding of double values
 */
public class DoubleRound {

    /**
     * Round a double value to a number of decimal places
     *
     * @param value  value that needs to be rounded
     * @param places number of decimal places
     * @return rounded value
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
