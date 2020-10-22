package br.mil.eb.a3gaaae.controledevisitantes.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.ion.Ion;

import java.net.URLDecoder;
import java.util.List;

import br.mil.eb.a3gaaae.controledevisitantes.Configuration.HttpConection;
import br.mil.eb.a3gaaae.controledevisitantes.Fragments.CadastroFragment;
import br.mil.eb.a3gaaae.controledevisitantes.Fragments.DestinoFragment;
import br.mil.eb.a3gaaae.controledevisitantes.Helper.Utils;
import br.mil.eb.a3gaaae.controledevisitantes.ItemClickListener;
import br.mil.eb.a3gaaae.controledevisitantes.Model.Pessoa;
import br.mil.eb.a3gaaae.controledevisitantes.R;

public class RecyclerRegistroAdapter extends RecyclerView.Adapter implements ItemClickListener {

    private List<Pessoa> pessoaList;
    private Context context;
    private FragmentManager manager;
    private Pessoa pessoa;

    public RecyclerRegistroAdapter(Context c, List<Pessoa> l, Fragment fm) {
        this.context = c;
        this.pessoaList = l;
        manager = fm.getFragmentManager();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pessoa_fragment_list_item, parent, false);
        return new PessoaViewHolderClickable(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        PessoaViewHolderClickable viewHolder = (PessoaViewHolderClickable) holder;

        pessoa = pessoaList.get(position);

        String nomePessoaEncoded;
        nomePessoaEncoded = URLDecoder.decode(pessoa.getNome());
        viewHolder.nome.setText(nomePessoaEncoded + " id " + pessoa.getId());

        try {
            String idt = "IDT: " + pessoa.getIdentidade();
            String cpf = "CPF: " + pessoa.getCpf();
            viewHolder.entrada.setText(pessoa.getTitulo());
            viewHolder.carroModelo.setText(idt);

        } catch (NullPointerException e) {
            Toast.makeText(context, "Há dados não cadastrados", Toast.LENGTH_SHORT).show();
        }
        String imageUrl = HttpConection.downloadPhoto() + pessoa.getId() + ".jpg";
        try {
            loadImage(viewHolder.imageView, imageUrl);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        viewHolder.regSaida.setVisibility(View.GONE);
        viewHolder.setItemClickListener(this);
    }

    @Override
    public int getItemCount() {
        return pessoaList.size();
    }

    private void loadImage(ImageView imageView, String imageUrl) {
        Ion.with(imageView)
                .error(R.drawable.conde_dist_round)
                .animateLoad(2000)
                .animateIn(2000)
                .load(imageUrl);
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        FragmentTransaction transaction;
        DestinoFragment destinoFragment = new DestinoFragment();

        Pessoa p = pessoaList.get(position);
        if (Utils.estaNaHorizontal(context)) {
            transaction = manager.beginTransaction();
            destinoFragment.setAttrs(p);

            transaction.replace(R.id.fragment2, destinoFragment)
                    .addToBackStack("cad")
                    .commit();
        } else {
            transaction = manager.beginTransaction();
            destinoFragment.setAttrs(p);
            transaction.replace(R.id.ambos_fragments, destinoFragment)
                    .addToBackStack("cad")
                    .commit();
        }
    }

    @Override
    public boolean onLongClick(View v, int position, boolean isLongClick) {

        AlertDialog.Builder d = new AlertDialog.Builder(context);

        final Pessoa p = pessoaList.get(position);

        d.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FragmentTransaction transaction;
                CadastroFragment destinoFragment = new CadastroFragment();

                if (Utils.estaNaHorizontal(context)) {
                    transaction = manager.beginTransaction();
                    destinoFragment.setPessoa(p);
                    transaction.replace(R.id.fragment2, destinoFragment)
                            .addToBackStack("cad")
                            .commit();
                } else {
                    transaction = manager.beginTransaction();
                    destinoFragment.setPessoa(p);
                    transaction.replace(R.id.ambos_fragments, destinoFragment)
                            .addToBackStack("cad")
                            .commit();
                }
            }
        });

        d.setCancelable(true);
        d.show();
        return true;
    }

}
