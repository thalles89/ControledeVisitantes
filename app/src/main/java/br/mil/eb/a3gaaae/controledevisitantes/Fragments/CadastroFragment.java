package br.mil.eb.a3gaaae.controledevisitantes.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import br.mil.eb.a3gaaae.controledevisitantes.Configuration.HttpConection;
import br.mil.eb.a3gaaae.controledevisitantes.Helper.Utils;
import br.mil.eb.a3gaaae.controledevisitantes.Model.Pessoa;
import br.mil.eb.a3gaaae.controledevisitantes.R;

import static android.app.Activity.RESULT_CANCELED;

public class CadastroFragment extends Fragment {

    private final int CAMERA = 1;
    private EditText nome, titulo, idt, cpf;
    private Button cadastrar;
    private ImageView visitorImage;
    private Bitmap imgUploadFile;
    private String id;
    private Pessoa pessoa;
    private String urlInsert = HttpConection.create();
    private String urlUpdate = HttpConection.updateUser();

    public CadastroFragment() {
    }

    public void setPessoa(Pessoa p) {
        this.pessoa = p;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.fragment_cadastro, container, false);
        loadUi(v);
        return v;
    }

    private void loadUi(View view) {

        try {
            nome = view.findViewById(R.id.edit_cadastro_nome);
            titulo = view.findViewById(R.id.edit_cadastro_titulo);
            idt = view.findViewById(R.id.edit_cadastro_identidade);
            cpf = view.findViewById(R.id.edit_cadastro_cpf);
            cadastrar = view.findViewById(R.id.btn_cadastrar);
            visitorImage = view.findViewById(R.id.edit_cadastro_foto);

            SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
            MaskTextWatcher mtw = new MaskTextWatcher(cpf, smf);
            cpf.addTextChangedListener(mtw);

            Utils.setProgressBar(view.<ProgressBar>findViewById(R.id.progressbar));
            Utils.getProgressBar().setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pessoa != null) {
            nome.setText(pessoa.getNome());
            titulo.setText(pessoa.getTitulo());
            idt.setText(pessoa.getIdentidade());
            cpf.setText(pessoa.getCpf());
            cadastrar.setText(R.string.atualizar);

            try {
                Ion.with(visitorImage)
                        .error(R.drawable.conde_dist_round)
                        .animateLoad(5000)
                        .load(HttpConection.downloadPhoto().concat(pessoa.getId() + ".jpg"));
            } catch (Exception ignored) {

            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        visitorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoClick();
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
                Utils.getProgressBar().setVisibility(View.VISIBLE);
            }
        });

    }

    private void cadastrar() {

        String name = nome.getText().toString();
        String tit = titulo.getText().toString();
        String idtNum = idt.getText().toString();
        String cpfNum = cpf.getText().toString();

        if (name.isEmpty()) {
            nome.setError("Preencha o nome!");
            return;
        }

        idtNum = idtNum.replace("-", "").trim();
        idtNum = idtNum.replace(".", "").trim();
        cpfNum = cpfNum.replace("-", "").trim();
        cpfNum = cpfNum.replace(".", "").trim();

        if (pessoa == null) {
            doInsert(name, tit, idtNum, cpfNum);
        } else {
            doUpdate(String.valueOf(pessoa.getId()), name, tit, idtNum, cpfNum);
        }
    }

    private void doUpdate(final String pessoaId, String name, String titutlo, String idtNum, String cpfNum) {
        Ion.with(Objects.requireNonNull(getActivity()).getBaseContext())
                .load(urlUpdate)
                .setBodyParameter("nome", name)
                .setBodyParameter("titulo", titutlo)
                .setBodyParameter("idt", idtNum)
                .setBodyParameter("cpf", cpfNum)
                .setBodyParameter("id", pessoaId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String url = HttpConection.uploadPhoto();

                            Ion.with(Objects.requireNonNull(getActivity()).getBaseContext())
                                    .load("POST", url)
                                    .setMultipartFile("foto", new File(saveImage(imgUploadFile, pessoaId)))
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {
                                            if (result != null && result.get("UPLOAD").getAsString().equals("OK")) {
                                                Utils.getProgressBar().setVisibility(View.GONE);
                                            } else {
                                                Utils.getProgressBar().setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "Erro ao salvar foto!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            limparCampos();

                            Utils.getProgressBar().setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Cadastro Atualizado!", Toast.LENGTH_SHORT).show();

                        } catch (NullPointerException e1) {
                            Toast.makeText(getContext(), "Refaça a operação!", Toast.LENGTH_SHORT).show();
                            Log.i("restultSet", result.getAsString());
                            e1.printStackTrace();
                            Utils.getProgressBar().setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void doInsert(String name, String titutlo, String idtNum, String cpfNum) {
        Ion.with(Objects.requireNonNull(getContext()))
                .load("POST", urlInsert)
                .setBodyParameter("nome", name)
                .setBodyParameter("titulo", titutlo)
                .setBodyParameter("idt", idtNum)
                .setBodyParameter("cpf", cpfNum)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            Log.i("CADASTRO", result.get("CREATE").getAsString());
                            String url = HttpConection.uploadPhoto();
                            id = result.get("ID").getAsString();
                            Toast.makeText(getContext(), "Usuário incluído!", Toast.LENGTH_SHORT).show();
                            Ion.with(Objects.requireNonNull(getContext()))
                                    .load("POST", url)
                                    .setMultipartFile("foto", new File(saveImage(imgUploadFile, id)))
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {
                                            if (result != null && result.get("UPLOAD").getAsString().equals("OK")) {
//                                                Log.i("restultSet", result.get("UPLOAD").getAsString());
                                                Utils.getProgressBar().setVisibility(View.GONE);
                                            } else {
                                                Utils.getProgressBar().setVisibility(View.GONE);
                                                e.printStackTrace();
                                                Toast.makeText(getContext(), "Erro ao salvar foto!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            limparCampos();
                            Utils.getProgressBar().setVisibility(View.GONE);

                        } catch (NullPointerException e1) {
                            Toast.makeText(getContext(), "Refaça a operação!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Utils.getProgressBar().setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void limparCampos() {
        nome.setText("");
        titulo.setText("");
        idt.setText("");
        cpf.setText("");
        visitorImage.setImageResource(R.drawable.conde_dist_round);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            photoClick();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED || data == null) {
            return;
        }

        if (requestCode == CAMERA) {

            try {
                Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                assert bitmap != null;
                visitorImage.setImageBitmap(bitmap);
                imgUploadFile = (bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void photoClick() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            showCamera();
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                Manifest.permission.CAMERA)) {
            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
            b.setTitle("Permissão!");
            b.setMessage("É preciso dar permissão");
            b.setPositiveButton("Perimitir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA}, CAMERA);
                }
            });
            b.show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA);
        }
    }

    private void showCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA);
    }

    @NonNull
    private String saveImage(Bitmap bitmap, String id) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String IMAGE_DIR = "/visitors";
        File directory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIR);

        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Log.i("", "");
            } else {
                Log.w("00", "");
            }
        }

        File f = new File(directory, id + ".jpg");

        try {
            if (f.createNewFile()) {
                Log.i("", "");
            } else {
                Log.w("", "");
            }
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            MediaScannerConnection.scanFile(getContext(), new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);

            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

}
