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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static mx.tecnm.mirecordatorios.utils.Util.*;

import mx.tecnm.mirecordatorios.R;

public class LoginEmailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef  = db.collection("usuarios");

    private ImageView imageView;
    private SharedPreferences preferences;

    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPass;

    private TextInputEditText editEmail;
    private TextInputEditText editPass;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        mAuth = FirebaseAuth.getInstance();

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        imageView = findViewById(R.id.imgBack_reg);
        layoutEmail = findViewById(R.id.layout_email_reg);
        layoutPass = findViewById(R.id.layout_pass_reg);

        editEmail = findViewById(R.id.et_emal_reg);
        editPass = findViewById(R.id.et_pass_reg);

        button = findViewById(R.id.btn_entrar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEditText(layoutEmail,LoginEmailActivity.this) &&
                        validateEditText(layoutEmail,LoginEmailActivity.this))
                    ingresar();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginEmailActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void ingresar() {
        String email = editEmail.getText().toString();
        String password = editPass.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("####", "signInWithEmail:success");
                            guardarNombre(mAuth.getCurrentUser().getUid());
                            ingresado();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("####", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginEmailActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void guardarNombre(String uid) {
        usersRef.document(uid)
                .get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nombre",documentSnapshot.getString("nombre"));
                editor.apply();
            }
        });
    }

    public void ingresado(){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginEmailActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
