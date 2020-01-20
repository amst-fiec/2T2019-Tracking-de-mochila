package espol.edu.bagtraking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import espol.edu.bagtraking.R;

public class RegisterActivity extends AppCompatActivity {

    Button back;
    Button register;
    HashMap<String, String> info_user;
    EditText nombre;
    EditText apellido;
    EditText usuario;
    EditText pass;
    EditText confirmPass;
    EditText correo;
    EditText telefono;
    DatabaseReference db_reference;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db_reference = FirebaseDatabase.getInstance().getReference().child("Aplicacion");
        mAuth = FirebaseAuth.getInstance();

        back=findViewById(R.id.anterior);
        register =  findViewById(R.id.btn_register);
        nombre = findViewById(R.id.txt_nombre);
        apellido = findViewById(R.id.txt_apellido);
        usuario = findViewById(R.id.txt_usuario);
        pass = findViewById(R.id.txt_pass);
        confirmPass = findViewById(R.id.txt_confirmpass);
        correo = findViewById(R.id.txt_correo);
        telefono = findViewById(R.id.txt_telefono);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        info_user = (HashMap<String, String>)intent.getSerializableExtra("info_user");
        cargarDatos();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(correo.getText().toString(),pass.getText().toString());

            }
        });
    }


    private void cargarDatos(){
        if(info_user!= null){
            nombre.setText(info_user.get("user_name"));
            apellido.setText(info_user.get("user_name"));
            correo.setText(info_user.get("user_email"));
            telefono.setText(info_user.get("user_phone"));
        }


    }
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            GuardadUsuario(info_user,user);
                            Intent intent = new Intent(getApplicationContext(), perfil_usuario_1.class);
                            intent.putExtra("info_user", info_user);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed."+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = this.correo.getText().toString();
        if (TextUtils.isEmpty(email)) {
            this.correo.setError("Required.");
            valid = false;
        } else {
            this.correo.setError(null);
        }

        String password = this.pass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pass.setError("Required.");
            valid = false;
        } else {
            pass.setError(null);
        }

        return valid;
    }
    private void GuardadUsuario(HashMap<String, String> info_user,FirebaseUser user){

        if(info_user==null){
            info_user = new HashMap<>();
        }
        info_user.put("user_phone",telefono.getText().toString());
        info_user.put("user_iduser",usuario.getText().toString());
        info_user.put("user_name", nombre.getText().toString());
        info_user.put("user_email", correo.getText().toString());
        info_user.put("user_photo", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/User_icon_2.svg/1200px-User_icon_2.svg.png");
        info_user.put("user_id", user.getUid());
        info_user.put("user_phone", telefono.getText().toString());

        DatabaseReference data = db_reference.child("Usuarios").child(info_user.get("user_id"));
        data.child("user_name").setValue(info_user.get("user_name"));
        data.child("user_email").setValue(info_user.get("user_email"));
        data.child("user_phone").setValue(info_user.get("user_phone"));
        data.child("user_iduser").setValue(info_user.get("user_iduser"));
        data.child("user_photo").setValue(info_user.get("user_photo"));




    }
    public static String randomAlphaNumeric(int count) {

        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {

            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));

        }

        return builder.toString();

    }
}
