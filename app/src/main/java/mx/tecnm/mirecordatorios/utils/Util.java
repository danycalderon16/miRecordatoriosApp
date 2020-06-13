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
import com.google.android.material.textfield.TextInputLayout;
import android.widget.TimePicker;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;


import java.util.Calendar;
import java.util.Locale;

import mx.tecnm.mirecordatorios.R;
import mx.tecnm.mirecordatorios.activities.MainActivity;
import mx.tecnm.mirecordatorios.services.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;
import static java.util.Calendar.AM_PM;

public class Util {

    private static int dia;
    private static int mes;
    private static int year;

    private static String mesS[] = {"ene","feb","mar","abr","may","jun","jul","ago","sep","oct","nov","dic"};
    private static Calendar c = Calendar.getInstance();

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    public static final int ROJO = 3991;
    public static final int MORADO = 3992;
    public static final int AZUL = 3993;
    public static final int CYAN = 3994;
    public static final int VERDE = 3995;
    public static final int AMARILLO = 3996;
    public static final int NARANJA = 3997;
    public static final int DARK = 3998;

    //Variables para obtener la hora hora
    static int hora = c.get(Calendar.HOUR_OF_DAY);
    static final int minuto = c.get(Calendar.MINUTE);


    public static final int GOOGLE = 2200;
    public static final int FACEBOOK = 2201;
    public static final int EMAIL = 2202;

    public static final int EDITAR = 2205;
    public static final int CREAR = 2206;

    public static void setCustomTheme(Activity activity,int color){
        switch (color){
            case ROJO:
                activity.setTheme(R.style.redTheme);
                break;
            case AMARILLO:
                activity.setTheme(R.style.yellowTheme);
                break;
            case VERDE:
                activity.setTheme(R.style.greenTheme);
                break;
            case AZUL:
                activity.setTheme(R.style.blueTheme);
                break;
            case MORADO:
                activity.setTheme(R.style.purpleTheme);
                break;
            case CYAN:
                activity.setTheme(R.style.cyanTheme);
                break;
            case NARANJA:
                activity.setTheme(R.style.orangeTheme);
                break;
            case DARK:
                activity.setTheme(R.style.darkTheme);
                break;
                default:
                    activity.setTheme(R.style.AppTheme);
        }

    }

    public static boolean validateEditText(TextInputLayout textInputLayout, Context context) {
        String text = textInputLayout.getEditText().getText().toString().trim();
        if (text.isEmpty()) {
            textInputLayout.setError("Campo requerido");
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, 10));
            } else {
                ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(150);
            }
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }


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
        Log.i("####",hora+"- "+ dia);
        String g[] = hora.split(" ");
        String h[] = g[0].split(":");
        String d[] = dia.split("/");
        if (d[0].length()==1)
            d[0] = "0"+d[0];
        if(g[1].equals("p.m.") && Integer.parseInt(h[0])<12) {
            h[0] = (Integer.parseInt(h[0]) + 12) + "";
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

}
