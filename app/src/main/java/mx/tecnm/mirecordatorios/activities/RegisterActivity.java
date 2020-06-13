package mx.tecnm.mirecordatorios.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import mx.tecnm.mirecordatorios.R;
import static mx.tecnm.mirecordatorios.utils.Util.*;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef  = db.collection("usuarios");

    private SharedPreferences preferences;

    private ImageView imageView;
    private TextInputLayout layoutNombre;
    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPass;

    private TextInputEditText editNombre;
    private TextInputEditText editEmail;
    private TextInputEditText editPass;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_register);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        imageView = findViewById(R.id.imgBack_reg);
        layoutNombre = findViewById(R.id.layout_nombre);
        layoutEmail = findViewById(R.id.layout_email_reg);
        layoutPass = findViewById(R.id.layout_pass_reg);

        editNombre = findViewById(R.id.et_nombre);
        editEmail = findViewById(R.id.et_emal_reg);
        editPass = findViewById(R.id.et_pass_reg);

        button = findViewById(R.id.btn_entrar_reg);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEditText(layoutNombre, RegisterActivity.this) &&
                        validateEditText(layoutPass, RegisterActivity.this) &&
                                validateEditText(layoutEmail, RegisterActivity.this))
                    registrar();

            }
        });
    }

    private void registrar() {
        String email = editEmail.getText().toString();
        String password = editPass.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("####", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            añadirUsusario(user.getUid());
                            saveLoginPreferences(EMAIL);
                            ingresado();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("####", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void ingresado(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void añadirUsusario(String uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("id",uid);
        map.put("nombre",editNombre.getText().toString());
        map.put("email",editEmail.getText().toString());

        usersRef.document(uid).set(map);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void saveLoginPreferences(int type) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("type",type);
        editor.putString("nombre",editNombre.getText().toString());
        editor.apply();
    }
}
