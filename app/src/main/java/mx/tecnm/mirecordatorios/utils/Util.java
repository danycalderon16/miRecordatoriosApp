package mx.tecnm.mirecordatorios.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

import mx.tecnm.mirecordatorios.R;
import mx.tecnm.mirecordatorios.activities.MainActivity;
import mx.tecnm.mirecordatorios.services.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;
import static java.util.Calendar.AM_PM;

public class Util {

    private static int dia;
    private static int mes;
    private static int year;

    private static String mesS[] = {"ene","feb","mar","abr","may","jun","jul","ago","sep","oct","nov","dic"};
    private static Calendar c = Calendar.getInstance();

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";


    //Variables para obtener la hora hora
    static int hora = c.get(Calendar.HOUR_OF_DAY);
    static final int minuto = c.get(Calendar.MINUTE);


    public static final int GOOGLE = 2200;
    public static final int FACEBOOK = 2201;


    public static final int EDITAR = 2205;
    public static final int CREAR = 2206;

    public static void goMain(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void setDate(TextView textView) {
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        textView.setText(String.format(Locale.getDefault(),"%d/%s/%d",dia, mesS[mes] , year));
    }

    public static void getDate(Context context, final TextView textView){if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int motnhOfYear, int dayOfMonth) {
                textView.setText(String.format(Locale.getDefault(),"%d/%s/%d",dayOfMonth, mesS[motnhOfYear] , year));
            }
        }, year, mes, dia);
        datePickerDialog.show();
    }}

    public static void setTime(TextView textView){
        String AM_PM;
        if(hora < 12) {
            AM_PM = "a.m.";
        } else {
            hora = hora-12;
            AM_PM = "p.m.";
        }
        String horaFormateada =  (hora < 10)? String.valueOf(CERO + hora) : String.valueOf(hora);
        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
        String minutoFormateado = (minuto < 10)? String.valueOf(CERO + minuto):String.valueOf(minuto);
        //Muestro la hora con el formato deseado
        textView.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
    }

    public static  void getTime(Context context, final TextView textView){
        TimePickerDialog recogerHora = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM;
                if(hourOfDay < 12)
                    AM_PM = "a.m.";
                else
                    AM_PM = "p.m.";
                if(hourOfDay>12)
                    hourOfDay = hourOfDay-12;
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Muestro la hora con el formato deseado
                textView.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }

    public static void setAlarm(int i, Long timestamp, Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }

    public static String generateToken(String dia, String hora) {
        String g[] = hora.split(" ");
        String h[] = g[0].split(":");
        String d[] = dia.split("/");
        if (d[0].length()==1)
            d[0] = "0"+d[0];
        if(g[1].equals("p.m.") && Integer.parseInt(h[0])<12) {
            Log.i("####","a-"+h[0]);
            h[0] = (Integer.parseInt(h[0]) + 12) + "";
            Log.i("####","b-"+h[0]);
        }
        String token = (d[2])+""+mesNumrero(d[1])+""+d[0]+""+h[0]+""+h[1];

        return (token);
    }

    private static String mesNumrero(String mes) {
        String c ="";
        switch (mes) {
            case "ene":
                c ="01";
                break;
            case "feb":
                c= "02";
                break;
            case "mar":
                c= "03";
                break;
            case "abr":
                c= "04";
                break;
            case "may":
                c= "05";
                break;
            case "jun":
                c= "06";
                break;
            case "jul":
                c= "07";
                break;
            case "ago":
                c= "08";
                break;
            case "sep":
                c= "09";
                break;
            case "oct":
                c= "10";
                break;
            case "nov":
                c= "11";
                break;
            case "dic":
                c= "12";
                break;
            default:
                return "00";
        }
        return  c;
    }


    public static int getRandomColor(){
        int j =  (int) (Math.random()* 10) + 1;
        int color = R.color.colorPrimary;

        switch (j){
            case 1:color = R.color.red_primary;
                break;
            case 2:color = R.color.blue_primary;
                break;
            case 3:color = R.color.green_primary;
                break;
            case 4:color = R.color.yellow_primary;
                break;
            case 5:color = R.color.black;
                break;
            case 6:color = R.color.orange_primary;
                break;
            case 7:color = R.color.indigo_primary;
                break;
            case 8:color = R.color.pink_primary;
                break;
            case 9:color = R.color.brown_primary;
                break;
            case 10:color = R.color.yellow_primary;
                break;

        }

        return color;
    }
}
