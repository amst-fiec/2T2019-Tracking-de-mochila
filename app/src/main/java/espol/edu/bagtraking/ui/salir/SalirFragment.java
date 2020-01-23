package espol.edu.bagtraking.ui.salir;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import espol.edu.bagtraking.Activity.LoginActivity;
import espol.edu.bagtraking.Modelo.Metodos;
import espol.edu.bagtraking.Modelo.Variables;
import espol.edu.bagtraking.R;

public class SalirFragment extends Fragment {

    private SalirViewModel salirViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        salirViewModel =
                ViewModelProviders.of(this).get(SalirViewModel.class);
        Variables.HAY_INTERNET = Metodos.isOnline();
        cerrarSesion();
        View root = null;

        return root;
    }
    public void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("msg", "cerrarSesion");
        startActivity(intent);

    }

}
