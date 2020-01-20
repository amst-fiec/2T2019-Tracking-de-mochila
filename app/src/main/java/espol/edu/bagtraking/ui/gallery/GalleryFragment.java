package espol.edu.bagtraking.ui.gallery;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import espol.edu.bagtraking.Activity.Device_Bluetooth;
import espol.edu.bagtraking.Activity.perfil_usuario_1;
import espol.edu.bagtraking.R;
import espol.edu.bagtraking.ui.grafica.GraficViewModel;
import espol.edu.bagtraking.ui.map.MapFragment;
import espol.edu.bagtraking.ui.tools.ToolsViewModel;

public class GalleryFragment extends Fragment {
    private GraficViewModel toolsViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        toolsViewModel =
                ViewModelProviders.of(this).get(GraficViewModel.class);
        ((perfil_usuario_1) getActivity()).getSupportActionBar().setTitle("GALLERY");
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.textViewInboxFragment);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFragmentManager().beginTransaction().remove(new MapFragment()).commit();
    }
}