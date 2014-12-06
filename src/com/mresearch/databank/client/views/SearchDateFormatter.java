package com.mresearch.databank.client.views;

import com.google.gwt.i18n.shared.DateTimeFormat;

import java.util.Date;

public class SearchDateFormatter {
    public static DateTimeFormat SEARCH_INPUT_FORMAT = DateTimeFormat.getFormat("yyyyMMdd");
    public static DateTimeFormat SEARCH_OUTPUT_FORMAT = DateTimeFormat.getFormat("dd.MM.yyyy");

    public static String formatDate(String date) {
      Date dateValue = SEARCH_INPUT_FORMAT.parse(date);
      return SEARCH_OUTPUT_FORMAT.format(dateValue);
    }
}