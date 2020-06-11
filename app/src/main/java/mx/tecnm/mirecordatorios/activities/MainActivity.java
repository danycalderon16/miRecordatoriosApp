package mx.tecnm.mirecordatorios.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import mx.tecnm.mirecordatorios.R;
import mx.tecnm.mirecordatorios.adapters.MyAdapterReordatorios;
import mx.tecnm.mirecordatorios.models.Recordatorios;
import mx.tecnm.mirecordatorios.utils.Util;

import static mx.tecnm.mirecordatorios.utils.Util.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseFirestore db  = FirebaseFirestore.getInstance();
    private CollectionReference usersRef  = db.collection("usuarios");

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private GoogleSignInClient mGoogleSignInClient;

    private FloatingActionButton fab;
    private Toolbar toolbar;

    private SharedPreferences preferences;
    private boolean salir = false;

    private int alarmID = 1;
    private int theme;
    private SharedPreferences settings;

    private MyAdapterReordatorios myAdapterReordatorios;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        settings = getSharedPreferences("Notificaciones", Context.MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        theme = preferences.getInt("tema",0);
        setCustomTheme(this,theme);

        setContentView(R.layout.activity_main);

        Map<String, Object> map = new HashMap<>();
        map.put("id",user.getUid());
        map.put("nombre",user.getDisplayName());
        map.put("email",user.getEmail());

        usersRef.document(user.getUid()).set(map);

        sendBind();
        setToolbar();
        setRecyclerViewRecordatorios();
    }

    private void setRecyclerViewRecordatorios() {
        usersRef = db.collection("usuarios").document(user.getUid()).collection("recordatorios");
        Query query = usersRef.orderBy("id", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Recordatorios> options = new FirestoreRecyclerOptions.Builder<Recordatorios>()
                .setQuery(query, Recordatorios.class)
                .build();
        myAdapterReordatorios = new MyAdapterReordatorios(options, this, new MyAdapterReordatorios.OnItemClickListener() {
            @Override
            public void onItemClick(Recordatorios recordatorios, int position) {
                Intent intent = new Intent(MainActivity.this, RecordatorioActivity.class);
                intent.putExtra("dia",recordatorios.getDia());
                intent.putExtra("hora",recordatorios.getHora());
                intent.putExtra("descripcion",recordatorios.getDescripcion());
                intent.putExtra("accion",EDITAR);
                intent.putExtra("importante",recordatorios.isImportante());
                startActivity(intent);
            }
        });
        
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterReordatorios);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myAdapterReordatorios.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapterReordatorios.stopListening();
    }

    private void sendBind(){
        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_recordatorios);

        fab.setOnClickListener(this);
    }

    private void setToolbar(){
        String nombre = preferences.getString("nombre",user.getDisplayName());
        toolbar.setTitle(nombre);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout){
            confirmDialog("¿Desea cerrar sesión?");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logOut(){
        int type = preferences.getInt("type",0);
        mAuth.signOut();
        if(type == FACEBOOK)
            com.facebook.login.LoginManager.getInstance().logOut();
        else
            mGoogleSignInClient.revokeAccess();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                //sendNotification();
                  Intent intent = new Intent(MainActivity.this, RecordatorioActivity.class);
                  intent.putExtra("accion",CREAR);
                  startActivity(intent);
                break;
        }
    }

    private void sendNotification() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String finalHour, finalMinute;

                finalHour = "" + selectedHour;
                finalMinute = "" + selectedMinute;
                if (selectedHour < 10) finalHour = "0" + selectedHour;
                if (selectedMinute < 10) finalMinute = "0" + selectedMinute;
              //notificationsTime.setText(finalHour + ":" + finalMinute);

                Calendar today = Calendar.getInstance();

                today.set(Calendar.HOUR_OF_DAY, selectedHour);
                today.set(Calendar.MINUTE, selectedMinute);
                today.set(Calendar.SECOND, 0);

                SharedPreferences.Editor edit = settings.edit();
                edit.putString("hour", finalHour);
                edit.putString("minute", finalMinute);

                //SAVE ALARM TIME TO USE IT IN CASE OF REBOOT
                edit.putInt("alarmID", alarmID);
                edit.putLong("alarmTime", today.getTimeInMillis());

                edit.commit();

                Toast.makeText(MainActivity.this, getString(R.string.changed_to, finalHour + ":" + finalMinute), Toast.LENGTH_LONG).show();

                Util.setAlarm(alarmID, today.getTimeInMillis(), MainActivity.this);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.show();
    }

    private void confirmDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       logOut();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
