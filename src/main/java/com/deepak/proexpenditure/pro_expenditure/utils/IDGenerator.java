package com.deepak.proexpenditure.pro_expenditure.utils;


import java.util.UUID;

public class IDGenerator {

    public static String generateId(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

}
