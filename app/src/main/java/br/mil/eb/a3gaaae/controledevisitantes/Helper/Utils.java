package br.mil.eb.a3gaaae.controledevisitantes.Helper;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.widget.ProgressBar;

public class Utils {

    private static ProgressBar progressBar;

    private static Handler handler = new Handler();
    private static int progressStatus;

    public static void loadProgress(){
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 10;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            getProgressBar().setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static boolean estaNaHorizontal(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    public static ProgressBar getProgressBar() {
        return progressBar;
    }

    public static void setProgressBar(ProgressBar progressBar) {
        Utils.progressBar = progressBar;
    }
}
