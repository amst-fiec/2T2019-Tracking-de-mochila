package espol.edu.bagtraking.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;


import espol.edu.bagtraking.Activity.Device_Bluetooth;
import espol.edu.bagtraking.Activity.perfil_usuario_1;

import espol.edu.bagtraking.R;
import espol.edu.bagtraking.ui.map.MapFragment;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        ((perfil_usuario_1) getActivity()).getSupportActionBar().setTitle("Configuraciones");
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        Intent intent=new Intent(getActivity(), Device_Bluetooth.class);
        startActivity(intent);
        try {
            finalize();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MapFragment inboxFragment = new MapFragment();
            fragmentTransaction.replace(this.getId(), inboxFragment);
            fragmentTransaction.commit();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return root;
    }
}