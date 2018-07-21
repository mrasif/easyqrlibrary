package in.mrasif.app.easyqrlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.mrasif.libs.easyqr.EasyQR;
import in.mrasif.libs.easyqr.QRScanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvData;
    Button btnQRScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvData=findViewById(R.id.tvData);
        btnQRScan=findViewById(R.id.btnQRScan);

        btnQRScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnQRScan: {
                Intent intent=new Intent(MainActivity.this, QRScanner.class);
//                intent.putExtra(EasyQR.IS_TOOLBAR_SHOW,true);
//                intent.putExtra(EasyQR.TOOLBAR_DRAWABLE_ID,R.drawable.ic_audiotrack_dark);
//                intent.putExtra(EasyQR.TOOLBAR_TEXT,"My QR");
//                intent.putExtra(EasyQR.TOOLBAR_BACKGROUND_COLOR,"#0588EE");
//                intent.putExtra(EasyQR.TOOLBAR_TEXT_COLOR,"#FFFFFF");
//                intent.putExtra(EasyQR.BACKGROUND_COLOR,"#000000");
//                intent.putExtra(EasyQR.CAMERA_MARGIN_LEFT,50);
//                intent.putExtra(EasyQR.CAMERA_MARGIN_TOP,50);
//                intent.putExtra(EasyQR.CAMERA_MARGIN_RIGHT,50);
//                intent.putExtra(EasyQR.CAMERA_MARGIN_BOTTOM,50);
                startActivityForResult(intent, EasyQR.QR_SCANNER_REQUEST);
            } break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EasyQR.QR_SCANNER_REQUEST: {
                if (resultCode==RESULT_OK){
                    tvData.setText(data.getStringExtra(EasyQR.DATA));
                }
            } break;
        }
    }
}
