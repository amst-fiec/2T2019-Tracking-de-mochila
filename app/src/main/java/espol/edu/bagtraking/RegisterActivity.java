package espol.edu.bagtraking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db_reference = FirebaseDatabase.getInstance().getReference().child("Aplicacion");

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
               GuardadUsuario(info_user);

                Intent intent = new Intent(getApplicationContext(), OpctionsActivity.class);
                intent.putExtra("info_user", info_user);
                startActivity(intent);
            }
        });
    }

    public void RegistrarUsuario(View view){
        GuardadUsuario(info_user);
    }
    private void cargarDatos(){
        if(info_user!= null){
            nombre.setText(info_user.get("user_name"));
            apellido.setText(info_user.get("user_name"));
            correo.setText(info_user.get("user_email"));
        }


    }
    private void GuardadUsuario(HashMap<String, String> info_user){

        info_user.put("user_pass",pass.getText().toString());
        info_user.put("user_phone",telefono.getText().toString());
        info_user.put("user_iduser",usuario.getText().toString());

        DatabaseReference data = db_reference.child("Usuarios").child(info_user.get("user_id"));
        data.child("user_name").setValue(info_user.get("user_name"));
        data.child("user_email").setValue(info_user.get("user_email"));
        data.child("user_pass").setValue(info_user.get("user_pass"));
        data.child("user_phone").setValue(info_user.get("user_phone"));
        data.child("user_iduser").setValue(info_user.get("user_iduser"));


    }
}
