package espol.edu.bagtraking.ui.maletas;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


import espol.edu.bagtraking.Activity.perfil_usuario_1;
import espol.edu.bagtraking.Modelo.Adapter;
import espol.edu.bagtraking.R;
import espol.edu.bagtraking.ui.grafica.GraficViewModel;
import espol.edu.bagtraking.ui.map.MapFragment;



public class MaletasFragment extends Fragment {
    private static final String TAG = "Error";
    private GraficViewModel toolsViewModel;
    ListView listView;
    private FirebaseAuth mAuth;
    DatabaseReference db_reference;
    Adapter adapter;
    String[] elementos = {"4334BA"};
    ArrayList<HashMap<String,String>> maletas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        toolsViewModel =
                ViewModelProviders.of(this).get(GraficViewModel.class);
        ((perfil_usuario_1) getActivity()).getSupportActionBar().setTitle("MALETAS");
        View root = inflater.inflate(R.layout.fragment_maletas, container, false);
        mAuth = FirebaseAuth.getInstance();
        maletas = new ArrayList<>();
        db_reference = FirebaseDatabase.getInstance().getReference().child("Aplicacion");
        UpdateMaletas(mAuth.getCurrentUser());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFragmentManager().beginTransaction().remove(new MapFragment()).commit();
    }
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        listView = (ListView)getView().findViewById(R.id.lista_maletas);



        Button agregar = getView().findViewById(R.id.buttonFragmentInbox);
        EditText id = getView().findViewById(R.id.txt_id);
        EditText nombre = getView().findViewById(R.id.txt_name);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarMaleta(id.getText().toString(),nombre.getText().toString());
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MaletasFragment starredFragment = new MaletasFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, starredFragment);
                fragmentTransaction.commit();

            }
        });

    }
    private void guardarMaleta(String id, String nombre){
        HashMap<String,String> maleta = new HashMap<>();
        FirebaseUser user = mAuth.getCurrentUser();
        maleta.put("user_id_maleta",id);
        maleta.put("user_name_maleta",nombre);
        if(maletas.isEmpty()){
            maletas = new ArrayList<>();
        }

        maletas.add(maleta);

        DatabaseReference data = db_reference.child("Usuarios").child(user.getUid());
        data.child("maletas").setValue(maletas);


    }

    private void UpdateMaletas(FirebaseUser user) {

        DatabaseReference ref =db_reference.child("Usuarios").child(user.getUid()).child("maletas");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    HashMap<String,String> valores = (HashMap<String,String>)snapshot.getValue();
                    maletas.add(valores);

                }
                adapter = new Adapter(getActivity().getApplicationContext(),maletas);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity().getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        MapFragment inboxFragment = new MapFragment();
                        fragmentTransaction.replace(getId(), inboxFragment);
                        fragmentTransaction.commit();
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}