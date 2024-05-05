package com.txt.eda.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    public static final String DD_MM_YYYY = "dd/MM/yyyy";

    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";


    public static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
        return df.format(date);
    }

    public static Date parseDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr))
            return null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
            return df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr))
            return null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDateTime(Object obj) {
        try {
            if (obj == null)
                return null;
            String dateStr = String.valueOf(obj);
            SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS);
            return df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseToString(Date date, String pattern) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return null;
    }

    public static Integer parseDateToLong(Date bizDate, String pattern) {
        if (bizDate == null) {
            return 0;
        }
        String sTemp = (pattern == null || pattern.length() == 0) ? DD_MM_YYYY
                : pattern.trim();
        java.text.SimpleDateFormat df = new SimpleDateFormat(sTemp);
        return Integer.parseInt(df.format(bizDate));
    }

    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public static int formatDate(String date) {
        try {
            Date d = parseDate(date);
            java.text.SimpleDateFormat df = new SimpleDateFormat(YYYYMMDD);
            return Integer.parseInt(df.format(d));
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        return 0;
    }

    public static String[] getPartsOfDate(String date) {
        if (StringUtils.isNotEmpty(date)) {
            String[] parts = date.split("/");
            return parts;
        }
        return null;

    }

    public static Integer getCustomerAge(Date birthDate, Date trDate) {
        if (birthDate == null)
            return null;
        if (trDate == null)
            return null;
        Calendar current = Calendar.getInstance();
        current.setTime(trDate);
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);
        double age = (current.get(Calendar.YEAR) - birthCalendar
                .get(Calendar.YEAR))
                + (current.get(Calendar.MONTH) - birthCalendar
                .get(Calendar.MONTH))
                * 0.031
                + (current.get(Calendar.DATE) - birthCalendar
                .get(Calendar.DATE)) * 0.001;
        return new Double(Math.floor(age)).intValue();
    }

    public static String getCorrectLASDate(String date) {
        if (StringUtils.equals(date, "99999999")
                || StringUtils.equals(date, "99/99/9999")
                || StringUtils.equals(date, "0/0/0")
                || StringUtils.equals(date, "31/12/9999")) {
            return null;
        }
        return date;
    }

    public static String convertDMYYYYToDDMMYYYY(String date) {
        if (StringUtils.isEmpty(date))
            return StringUtils.EMPTY;
        if (StringUtils.length(date) >= 8) {
            if (date.charAt(1) == '/')
                date = "0" + date;
            if (date.charAt(4) == '/')
                date = date.substring(0, 3) + "0" + date.substring(3);
        }

        return date;
    }

    public static String convertLASDateToDateStr(String dd, String mm,
                                                 String yyyy) {
        String date = dd + "/" + mm + "/" + yyyy;
        date = DateUtil.convertDMYYYYToDDMMYYYY(date);
        return DateUtil.getCorrectLASDate(date);
    }

    public static String convertLASDateToDateStr(String yyyymmdd) {
        if (StringUtils.isEmpty(yyyymmdd)
                || StringUtils.equalsIgnoreCase(yyyymmdd, "NaN"))
            return StringUtils.EMPTY;
        if (StringUtils.length(yyyymmdd) == 8) {
            StringBuilder sb = new StringBuilder();
            sb.append(yyyymmdd.substring(6, 8));
            sb.append("/");
            sb.append(yyyymmdd.substring(4, 6));
            sb.append("/");
            sb.append(yyyymmdd.substring(0, 4));
            return DateUtil.getCorrectLASDate(sb.toString());
        }
        return DateUtil.getCorrectLASDate(yyyymmdd);
    }

    public static String convertLASDateToPcaDate(String yyyymmdd) {
        if (StringUtils.isEmpty(yyyymmdd)
                || StringUtils.equalsIgnoreCase(yyyymmdd, "NaN"))
            return StringUtils.EMPTY;
        if (StringUtils.length(yyyymmdd) == 8) {
            StringBuilder sb = new StringBuilder();
            sb.append(yyyymmdd.substring(0, 4));
            sb.append("-");
            sb.append(yyyymmdd.substring(4, 6));
            sb.append("-");
            sb.append(yyyymmdd.substring(6, 8));
            return DateUtil.getCorrectLASDate(sb.toString());
        }
        return DateUtil.getCorrectLASDate(yyyymmdd);
    }

    public static String convertPvaDateToStandardDate(String date) {
        String[] splitDate = getPartsOfDate(date);
        if (StringUtils.isEmpty(date)
                || StringUtils.equalsIgnoreCase(date, "NaN") || splitDate == null)
            return StringUtils.EMPTY;
        return DateUtil.getCorrectLASDate(Arrays.stream(splitDate).reduce("", (s1, s2) -> s2.concat(s1)));
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) return "";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }


    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static LocalDate fromStrDate(String yyyyMMdd) {
        if (yyyyMMdd == null || yyyyMMdd.trim().isEmpty()) return LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(YYYYMMDD);
        return LocalDate.parse(yyyyMMdd, dtf);

    }

    public static String getCurrentDateTimeTZ() {
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
    }


    public static boolean isBasicDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException dateTimeParseException) {
            return false;
        }
    }

    public static String covertDateVNtoDateTimeTZ(String dateVn, boolean isStartDate) {
        if (isStartDate)
            return LocalDate.parse(dateVn, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .minusDays(1)
                    .atTime(17, 0, 0, 0)
                    .toInstant(ZoneOffset.UTC).toString();
        return LocalDate.parse(dateVn, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .atTime(16, 59, 59, 999)
                .toInstant(ZoneOffset.UTC).toString();
    }
}
