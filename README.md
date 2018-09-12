# Easy QR Code Library
A simple Android Easy QR Code Library. It is very easy to use, to use this library follow these steps.


## For Gradle:
Step 1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency:
```
dependencies {
        implementation 'com.github.mrasif:easyqrlibrary:v1.0.2'
}
```
### For Maven:
Step 1. Add the JitPack repository to your build file:
```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
Step 2. Add the dependency:
```
<dependency>
    <groupId>com.github.mrasif</groupId>
    <artifactId>easyqrlibrary</artifactId>
    <version>v1.0.2</version>
</dependency>
```
### For SBT:
Step 1. Add the JitPack repository to your build.sbt file:
```
resolvers += "jitpack" at "https://jitpack.io"
```
Step 2. Add the dependency:
```
libraryDependencies += "com.github.mrasif" % "easyqrlibrary" % "v1.0.2"
```
### For Leiningen:
Step 1. Add it in your project.clj at the end of repositories:
```
:repositories [["jitpack" "https://jitpack.io"]]
```
Step 2. Add the dependency:
```
:dependencies [[com.github.mrasif/easyqrlibrary "v1.0.2"]]
```

### Add this in your layout xml file:
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="20dp"
    tools:context=".MainActivity"
    android:orientation="vertical">

    <TextView
        android:id="@+id/tvData"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:text="No QR Data"/>
    <Button
        android:id="@+id/btnQRScan"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="QR Scan"/>

</LinearLayout>
```

### Add this in your activity java files:
```
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

```
For customized scanner screen just add these lines when you start the scanner Activity.
```
Intent intent=new Intent(MainActivity.this, QRScanner.class);
intent.putExtra(EasyQR.IS_TOOLBAR_SHOW,true);
intent.putExtra(EasyQR.TOOLBAR_DRAWABLE_ID,R.drawable.ic_audiotrack_dark);
intent.putExtra(EasyQR.TOOLBAR_TEXT,"My QR");
intent.putExtra(EasyQR.TOOLBAR_BACKGROUND_COLOR,"#0588EE");
intent.putExtra(EasyQR.TOOLBAR_TEXT_COLOR,"#FFFFFF");
intent.putExtra(EasyQR.BACKGROUND_COLOR,"#000000");
intent.putExtra(EasyQR.CAMERA_MARGIN_LEFT,50);
intent.putExtra(EasyQR.CAMERA_MARGIN_TOP,50);
intent.putExtra(EasyQR.CAMERA_MARGIN_RIGHT,50);
intent.putExtra(EasyQR.CAMERA_MARGIN_BOTTOM,50);
intent.putExtra(EasyQR.CAMERA_BORDER,100);
intent.putExtra(EasyQR.CAMERA_BORDER_COLOR,"#C1000000");
intent.putExtra(EasyQR.IS_SCAN_BAR,true);
intent.putExtra(EasyQR.IS_BEEP,true);
// BEEP_RESOURCE_ID is optional
intent.putExtra(EasyQR.BEEP_RESOURCE_ID,R.raw.beep);
startActivityForResult(intent, EasyQR.QR_SCANNER_REQUEST);
```

You are done.