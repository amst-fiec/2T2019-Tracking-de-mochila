package espol.edu.bagtraking.Activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;


import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

import espol.edu.bagtraking.Modelo.Variables;
import espol.edu.bagtraking.R;
import espol.edu.bagtraking.ui.maletas.MaletasFragment;
import espol.edu.bagtraking.ui.grafica.GraficFragment;
import espol.edu.bagtraking.ui.map.MapFragment;
import espol.edu.bagtraking.ui.salir.SalirFragment;
import espol.edu.bagtraking.ui.tools.ToolsFragment;

public class perfil_usuario_1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =300 ;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView imv_photo;
    private TextView txt_name, txt_email;
    private String phone;
    public static TextView text;

    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario_1);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Variables.RECIBIR_NOTIFICACION = true;
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Variables.RECIBIR_NOTIFICACION){
                    fab.setImageResource(R.drawable.ic_notifications_off_black_24dp);
                    Snackbar.make(view, "Desactivo Sus Notificaciones de Ubicacion", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Variables.RECIBIR_NOTIFICACION = false;
                }else{
                    fab.setImageResource(R.drawable.ic_notifications_active_black_24dp);
                    Snackbar.make(view, "Activo Sus Notificaciones de Ubicacion", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Variables.RECIBIR_NOTIFICACION = true;
                }

            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        navigationView.setNavigationItemSelectedListener(this);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools,R.id.nav_salir)
                .setDrawerLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        HashMap<String, String> info_user = (HashMap<String, String>)intent.getSerializableExtra("info_user");

        View headview = navigationView.getHeaderView(0);
        imv_photo = headview.findViewById(R.id.perfil1);
        txt_email = headview.findViewById(R.id.mail1);
        txt_name = headview.findViewById(R.id.nombre1);

        txt_name.setText(info_user.get("user_name"));
        txt_email.setText(info_user.get("user_email"));
        phone= info_user.get("user_phone");
        String photo = info_user.get("user_photo");

        Picasso.with(getApplicationContext()).load(photo).into(imv_photo);


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                MY_PERMISSIONS_REQUEST_SEND_SMS);

        StartThread();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.stop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {super.onStart();
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.perfil_usuario_1, menu);
        return true;
    }


    public void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("msg", "cerrarSesion");
        startActivity(intent);
        thread.stop();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                menuItem.setChecked(true);
                setFragment(0);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_gallery:
                menuItem.setChecked(true);
                setFragment(1);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_tools:
                menuItem.setChecked(true);
                setFragment(3);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_slideshow:
                menuItem.setChecked(true);
                setFragment(2);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_salir:
                menuItem.setChecked(true);
                //setFragment(4);
                cerrarSesion();
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;

        }
        return false;
    }
    public void StartThread(){
        thread = new Thread() {
            @Override
            public void run() {

                while(true){
                    try {
                        Thread.sleep(Variables.INTERVALO_NOTIFICACION);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    text = findViewById(R.id.tag_distancia);

                    if(text!=null && MapFragment.Ready){

                        text.setText("Distancia Mochila: "+(new DecimalFormat("#.00").format(MapFragment.DISTANCIA))+"m");

                        if(MapFragment.DISTANCIA>Variables.MAXIMO_DISTANCIA && Variables.RECIBIR_NOTIFICACION) {
                            showNotification(" Alerta de Movimiento", "Su Maleta Se movio: " + (new DecimalFormat("#.00").format(MapFragment.DISTANCIA)));

                            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                    Manifest.permission.SEND_SMS)
                                    != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            if(phone!=null && phone!=""){
                                sendMessage(phone,"Mensaje de Prueba");
                            }





                        }
                    }

                }

            }
        };

        thread.start();
    }

    private void sendMessage(String numero, String sms){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(numero, null, sms, null, null);

    }


    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MapFragment inboxFragment = new MapFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, inboxFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MaletasFragment starredFragment = new MaletasFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, starredFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                GraficFragment graficfragment = new GraficFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, graficfragment);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ToolsFragment toolsFragment = new ToolsFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, toolsFragment);
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                SalirFragment salirFragment = new SalirFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, salirFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.amst.firebasenotify.test";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("EDMT Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(body).setContentInfo("info").setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }
}
