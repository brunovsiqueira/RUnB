package br.unb.runb.util;

import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


public class FormatterDate {

    public static String getDayOfWeekInPortuguese(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        return dayOfWeekMap(dayOfWeek);

    }

    public static String convertDateToString(Date date){

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String reportDate = df.format(date);

        return reportDate;
    }

    public static String getTimezone() {

        Calendar date = Calendar.getInstance();
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat sdf = new SimpleDateFormat("Z");
        return sdf.format(date.getTime());

    }

    public static String convertToAmerican(Calendar date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date.getTime());

    }

    public static String formatToShow(Calendar date) {


        final String OLD_FORMAT = "yyyy-MM-dd";
        final String NEW_FORMAT = "EEE, d MMM";

        // August 12, 2010
        String oldDateString = convertToAmerican(date);
        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = null;
        try {
            d = sdf.parse(oldDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);

        return  FormatterString.capitalizeSentences(newDateString);/*weekDay + ", " + part1 + "/" + part2 + "/" + part3;*/
    }

    @SuppressLint("WrongConstant")
    public static String getWeekDay(Date date) {
        String weekDay = "";
        Calendar c = Calendar.getInstance();
        c.setTime(date); // yourdate is an object of type Date

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case 1:
                weekDay = "Dom";
                break;
            case 2:
                weekDay = "Seg";
                break;
            case 3:
                weekDay = "Ter";
                break;
            case 4:
                weekDay = "Qua";
                break;
            case 5:
                weekDay = "Qui";
                break;
            case 6:
                weekDay = "Sex";
                break;
            case 7:
                weekDay = "Sáb";
                break;
            default:
                break;
        }

        return weekDay;
    }

    public static String convertDateToStringWithoutDate(Date date){

        DateFormat df = new SimpleDateFormat("HH:mm");

        String reportDate = df.format(date);

        return reportDate;
    }

    public static String convertDateToStringWithoutHour(Date date){

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String reportDate = df.format(date);

        return reportDate;
    }

    public static String getDayOfMonth (String date) {
        String dayOfMonth = date.substring(0, 2);

        return dayOfMonth;
    }

    public static String getMonthDay (String date) {
        String monthDay = date.substring(3, 5);

        return monthDay;
    }

    public static String getDayAndMonth(String date) {
        String monthDay = date.substring(0, 5);

        return monthDay;
    }

    public static String getYear(String date) {
        String monthDay = date.substring(6, 10);

        return monthDay;
    }

    public static String getHour(String date) {
        String monthDay = date.substring(11, 16);

        return monthDay;
    }

    public static String getOnlyHour(String date) {
        String monthDay = date.substring(11, 13);

        return monthDay;
    }

    public static String getMinute(String date) {
        String monthDay = date.substring(14, 16);

        return monthDay;
    }

    public static Date convertDateTimeToDate(String oldDateString) {

        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = sdf.parse(oldDateString);
        } catch (ParseException e) {
            // handle exception here !
            e.printStackTrace();
        }

        String newFormatString = "dd/MM/yyyy";

        SimpleDateFormat newFormatter = new SimpleDateFormat(newFormatString);
//        newFormatter.setTimeZone(TimeZone.getDefault());

        return date;
    }

    public static Date getNextSunday(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            Calendar cal1 = Calendar.getInstance();
            // Here you will set specified date if provided
            if(date != null)
                c.setTime(date);
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            // Here we will set current date
            cal1.setTime(Calendar.getInstance().getTime());
            //here we will check if the sunday has passed or not, if yes then will add 7 that will give coming Sunday
//            if(c.get(Calendar.DAY_OF_MONTH) < cal1.get(Calendar.DAY_OF_MONTH)) {
//                c.add(Calendar.DATE, 7);
//            }
            c.add(Calendar.DATE, 7);
            //This will print the coming Sunday date if Sunday has passes otherwise it will print current Sunday
            System.out.println("Date: "+c.getTime());
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getNextMonday(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            Calendar cal1 = Calendar.getInstance();
            // Here you will set specified date if provided
            if(date != null)
                c.setTime(date);
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            // Here we will set current date
            cal1.setTime(Calendar.getInstance().getTime());
            //here we will check if the sunday has passed or not, if yes then will add 7 that will give coming Sunday
//            if(c.get(Calendar.DAY_OF_MONTH) < cal1.get(Calendar.DAY_OF_MONTH)) {
//                c.add(Calendar.DATE, 7);
//            }
            c.add(Calendar.DATE, 7);
            //This will print the coming Sunday date if Sunday has passes otherwise it will print current Sunday
            System.out.println("Date: "+c.getTime());
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDateToRequest(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(date.getTime());

        return formattedDate;
    }

    public static Date getLastDateOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static String formatDateToOrderList(String string) {
        String month = monthMap(Integer.valueOf(string.substring(0,2)), false);
        month = Character.toUpperCase(month.charAt(0)) + month.substring(1);

        String year = string.substring(3,7);

        return month + " • " + year;
    }

    public static String monthMap (int day, boolean isUpperCase) {

        HashMap<Integer,String> hm = new HashMap<Integer, String>();

        if (isUpperCase) {

            hm.put(1, "JAN");
            hm.put(2, "FEV");
            hm.put(3, "MAR");
            hm.put(4, "ABRIL");
            hm.put(5, "MAI");
            hm.put(6, "JUN");
            hm.put(7, "JUL");
            hm.put(8, "AGO");
            hm.put(9, "SET");
            hm.put(10, "OUT");
            hm.put(11, "NOV");
            hm.put(12, "DEZ");

        } else {

            hm.put(1, "janeiro");
            hm.put(2, "fevereiro");
            hm.put(3, "março");
            hm.put(4, "abril");
            hm.put(5, "maio");
            hm.put(6, "junho");
            hm.put(7, "julho");
            hm.put(8, "agosto");
            hm.put(9, "setembro");
            hm.put(10, "outubro");
            hm.put(11, "novembro");
            hm.put(12, "dezembro");

        }


        BigDecimal myBigDecimal = new BigDecimal(day);

        String message = (String) hm.get(myBigDecimal.intValue());

        return message;
    }

    public static String dayOfWeekMap (int day) {

        HashMap<Integer,String> hm = new HashMap<Integer, String>();

        hm.put(1, "Domingo");
        hm.put(2, "Segunda");
        hm.put(3, "Terça");
        hm.put(4, "Quarta");
        hm.put(5, "Quinta");
        hm.put(6, "Sexta");
        hm.put(7, "Sábado");

        BigDecimal myBigDecimal = new BigDecimal(day);

        String message = (String) hm.get(myBigDecimal.intValue());

        return message;
    }

}
