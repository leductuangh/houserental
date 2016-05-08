package com.example.houserental.function;

import com.example.houserental.R;

import core.util.Utils;

/**
 * Created by leductuan on 5/8/16.
 */
public class HouseRentalUtils extends Utils {

    public static String toThousandVND(int value) {
        return String.format(HouseRentalApplication.getContext().getString(R.string.common_money_thousand), value);
    }
}
