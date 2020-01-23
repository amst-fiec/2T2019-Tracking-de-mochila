package espol.edu.bagtraking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import espol.edu.bagtraking.Modelo.Metodos;
import espol.edu.bagtraking.Modelo.ModelFireBase;
import espol.edu.bagtraking.Modelo.Variables;
import espol.edu.bagtraking.R;

public class Splash_Screen extends AppCompatActivity {

    private ModelFireBase firebase;
    private HashMap<String, String> info_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash__screen);
        Variables.HAY_INTERNET = Metodos.isOnline();
        info_user = new HashMap<>();
        if (Variables.HAY_INTERNET) {
            firebase = new ModelFireBase("Aplicacion");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (firebase.getCurrentUser() == null) {
                        Intent intent = new Intent(Splash_Screen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();


                    } else {
                        firebase.getUser(info_user);
                        StartThread();
                    }
                }
            }, 2000);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splash_Screen.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(Splash_Screen.this, "Conexion Perdida", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, 2000);
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
                Intent intent = new Intent(getApplicationContext(), perfil_usuario_1.class);
                intent.putExtra("info_user", info_user);
                startActivity(intent);
                finish();
            }
        };

        thread.start();
    }



}
