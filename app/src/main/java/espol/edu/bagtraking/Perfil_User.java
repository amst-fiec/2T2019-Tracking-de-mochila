package espol.edu.bagtraking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Perfil_User extends AppCompatActivity {
    private ListView lista;
    private ArrayList<String>nombre;
    private EditText editText;
    private Button button;
    TextView  txt_name, txt_email;
    ImageView imv_photo;
    Button btn_logout;
    //private ArrayAdapter ADP;
    private ArrayList<String> arrayList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil__user);
        nombre=new ArrayList<>();
        editText=findViewById(R.id.nombre_mochila);
        button=findViewById(R.id.registrar_mochila);
        lista=findViewById(R.id.lista_mochila);
        //ADP.notifyDataSetChanged();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty())
                {
                    if (nombre.contains(editText.getText().toString()))
                    {
                        Toast.makeText(getApplicationContext(),"Ya se encuentra registrado",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        nombre.add(editText.getText().toString());
                        ArrayAdapter adapter = new ArrayAdapter<String>(Perfil_User.this, R.layout.list_item, nombre);
                        lista.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

        });



        Intent intent = getIntent();
        HashMap<String, String> info_user = (HashMap<String, String>)intent.getSerializableExtra("info_user");


        txt_name = findViewById(R.id.editText);
        txt_email = findViewById(R.id.correo);
        imv_photo = findViewById(R.id.foto_perfil);

        //txt_id.setText(info_user.get("user_id"));
        txt_name.setText(info_user.get("user_name"));
        txt_email.setText(info_user.get("user_email"));
        String photo = info_user.get("user_photo");
        Picasso.with(getApplicationContext()).load(photo).into(imv_photo);
    }
    public void cerrarSesion(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("msg", "cerrarSesion");
        startActivity(intent);
    }
}
