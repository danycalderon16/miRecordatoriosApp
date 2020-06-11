package mx.tecnm.mirecordatorios.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import mx.tecnm.mirecordatorios.R;

import static mx.tecnm.mirecordatorios.utils.Util.*;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Toolbar toolbar;

    private ImageView imageViewUser;
    private ImageView imageViewEdit;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private CardView cardViewRojo;
    private CardView cardViewAmarillo;
    private CardView cardViewVerde;
    private CardView cardViewAzul;
    private CardView cardViewNaranja;
    private CardView cardViewDark;
    private CardView cardViewCyan;
    private CardView cardViewMorado;

    private SharedPreferences preferences;
    private int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        theme = preferences.getInt("tema",0);
        setCustomTheme(this,theme);
        setContentView(R.layout.activity_user);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setBind();
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setBind() {
        editText = findViewById(R.id.editTextNombre);
        imageViewUser = findViewById(R.id.imageUser);
        imageViewEdit = findViewById(R.id.imageViewEdit);
        toolbar = findViewById(R.id.toolbarR2);

        cardViewAmarillo = findViewById(R.id.amarillo);
        cardViewVerde = findViewById(R.id.verde);
        cardViewRojo = findViewById(R.id.rojo);
        cardViewAzul = findViewById(R.id.azul);

        cardViewRojo.setOnClickListener(this);
        cardViewVerde.setOnClickListener(this);
        cardViewAzul.setOnClickListener(this);
        cardViewAmarillo.setOnClickListener(this);

        cardViewCyan = findViewById(R.id.cyan);
        cardViewDark = findViewById(R.id.dark);
        cardViewMorado = findViewById(R.id.morado);
        cardViewNaranja = findViewById(R.id.naranja);

        cardViewCyan.setOnClickListener(this);
        cardViewDark.setOnClickListener(this);
        cardViewMorado.setOnClickListener(this);
        cardViewNaranja.setOnClickListener(this);

        String nombre = preferences.getString("nombre",user.getDisplayName());
        editText.setText(nombre);
        Picasso.get().load(user.getPhotoUrl()).into(imageViewUser);

        imageViewEdit.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            goMain(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = preferences.edit();
        Intent i=new Intent(this,MainActivity.class);
        switch (v.getId()){
            case R.id.imageViewEdit:
                editor.putString("nombre",editText.getText().toString());
                Toast.makeText(UserActivity.this, "Nombre Cambiado", Toast.LENGTH_SHORT).show();
                hideKeyboard(this);
                break;
            case R.id.verde:
                editor.putInt("tema",VERDE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.rojo:
                editor.putInt("tema",ROJO);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.azul:
                editor.putInt("tema",AZUL);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.amarillo:
                editor.putInt("tema",AMARILLO);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.cyan:
                editor.putInt("tema",CYAN);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.morado:
                editor.putInt("tema",MORADO);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.dark:
                editor.putInt("tema",DARK);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.naranja:
                editor.putInt("tema",NARANJA);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
        editor.apply();
    }
}
