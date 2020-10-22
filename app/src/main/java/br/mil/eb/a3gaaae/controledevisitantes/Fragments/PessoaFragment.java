package br.mil.eb.a3gaaae.controledevisitantes.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import br.mil.eb.a3gaaae.controledevisitantes.Adapter.RecyclerPessoaAdapter;
import br.mil.eb.a3gaaae.controledevisitantes.Configuration.HttpConection;
import br.mil.eb.a3gaaae.controledevisitantes.Helper.Utils;
import br.mil.eb.a3gaaae.controledevisitantes.Model.Pessoa;
import br.mil.eb.a3gaaae.controledevisitantes.R;

public class PessoaFragment extends Fragment {

    private RecyclerPessoaAdapter adapter;
    private List<Pessoa> pessoas;

    public PessoaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pessoas = new ArrayList<>();
        adapter = new RecyclerPessoaAdapter(getContext(), pessoas);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = getLayoutInflater().inflate(R.layout.fragment_pessoa_list, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.fragment_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        Utils.setProgressBar(v.<ProgressBar>findViewById(R.id.progressBar));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadList();
    }

    private void loadList() {

        Utils.loadProgress();

        Ion.with(this)
                .load(HttpConection.read())
                .progressBar(Utils.getProgressBar())
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        if (result != null) {
                            pessoas.clear();
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject object = result.get(i).getAsJsonObject();
                                Pessoa p = new Pessoa();
                                p.setId(object.get("id").getAsInt());
                                p.setNome(object.get("nome").getAsString());
                                p.setTitulo(object.get("titulo").getAsString());
                                p.setIdentidade(object.get("idt").getAsString());
                                p.setCarroModelo(object.get("modelo").getAsString());
                                p.setCarroPlaca(object.get("placa").getAsString());
                                p.setEntrada(object.get("entrada").getAsString());
                                pessoas.add(p);
                            }
                            adapter.notifyDataSetChanged();
                            Utils.getProgressBar().setVisibility(View.GONE);
                        }

                    }
                });


    }

}
