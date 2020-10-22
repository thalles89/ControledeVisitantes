package br.mil.eb.a3gaaae.controledevisitantes.Configuration;

public class HttpConection {

    private static final String HOST = "http://192.168.0.5/controle/mobi/";

    private static final String CREATE = "create.php";
//    private static final String DELETE = "delete.php";
    private static final String UPDATESAIDA = "updateSaida.php";
    private static final String UPDATEENTRADA = "updateEntrada.php";
    private static final String READ = "appSaida.php";
    private static final String BUSCAENTRADA = "buscaRegistroEntrada.php";
    private static final String UPLOADPHOTO = "recebeUpload.php";
    private static final String FOTODIR = "../customers/fotos/";
    private static final String DESTINOS = "getDestino.php";
    private static final String UPDATEUSER = "updateUser.php";

    public static String create(){
        return HOST + CREATE;
    }

    public static String updateSaida(){
        return HOST + UPDATESAIDA;
    }

    public static String updateEntrada(){
        return HOST + UPDATEENTRADA;
    }

//    public static String delete(){
//        return HOST + DELETE;
//    }

    public static String buscaentrada(){
        return HOST + BUSCAENTRADA;
    }

    public static String uploadPhoto(){
        return HOST + UPLOADPHOTO;
    }

    public static String read() {
        return  HOST + READ;
    }

    public static String downloadPhoto(){
        return HOST + FOTODIR;
    }

    public static String getDestinos() {
        return HOST + DESTINOS;
    }

    public static String updateUser() {
        return  HOST + UPDATEUSER;
    }
}
