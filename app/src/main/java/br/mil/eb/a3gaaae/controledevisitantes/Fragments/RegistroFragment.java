package br.mil.eb.a3gaaae.controledevisitantes.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import br.mil.eb.a3gaaae.controledevisitantes.Adapter.RecyclerRegistroAdapter;
import br.mil.eb.a3gaaae.controledevisitantes.Configuration.HttpConection;
import br.mil.eb.a3gaaae.controledevisitantes.Model.Pessoa;
import br.mil.eb.a3gaaae.controledevisitantes.R;

public class RegistroFragment extends Fragment {
    protected List<Pessoa> pessoas;
    protected SearchView search;
    private RecyclerRegistroAdapter adapter;
    private Pessoa p;

    public RegistroFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pessoas = new ArrayList<>();
        adapter = new RecyclerRegistroAdapter(getContext(), pessoas, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.fragment_registro, container, false);
        loadUi(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadList(newText);
                return false;
            }
        });
    }

    private void loadUi(View v) {
        search = v.findViewById(R.id.searchView);
        search.requestFocus();
        RecyclerView recyclerView = v.findViewById(R.id.fragment_list_recycler_registro);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadList(String valor) {

        Ion.with(Objects.requireNonNull(getContext()))
                .load(HttpConection.buscaentrada())
                .setBodyParameter("valor", valor)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        pessoas.clear();
                        if (result != null) {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject object = result.get(i).getAsJsonObject();
                                p = new Pessoa();
                                p.setId(object.get("id").getAsInt());
                                p.setNome(object.get("nome").getAsString());
                                p.setTitulo(object.get("titulo").getAsString());
                                p.setIdentidade(object.get("idt").getAsString());
                                p.setCpf(object.get("cpf").getAsString());
                                pessoas.add(p);
                            }
                            adapter.notifyDataSetChanged();

                        } else {

                            Log.i("Json", e.getCause() + " " + Arrays.toString(e.getStackTrace()));
                            Log.i("Json", HttpConection.buscaentrada());
//                            e.printStackTrace();
                        }
                    }
                });
    }

}
