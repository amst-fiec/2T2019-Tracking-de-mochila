package espol.edu.bagtraking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Splash_Screen extends AppCompatActivity {
    FirebaseAuth mAuth;
    private DatabaseReference db_reference;
    private HashMap<String, String> info_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash__screen);

        mAuth = FirebaseAuth.getInstance();
        db_reference = FirebaseDatabase.getInstance().getReference().child("Aplicacion");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser==null){
                    Intent intent=new Intent(Splash_Screen.this,LoginActivity.class);
                    startActivity(intent);
                    finish();


                }
                else {
                    getUser(currentUser);
                    StartThread();
                }
            }
        },3000) ;

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
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
    public void StartThread(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                while(info_user.isEmpty()){System.out.println(info_user);}
                Intent intent = new Intent(getApplicationContext(), perfil_usuario_1.class);
                intent.putExtra("info_user", info_user);
                startActivity(intent);
                finish();
            }
        };

        thread.start();
    }
}
