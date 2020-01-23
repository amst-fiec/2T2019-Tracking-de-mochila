package espol.edu.bagtraking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import espol.edu.bagtraking.Modelo.Metodos;
import espol.edu.bagtraking.Modelo.Variables;
import espol.edu.bagtraking.R;

public class Splash_Screen extends AppCompatActivity {


    private static final String TAG = "Error";
    private HashMap<String, String> info_user;
    private FirebaseAuth mAuth;
    private DatabaseReference db_reference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash__screen);
        Variables.HAY_INTERNET = Metodos.isOnline();



        if (Variables.HAY_INTERNET) {
            this.mAuth =FirebaseAuth.getInstance();
            this.db_reference = FirebaseDatabase.getInstance().getReference().child("Aplicacion");
            this.currentUser = mAuth.getCurrentUser();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null) {
                        Intent intent = new Intent(Splash_Screen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();


                    } else {

                        getUser(currentUser);
                        StartThread();
                    }
                }
            }, 3000);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splash_Screen.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(Splash_Screen.this, "Conexion Perdida", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, 3000);
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();

    }

    public void StartThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (info_user.isEmpty()) {
                    System.out.println("Esperando Datos...");
                }
                //System.out.println(info_user);
                Intent intent = new Intent(getApplicationContext(), perfil_usuario_1.class);
                intent.putExtra("info_user", info_user);
                startActivity(intent);
                finish();
            }
        };

        thread.start();
    }


    public void getUser(FirebaseUser user){
        HashMap<String, String> info_user = new HashMap<String, String>();
        db_reference.child("Usuarios").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                info_user.put("user_name", String.valueOf(dataSnapshot.child("user_name").getValue()));
                info_user.put("user_email", String.valueOf(dataSnapshot.child("user_email").getValue()));
                info_user.put("user_photo", String.valueOf(dataSnapshot.child("user_photo").getValue()));
                info_user.put("user_phone", String.valueOf(dataSnapshot.child("user_phone").getValue()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
        this.info_user = info_user;
    }
}
