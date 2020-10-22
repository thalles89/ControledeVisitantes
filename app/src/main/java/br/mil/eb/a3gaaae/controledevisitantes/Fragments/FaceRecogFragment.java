package br.mil.eb.a3gaaae.controledevisitantes.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

import br.mil.eb.a3gaaae.controledevisitantes.R;

public class FaceRecogFragment extends Activity implements CameraBridgeViewBase.CvCameraViewListener {

    private static final String CASCADE = "lbpcascade_frontalface.xml";

    static {
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initDebug();
        }
    }

    private int absoluteFaceSize;
    private Mat grayscaleImage;
    private CameraBridgeViewBase openCvCamera;
    //    private String TAG = "OpenCV";
    private CascadeClassifier cascadeClassifier;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.fragment_face_recog);

        openCvCamera = findViewById(R.id.cameraview);
        Button detect = findViewById(R.id.btn_detect);

        openCvCamera.enableFpsMeter();

        openCvCamera.setVisibility(View.VISIBLE);
        openCvCamera.setCvCameraViewListener(this);

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCvCamera.enableView();
            }
        });
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);

        // The faces will be a 20% of the height of the screen
        absoluteFaceSize = (int) (height * 0.2);
    }


    @Override
    public void onCameraViewStopped() {
//        openCvCamera.disableView();
    }

    @Override
    public Mat onCameraFrame(Mat aInputFrame) {
        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);

        MatOfRect faces = new MatOfRect();

        // Use the classifier to detect faces
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        // If there are any faces found, draw a rectangle around it
        Rect[] listOfFaces = faces.toArray();

        for (Rect rect : listOfFaces)
            Imgproc.rectangle(aInputFrame, rect.tl(), rect.br(), new Scalar(0, 255, 0, 255), 2);

        Core.rotate(aInputFrame, aInputFrame, Core.ROTATE_90_CLOCKWISE);

        return aInputFrame;
    }

    private void initializeOpenCVDependencies() {

        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = this.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, CASCADE);
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1)
                os.write(buffer, 0, bytesRead);

            is.close();
            os.close();

            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());

        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
            Toast.makeText(this, "Erro ao carregar CASCADE", Toast.LENGTH_SHORT).show();
        }

        // And we are ready to go
        // openCvCamera.enableView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        openCvCamera.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (openCvCamera != null) openCvCamera.disableView();
    }
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {

                initializeOpenCVDependencies();
            } else {
                super.onManagerConnected(status);
            }
        }


    };
}
