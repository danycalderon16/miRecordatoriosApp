package mx.tecnm.mirecordatorios.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import static mx.tecnm.mirecordatorios.utils.Util.*;

import mx.tecnm.mirecordatorios.R;

public class RecordatorioActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db  = FirebaseFirestore.getInstance();
    private CollectionReference usersRef  = db.collection("usuarios");

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private GoogleSignInClient mGoogleSignInClient;

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private TextView textViewHora;
    private TextView textViewDia;
    private EditText editTextDesc;
    private ImageView imageViewTime;
    private ImageView imageViewDate;

    private SharedPreferences preferences;

    private int accion;
    private String hora;
    private String dia;
    private String descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        sendBind();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            accion = bundle.getInt("accion");
            if(accion == EDITAR){
                hora = bundle.getString("hora");
                dia = bundle.getString("dia");
                descripcion = bundle.getString("descripcion");
            }
        }
        setToolbar();
        setData();
    }

    private void setData() {
        if(accion == EDITAR){
            textViewDia.setText(dia);
            textViewHora.setText(hora);
            editTextDesc.setText(descripcion);
        }else {
            setTime(textViewHora);
            setDate(textViewDia);
        }
    }

    private void sendBind() {
        toolbar = findViewById(R.id.toolbarR);
        textViewDia = findViewById(R.id.textDia);
        textViewHora = findViewById(R.id.textHora);
        editTextDesc = findViewById(R.id.textDesc);
        fab = findViewById(R.id.fab_save);
        imageViewDate = findViewById(R.id.dateImage);
        imageViewTime = findViewById(R.id.timeImage);

        fab.setOnClickListener(this);
        textViewHora.setOnClickListener(this);
        textViewDia.setOnClickListener(this);
        imageViewDate.setOnClickListener(this);
        imageViewTime.setOnClickListener(this);
    }

    private void setToolbar(){
        toolbar.setTitle(user.getDisplayName());
        if(accion == EDITAR)
            toolbar.setTitle("Editar");
        else
            toolbar.setTitle("Crear");
        setSupportActionBar(toolbar);
    }

    private void crearRecordatorio() {

        String texto = editTextDesc.getText().toString();

        if(texto.isEmpty()){
            Toast.makeText(this, "Añada una descripion por favor", Toast.LENGTH_SHORT).show();
            return;
        }

        String hora  = textViewHora.getText().toString();
        String dia = textViewDia.getText().toString();

        Map<String, Object> mapR = new HashMap<>();
        mapR.put("id",generateToken(dia, hora));
        mapR.put("descripcion",texto);
        mapR.put("hora",hora);
        mapR.put("dia", dia);

        db.collection("usuarios")
                .document(user.getUid())
                .collection("recordatorios")
                .add(mapR)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("////////////","Agregado Corretamente");
                        Toast.makeText(RecordatorioActivity.this, "Recordatorio Agregado", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("%%%%%%%%%%",e.getMessage());
                        Toast.makeText(RecordatorioActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                goMain(this);
                return true;
                case R.id.item_delete:
                goMain(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_save:
                crearRecordatorio();
                break;
            case R.id.textHora:
            case R.id.timeImage:
                getTime(RecordatorioActivity.this, textViewHora);
                break;
            case R.id.textDia:
            case R.id.dateImage:
                getDate(RecordatorioActivity.this, textViewDia);
                break;

        }
    }
}
