package in.mrasif.libs.easyqr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRScanner extends AppCompatActivity {

    private static final String TAG = "QRScanner";
    SurfaceView svCamera;
    BarcodeDetector barcode;
    CameraSource cameraSource;
    SurfaceHolder holder;
    Toolbar toolbar;
    TextView tvToolBar;
    ImageView ivToolBar;
    LinearLayout llCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        svCamera=(SurfaceView) findViewById(R.id.svCamera);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tvToolBar=(TextView) findViewById(R.id.tvToolBar);
        ivToolBar=(ImageView) findViewById(R.id.ivToolBar);
        llCamera=(LinearLayout) findViewById(R.id.llCamera);
        setUI();
        startScanning();

    }

    private void setUI() {
        Intent intent=getIntent();
        boolean showToolBar=intent.getBooleanExtra(EasyQR.IS_TOOLBAR_SHOW,false);
        String toolbar_text=intent.getStringExtra(EasyQR.TOOLBAR_TEXT);
        int toolbar_drawable_id=intent.getIntExtra(EasyQR.TOOLBAR_DRAWABLE_ID,0);
        String toolbar_background_color=intent.getStringExtra(EasyQR.TOOLBAR_BACKGROUND_COLOR);
        String toolbar_text_color=intent.getStringExtra(EasyQR.TOOLBAR_TEXT_COLOR);
        String background_color=intent.getStringExtra(EasyQR.BACKGROUND_COLOR);

        int camera_margin_left=intent.getIntExtra(EasyQR.CAMERA_MARGIN_LEFT,0);
        int camera_margin_top=intent.getIntExtra(EasyQR.CAMERA_MARGIN_TOP,0);
        int camera_margin_right=intent.getIntExtra(EasyQR.CAMERA_MARGIN_RIGHT,0);
        int camera_margin_bottom=intent.getIntExtra(EasyQR.CAMERA_MARGIN_BOTTOM,0);


        if (showToolBar){
            toolbar.setVisibility(View.VISIBLE);
            if (toolbar_drawable_id!=0){
                ivToolBar.setVisibility(View.VISIBLE);
                ivToolBar.setImageDrawable(getResources().getDrawable(toolbar_drawable_id));
            }
            else {
                ivToolBar.setVisibility(View.GONE);
            }
            if (null!=toolbar_text){
                tvToolBar.setText(toolbar_text);
            }
            if (null!=toolbar_background_color) {
                toolbar.setBackgroundColor(Color.parseColor(toolbar_background_color));
            }
            if (null!=toolbar_text_color) {
                tvToolBar.setTextColor(Color.parseColor(toolbar_text_color));
            }
        }
        else {
            toolbar.setVisibility(View.GONE);
        }

        if (null!=background_color){
            llCamera.setBackgroundColor(Color.parseColor(background_color));
        }
        llCamera.setPadding(camera_margin_left,camera_margin_top,camera_margin_right,camera_margin_bottom);
    }

    private void startScanning() {
        svCamera.setZOrderMediaOverlay(true);
        holder=svCamera.getHolder();

        barcode=new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        if(!barcode.isOperational()){
            Log.e(TAG, "onCreate: "+"Sorry! Could not setup the decoder.");
            Intent intent=new Intent();
            intent.putExtra(EasyQR.DATA,"");
            setResult(RESULT_CANCELED,intent);
            this.finish();
        }
        cameraSource=new CameraSource.Builder(this,barcode).setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920,1024)
                .build();

        svCamera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    cameraSource.start(svCamera.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }

        });

        barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes=detections.getDetectedItems();
                if(barcodes.size()>0){
                    String code=barcodes.valueAt(0).rawValue;
                    Intent intent=new Intent();
                    intent.putExtra(EasyQR.DATA,code);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }


}
