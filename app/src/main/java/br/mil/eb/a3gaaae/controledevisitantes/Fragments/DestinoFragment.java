package br.mil.eb.a3gaaae.controledevisitantes.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.mil.eb.a3gaaae.controledevisitantes.Configuration.HttpConection;
import br.mil.eb.a3gaaae.controledevisitantes.Model.Pessoa;
import br.mil.eb.a3gaaae.controledevisitantes.R;

public class DestinoFragment extends Fragment implements View.OnClickListener {

    private ListView listView;
    private EditText modeloCarro, placaCarro;
    private Button registra;
    private Pessoa pessoa;
    private String destino;
    private List<String> listaDestinos;
    private Context context;
    private ArrayAdapter adapter;
    private String TAG = "LOG";

    public DestinoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View v = getLayoutInflater().inflate(R.layout.fragment_destino, container, false);
        listaDestinos = new ArrayList<>();
        loadUi(v);
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listaDestinos);
        loadDestinos(context);
        listView.setAdapter(adapter);
        return v;
    }

    private void loadUi(View view) {

        listView = view.findViewById(R.id.list_view_destino);
        modeloCarro = view.findViewById(R.id.dialog_modelo_carro);
        placaCarro = view.findViewById(R.id.dialog_placa_carro);
        registra = view.findViewById(R.id.dialog_btn_registrar);
        destino = "";
        SimpleMaskFormatter smf = new SimpleMaskFormatter("LLL-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(placaCarro, smf);
        placaCarro.addTextChangedListener(mtw);
    }

    @Override
    public void onClick(View v) {
        registraEntradaComCarro(pessoa, destino);
    }

    @Override
    public void onStart() {
        super.onStart();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destino = listaDestinos.get(position);
                if (!destino.equals("")) {
                    listView.setSelected(true);
                }
                //TODO Criar dialogo para o "Falar com" e "outro"
            }
        });

        registra.setOnClickListener(this);

    }

    private void registraEntradaComCarro(Pessoa pessoa, String destino) {

        String updateUrl = HttpConection.updateEntrada();

        pessoa.setCarroModelo(modeloCarro.getText().toString());
        pessoa.setCarroPlaca(placaCarro.getText().toString());

        if (destino.isEmpty()) {

            listView.requestFocus();
            Toast.makeText(context, "Selecione UM destino!", Toast.LENGTH_SHORT).show();

        } else Ion.with(Objects.requireNonNull(getContext())).load(updateUrl)
                .setBodyParameter("id", String.valueOf(pessoa.getId()))
                .setBodyParameter("nome", pessoa.getNome())
                .setBodyParameter("titulo", pessoa.getTitulo())
                .setBodyParameter("idt", pessoa.getIdentidade())
                .setBodyParameter("destino", destino)
                .setBodyParameter("modelo", pessoa.getCarroModelo())
                .setBodyParameter("placa", pessoa.getCarroPlaca())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null && result.get("UPDATE").getAsString().equals("OK")) {
                            Toast.makeText(getContext(), "Regsitrando Entrada...", Toast.LENGTH_SHORT).show();
                            if (getFragmentManager() != null) {
                                getFragmentManager().popBackStack();
                            }
                        } else e.printStackTrace();
                    }
                });
    }

    public void setAttrs(Pessoa attrs) {
        this.pessoa = attrs;
        Log.i(TAG, " " + pessoa.getId());
    }

    private void loadDestinos(Context c) {

        String updateUrl = HttpConection.getDestinos();

        Ion.with(c).load(updateUrl)
                .setLogging(TAG, Log.DEBUG)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result != null) {
                            listaDestinos.clear();
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject object = result.get(i).getAsJsonObject();
                                String destinos = (object.get("destino").getAsString());
                                listaDestinos.add(destinos);
                                adapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.e(TAG, "Lista Vazia");
                        }
                    }
                });

    }

}
