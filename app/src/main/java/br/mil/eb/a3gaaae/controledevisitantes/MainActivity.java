package br.mil.eb.a3gaaae.controledevisitantes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import br.mil.eb.a3gaaae.controledevisitantes.Fragments.MainFragment;
import br.mil.eb.a3gaaae.controledevisitantes.Fragments.RegistroFragment;
import br.mil.eb.a3gaaae.controledevisitantes.Helper.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (Utils.estaNaHorizontal(this)) {
            setContentView(R.layout.fragment_main_landscape);
            MainFragment mainFragment = new MainFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment1, mainFragment)
                    .commit();

            RegistroFragment registroFragment = new RegistroFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment2, registroFragment)
                    .commit();
        } else {

            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ambos_fragments, mainFragment)
                    .commit();
        }

    }

}
