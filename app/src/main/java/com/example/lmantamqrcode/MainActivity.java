package com.example.lmantamqrcode;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


// implements onClickListener for the onclick behaviour of button
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn;
    private static Context context;
    String message_result = "";
    String message_result2= "";
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();




        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.scanBtn);

        checkGPSandInternet();



        // adding listener to the button
        scanBtn.setOnClickListener(this);


    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    public void checkGPSandInternet(){
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(this)
                    .setMessage(R.string.gps_network_not_enabled)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            MainActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.Cancel,null)
                    .show();
        }
    }


    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if( requestCode != REQUEST_IMAGE_CAPTURE) {
            message_result = intentResult.getContents();
            message_result2 = intentResult.getFormatName();
        }

        if( requestCode != REQUEST_IMAGE_CAPTURE) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra("android.intent.extra.quickCapture", true);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
                {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    newActivity(message_result, message_result2,photo);
                }
                //messageText.setText(intentResult.getContents());
                //messageFormat.setText(intentResult.getFormatName());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void newActivity(String message1, String message2, Bitmap bitmap){
        Intent intent = new Intent(this, HasilScan_Activity.class);
        intent.putExtra("m1",message1);
        intent.putExtra("m2",message2);
        intent.putExtra("BitmapImage",bitmap);
        startActivity(intent);

    }
}