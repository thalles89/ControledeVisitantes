package br.mil.eb.a3gaaae.controledevisitantes;

import android.view.View;

public interface ItemClickListener {

    void onClick(View v, int position, boolean isLongClick);

    boolean onLongClick(View v, int position, boolean isLongClick);
}