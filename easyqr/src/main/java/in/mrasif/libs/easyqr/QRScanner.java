package in.mrasif.libs.easyqr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    RelativeLayout rlCamera;
    View vLeft, vTop, vRight, vBottom;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        svCamera=(SurfaceView) findViewById(R.id.svCamera);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tvToolBar=(TextView) findViewById(R.id.tvToolBar);
        ivToolBar=(ImageView) findViewById(R.id.ivToolBar);
        rlCamera=(RelativeLayout) findViewById(R.id.llCamera);
        vLeft=(View) findViewById(R.id.vLeft);
        vTop=(View) findViewById(R.id.vTop);
        vRight=(View) findViewById(R.id.vRight);
        vBottom=(View) findViewById(R.id.vBottom);

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

        int camera_border=intent.getIntExtra(EasyQR.CAMERA_BORDER,0);
        String camera_border_color=intent.getStringExtra(EasyQR.CAMERA_BORDER_COLOR);
        boolean is_scan_bar=intent.getBooleanExtra(EasyQR.IS_SCAN_BAR,false);


        boolean is_beep=intent.getBooleanExtra(EasyQR.IS_BEEP,false);
        int beep_resource_id=intent.getIntExtra(EasyQR.BEEP_RESOURCE_ID,0);


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
            rlCamera.setBackgroundColor(Color.parseColor(background_color));
        }

        if (camera_border>0){
            RelativeLayout.LayoutParams params1=(RelativeLayout.LayoutParams) vLeft.getLayoutParams();
            params1.width=camera_border;
            params1.setMargins(0,0,0,camera_border);
            vLeft.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2=(RelativeLayout.LayoutParams) vTop.getLayoutParams();
            params2.height=camera_border;
            params2.setMargins(camera_border,0,0,0);
            vTop.setLayoutParams(params2);

            RelativeLayout.LayoutParams params3=(RelativeLayout.LayoutParams) vRight.getLayoutParams();
            params3.width=camera_border;
            params3.setMargins(0,camera_border,0,0);
            vRight.setLayoutParams(params3);

            RelativeLayout.LayoutParams params4=(RelativeLayout.LayoutParams) vBottom.getLayoutParams();
            params4.height=camera_border;
            params4.setMargins(0,0,camera_border,0);
            vBottom.setLayoutParams(params4);

            if(null!=camera_border_color){
                vLeft.setBackgroundColor(Color.parseColor(camera_border_color));
                vTop.setBackgroundColor(Color.parseColor(camera_border_color));
                vRight.setBackgroundColor(Color.parseColor(camera_border_color));
                vBottom.setBackgroundColor(Color.parseColor(camera_border_color));
            }
        }

        rlCamera.setPadding(camera_margin_left,camera_margin_top,camera_margin_right,camera_margin_bottom);
        if (is_scan_bar) {
            startAnimation();
        }
        if (is_beep) {
            if (beep_resource_id>0){
                mp = MediaPlayer.create(getApplicationContext(), beep_resource_id);
            }
            else {
                mp = MediaPlayer.create(getApplicationContext(), R.raw.beep);
            }
        }
    }

    private void startAnimation() {
        final View vScanBar = findViewById(R.id.vScanBar);
        vScanBar.setVisibility(View.VISIBLE);
        final Animation animation = AnimationUtils.loadAnimation(QRScanner.this, R.anim.anim_scan_effect);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vScanBar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        vScanBar.startAnimation(animation);

    }

    private void startScanning() {
        svCamera.setZOrderMediaOverlay(true);
        holder=svCamera.getHolder();

        barcode=new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        if(!barcode.isOperational()){
            Log.e(TAG, "onCreate: "+"Sorry! Could not setup the decoder or do not have camera permission.");
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
                    if (null!=mp){
                        mp.start();
                    }
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
