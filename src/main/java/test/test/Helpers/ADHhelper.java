/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Helpers;

import static java.lang.System.out;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Months;

/**
 *
 * @author user
 */
public class ADHhelper {
    public static String hasilTambahBulanString(int bulanAwalParam, int tambahParam) {
        String bulanAwal = ADHhelper.bulan(bulanAwalParam);
        int bulanAkhirRaw = ADHhelper.tambahBulanInteger(bulanAwalParam, tambahParam - 1);
        String bulanAkhir = ADHhelper.bulan(bulanAkhirRaw);
        if (tambahParam > 1) {
            return bulanAwal + " - " + bulanAkhir;
        } else {
            return bulanAwal;
        }
    }
    
    public static int tambahBulanInteger(int bulanAwal, int tambah) {
        int hasil = bulanAwal;
        
        for (int i = 1; i <= tambah; i++) {
            hasil++;
            if (hasil > 12) {
                hasil = 1;
            }
        }
        
        return hasil;
    }

    public static Calendar dateToCalendar(Date tanggalParam) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(tanggalParam);
        
        return cal;
    }

    public static Date dateTambahBulan(Date tanggalParam, int bulan) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(tanggalParam);
        cal.add(Calendar.MONTH, bulan);
        
        return cal.getTime();
    }
    
    public static int dateMonthBetween(Date tanggalParam1, Date tanggalParam2) {
        DateTime dt1 = new DateTime(tanggalParam1);
        DateTime dt2 = new DateTime(tanggalParam2);
        
        return Months.monthsBetween(dt1, dt2).getMonths();
    }
    
    public static int dateGetDay(Date tanggalParam) {
        Calendar cal = dateToCalendar(tanggalParam);
        return cal.get(Calendar.DATE);
    }

    public static int dateGetMonth(Date tanggalParam) {
        Calendar cal = dateToCalendar(tanggalParam);
        return cal.get(Calendar.MONTH);
    }

    public static int dateGetYear(Date tanggalParam) {
        Calendar cal = dateToCalendar(tanggalParam);
        return cal.get(Calendar.YEAR);
    }
    
    public static void d(String string, boolean pembatas) {
        if (!pembatas) {
            out.println(string);
        } else {
            d(string);
        }
    }
    
    public static Date getTanggalFromDB(String tanggalParam) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(tanggalParam);
    }

    public static String parseTanggal(Date tanggalParam) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(tanggalParam);
    }
    
    public static String tanggalIndo(String tanggalParam) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date tanggal = format.parse(tanggalParam);

        SimpleDateFormat parsedFormat = new SimpleDateFormat("dd-MM-YYYY");
        String parsedtanggal = parsedFormat.format(tanggal);
        
        return parsedtanggal;
    }
    
    public static String rupiah(int uang) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        
        return kursIndonesia.format(uang);
    }
    public static void d(String string) {
        out.println("===========================================");
        out.println(string);
        out.println("===========================================");
    }
    
    public static String bulan(int i) {
        switch(i) {
            case 1:
              return "Januari";
            case 2:
              return "Februari";
            case 3:
              return "Maret";
            case 4:
              return "April";
            case 5:
              return "Mei";
            case 6:
              return "Juni";
            case 7:
              return "Juli";
            case 8:
              return "Agustus";
            case 9:
              return "September";
            case 10:
              return "Oktober";
            case 11:
              return "November";
            case 12:
              return "Desember";
            default:
              return "ERROR !!!";
          }
    }
}
