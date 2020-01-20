package espol.edu.bagtraking.Activity;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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


import java.text.DecimalFormat;
import java.util.HashMap;

import espol.edu.bagtraking.R;
import espol.edu.bagtraking.ui.gallery.GalleryFragment;
import espol.edu.bagtraking.ui.grafica.GraficFragment;
import espol.edu.bagtraking.ui.map.MapFragment;
import espol.edu.bagtraking.ui.salir.SalirFragment;
import espol.edu.bagtraking.ui.tools.ToolsFragment;

public class perfil_usuario_1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView imv_photo;
    private TextView txt_name, txt_email;
    public static TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario_1);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        String photo = info_user.get("user_photo");
        System.out.println(photo);
        Picasso.with(getApplicationContext()).load(photo).into(imv_photo);

        StartThread();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

         /*button.setOnClickListener(new View.OnClickListener() {
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

        });*/
        getMenuInflater().inflate(R.menu.perfil_usuario_1, menu);
        return true;
    }


    public void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("msg", "cerrarSesion");
        startActivity(intent);
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
        Thread thread = new Thread() {
            @Override
            public void run() {

                while(true){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    text = findViewById(R.id.tag_distancia);
                    if(text!=null && MapFragment.Ready){

                        text.setText("Distancia Mochila: "+(new DecimalFormat("#.00").format(MapFragment.DISTANCIA))+"Km");
                    }

                }

            }
        };

        thread.start();
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
                GalleryFragment starredFragment = new GalleryFragment();
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
}
