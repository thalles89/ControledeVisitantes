package br.mil.eb.a3gaaae.controledevisitantes.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.mil.eb.a3gaaae.controledevisitantes.R;

public class PessoaViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    protected TextView nome;
     TextView carroModelo;
     TextView carroPlaca;
     TextView entrada;
     Button regSaida;

    PessoaViewHolder(View itemView) {
        super(itemView);
        nome = itemView.findViewById(R.id.fragment_list_nome);
        carroModelo = itemView.findViewById(R.id.fragment_list_carro_modelo);
        carroPlaca = itemView.findViewById(R.id.fragment_list_carro_placa);
        entrada = itemView.findViewById(R.id.fragment_list_entrada);
        imageView = itemView.findViewById(R.id.fragment_list_img);
        regSaida = itemView.findViewById(R.id.fragment_list_button);

    }

}

