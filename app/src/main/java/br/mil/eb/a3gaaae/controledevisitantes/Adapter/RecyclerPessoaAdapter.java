package br.mil.eb.a3gaaae.controledevisitantes.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import br.mil.eb.a3gaaae.controledevisitantes.Configuration.HttpConection;
import br.mil.eb.a3gaaae.controledevisitantes.Model.Pessoa;
import br.mil.eb.a3gaaae.controledevisitantes.R;

@SuppressLint("RecyclerView")
public class RecyclerPessoaAdapter extends RecyclerView.Adapter {

    private List<Pessoa> pessoaList;
    private Context context;

    public RecyclerPessoaAdapter(Context c, List<Pessoa> l) {
        this.context = c;
        this.pessoaList = l;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pessoa_fragment_list_item, parent, false);
        return new PessoaViewHolder(itemView);
    }
    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,  final int position) {
        PessoaViewHolder viewHolder = (PessoaViewHolder) holder;
        final Pessoa pessoa = pessoaList.get(position);

        Timestamp timestamp = Timestamp.valueOf(pessoa.getEntrada());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String entradaFormat = dateFormat.format(timestamp);

        if (pessoa.getCarroModelo().isEmpty()) {
            viewHolder.carroModelo.setVisibility(View.GONE);
        } else {
            viewHolder.carroModelo.setText(pessoa.getCarroModelo());
        }
        if (pessoa.getCarroPlaca().isEmpty()) {
            viewHolder.carroPlaca.setVisibility(View.GONE);
        } else {
            viewHolder.carroPlaca.setText(pessoa.getCarroPlaca());
        }

        viewHolder.nome.setText(pessoa.getNome());
        viewHolder.entrada.setText(entradaFormat);

        String imageUrl = HttpConection.downloadPhoto() + pessoa.getId() + ".jpg";
        try {
            loadImage(viewHolder.imageView, imageUrl);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        viewHolder.regSaida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registraSaida(String.valueOf(pessoa.getId()));
                pessoaList.remove(position);
                RecyclerPessoaAdapter.this.notifyDataSetChanged();
            }
        });
    }

    private void loadImage(ImageView imageView, String imageUrl) {
        Ion.with(imageView)
                .error(R.drawable.conde_dist_round)
                .animateLoad(2000)
                .animateIn(2000)
                .load(imageUrl);
    }

    @Override
    public int getItemCount() {
        return pessoaList.size();
    }

    private void registraSaida(String pessoaId) {

        String updateUrl = HttpConection.updateSaida();

        Ion.with(context).load(updateUrl)
                .setBodyParameter("id", pessoaId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null && result.get("UPDATE").getAsString().equals("OK")) {
                            Toast.makeText(context, "Regsitrando saída!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}