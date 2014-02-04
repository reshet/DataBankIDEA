package com.mresearch.databank.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 1/25/14
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDateFormat {
    public static void main(String [] args){
        Date dt = new Date();
        System.out.println(dt);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String date=sdf.format(dt);
        System.out.println(date);
        System.out.println(date.substring(0,4));
        System.out.println(date.substring(4,6));


    }
}
