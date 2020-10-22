package br.mil.eb.a3gaaae.controledevisitantes.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import br.mil.eb.a3gaaae.controledevisitantes.Helper.Utils;
import br.mil.eb.a3gaaae.controledevisitantes.R;

public class MainFragment extends Fragment implements View.OnClickListener {
    Button botaoNovo, botaoRegistrar, botaoListar, faceDetection;
    FragmentManager manager;
    FragmentTransaction transaction;
    private String TAG = "DEBUG";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = getLayoutInflater().inflate(R.layout.activity_main, container, false);
        botaoNovo = v.findViewById(R.id.btn_novo);
        botaoRegistrar = v.findViewById(R.id.btn_reg);
        botaoListar = v.findViewById(R.id.btn_lista);
        faceDetection = v.findViewById(R.id.btn_face_detect);

        return v;
    }

    @Override
    public void onClick(View v) {

        if (Utils.estaNaHorizontal(Objects.requireNonNull(this.getContext()))) {
            switch (v.getId()) {

                case R.id.btn_novo:

                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragment2, new CadastroFragment());
                    transaction.commit();
//                    Log.i("touched", "NOVO");
                    break;

                case R.id.btn_lista:
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragment2, new PessoaFragment());
                    transaction.commit();
//                    Log.i("touched", "LISTA");
                    break;

                case R.id.btn_reg:
                    transaction = manager.beginTransaction();

                    manager.beginTransaction()
                            .replace(R.id.fragment2, new RegistroFragment())
                            .commit();
                    break;

                case R.id.btn_face_detect:
//                    transaction = manager.beginTransaction();
//                    manager.beginTransaction()
//                            .replace(R.id.fragment2, new FaceRecogFragment())
//                            .commit();

                    startActivity(new Intent(getContext(), FaceRecogFragment.class));

                    break;
            }
        } else {
//            Log.i("TELA", "false");
            switch (v.getId()) {

                case R.id.btn_novo:

                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.ambos_fragments, new CadastroFragment())
                            .addToBackStack("cad")
                            .commit();
//                    Log.i(TAG, "NOVO");
                    break;

                case R.id.btn_lista:
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.ambos_fragments, new PessoaFragment()).addToBackStack("pessoa")
                            .addToBackStack("cad")
                            .commit();
//                    Log.i(TAG, "LISTA");
                    break;

                case R.id.btn_reg:
                    transaction = manager.beginTransaction();
                    manager.beginTransaction()
                            .replace(R.id.ambos_fragments, new RegistroFragment()).addToBackStack("cad")
                            .commit();
                    break;

                case R.id.btn_face_detect:
//                    transaction = manager.beginTransaction();
//                    manager.beginTransaction()
//                            .replace(R.id.ambos_fragments, new FaceRecogFragment()).addToBackStack("face")
//                            .commit();
                    startActivity(new Intent(getContext(), FaceRecogFragment.class));

                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        botaoNovo.setOnClickListener(this);
        botaoListar.setOnClickListener(this);
        botaoRegistrar.setOnClickListener(this);
        faceDetection.setOnClickListener(this);
    }

}
