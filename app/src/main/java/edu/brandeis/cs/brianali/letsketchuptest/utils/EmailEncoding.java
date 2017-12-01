package edu.brandeis.cs.brianali.letsketchuptest.utils;

public class EmailEncoding {

    public static String commaEncodePeriod(String email) {
        return email.replace(".", ",");
    }

    public static String commaDecodePeriod(String email) {
        return email.replace(",", ".");
    }
}
