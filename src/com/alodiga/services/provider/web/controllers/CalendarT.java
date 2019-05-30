/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.alodiga.services.provider.web.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalendarT {

        public static Calendar addDays(Date date, int daysNumber) {

         Calendar calendar = Calendar.getInstance();
          
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.add(Calendar.DATE, +daysNumber);









        return calendar;
    }

}
