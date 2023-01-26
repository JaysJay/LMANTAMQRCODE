package com.example.lmantamqrcode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HasilScan_Activity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView qrCodes,produk,thn_produk,webAntam,webPro,lokasi,kd_hp, tipHP,webantam2,webpro2;
    private TextView tv,tv2,tv3,tv4,tv5,tv6,linkproduk,imei,tv7,linkproduk2;
    private TextView tf;
    private ImageView image,image2;
    private String latlong;
    private ProgressBar progressBar;
    private Bitmap bitmap;
    String m_szDevIDShort;
    String m1,m2;
    Double[] test = new Double[2];

    private final String baseURL = "https://lmantam.iplogika.co.id/";

    private JsonPlaceHolderAPI jsonPlaceHolderAPI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_scan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkGPSandInternet();

        Intent intent = getIntent();
        //qr codenya
        m1 = intent.getStringExtra("m1");
        //jenis qr code
        m2 = intent.getStringExtra("m2");

        bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");

        inisialisasi();
        loading();



        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);


        m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits

        kd_hp.setText(": "+m_szDevIDShort);

        loadLocation();



        //Log.e("Runnable B", test[0].toString()+test[1].toString() );
        //getData(m1,test[0],test[1],m_szDevIDShort);
    }

    private void inisialisasi(){
        progressBar = findViewById(R.id.progress);

        //hasil data
        image = findViewById(R.id.image);
        qrCodes = findViewById(R.id.qrCode);
        produk = findViewById(R.id.produk);
        thn_produk = findViewById(R.id.thn_prod);
        webAntam = findViewById(R.id.webAntam);
        webPro = findViewById(R.id.webPro);
        lokasi = findViewById(R.id.lokasi);
        kd_hp = findViewById(R.id.kd_hp);
        tipHP = findViewById(R.id.tipeHp);

        //teks untuk hasil data
        tv = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);
        tv5 = findViewById(R.id.textView5);
        tv6 = findViewById(R.id.textView6);
        linkproduk = findViewById(R.id.linkProduk);
        imei = findViewById(R.id.IMEI);

        //no data
        tf = findViewById(R.id.textFormat);
        image2 = findViewById(R.id.imageView2);
        linkproduk2 = findViewById(R.id.linkProduk2);
        tv7 = findViewById(R.id.textView7);
        webantam2 = findViewById(R.id.webAntam2);
        webpro2 = findViewById(R.id.webPro2);



    }

    private void loading(){
        tf.setVisibility(View.VISIBLE);
        tf.setText("Tunggu Sebentar...");
    }

    private void noData(){
        image.setVisibility(View.GONE);
        qrCodes.setVisibility(View.GONE);
        produk.setVisibility(View.GONE);
        thn_produk.setVisibility(View.GONE);
        webAntam.setVisibility(View.GONE);
        webPro.setVisibility(View.GONE);
        lokasi.setVisibility(View.GONE);
        kd_hp.setVisibility(View.GONE);
        tipHP.setVisibility(View.GONE);

        tv.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);
        tv4.setVisibility(View.GONE);
        tv5.setVisibility(View.GONE);
        tv6.setVisibility(View.GONE);
        linkproduk.setVisibility(View.GONE);
        imei.setVisibility(View.GONE);

        //
        tf.setVisibility(View.VISIBLE);
        image2.setVisibility(View.VISIBLE);
        linkproduk2.setVisibility(View.VISIBLE);
        tv7.setVisibility(View.VISIBLE);
        webantam2.setVisibility(View.VISIBLE);
        webpro2.setVisibility(View.VISIBLE);


        tf.setText("QR Code Tidak Valid");

        SpannableString text1 = new SpannableString(": "+"Halaman utama LM ANTAM");
        text1.setSpan(new UnderlineSpan(), 02, text1.length(), 0);
        webantam2.setText(text1);

        SpannableString text2 = new SpannableString(": "+"Pembelian Emas LM Antam");
        text2.setSpan(new UnderlineSpan(), 2, text2.length(), 0);
        webpro2.setText(text2);


        webantam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.logammulia.com/id"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        webpro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.logammulia.com/id/purchase/gold"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void adaData(){

        //
        tf.setVisibility(View.GONE);
        image2.setVisibility(View.GONE);
        linkproduk2.setVisibility(View.GONE);
        tv7.setVisibility(View.GONE);
        webantam2.setVisibility(View.GONE);
        webpro2.setVisibility(View.GONE);

        //
        image.setVisibility(View.VISIBLE);
        qrCodes.setVisibility(View.VISIBLE);
        produk.setVisibility(View.VISIBLE);
        thn_produk.setVisibility(View.VISIBLE);
        webAntam.setVisibility(View.VISIBLE);
        webPro.setVisibility(View.VISIBLE);
        //lokasi.setVisibility(View.VISIBLE);
        //kd_hp.setVisibility(View.VISIBLE);
        //tipHP.setVisibility(View.VISIBLE);

        //tv.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.VISIBLE);
        tv3.setVisibility(View.VISIBLE);
        tv4.setVisibility(View.VISIBLE);
        tv5.setVisibility(View.VISIBLE);
        //tv6.setVisibility(View.VISIBLE);
        linkproduk.setVisibility(View.VISIBLE);
        //imei.setVisibility(View.VISIBLE);


        
    }

    public static String getPsuedoUniqueID()
    {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" +
                (Build.BOARD.length() % 10)
                + (Build.BRAND.length() % 10)
                + (Build.CPU_ABI.length() % 10)
                + (Build.DEVICE.length() % 10)
                + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10)
                + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their phone, there will be a duplicate entry
        String serial = null;
        try
        {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        }
        catch (Exception e)
        {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
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
                            HasilScan_Activity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AlertDialog.Builder(HasilScan_Activity.this)
                                    .setMessage("Tidak Dapat Melakukan Request")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                            Intent intent = new Intent(HasilScan_Activity.this,MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    })
                    .show();
        }
    }

    public void getData(String qrCode, double latitude, double longitude, String imei, String lokasii, String tphp){
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("no_qrcode", qrCode);
        params.put("lat", String.valueOf(latitude));
        params.put("long", String.valueOf(longitude));
        params.put("imei", imei);
        params.put("lokasi", lokasii);
        params.put("platform", tphp);

        Call<Post> posts = jsonPlaceHolderAPI.postMessage(params);


        posts.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.body() != null){
                    adaData();

                    Toast.makeText(HasilScan_Activity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                    Post responseData = response.body();

                    Picasso.get().load(baseURL+responseData.getData().get(0).getPath()).into(image);
                    qrCodes.setText(": "+responseData.getData().get(0).getNoQrcode());
                    qrCodes.setText(": "+responseData.getData().get(0).getNoQrcode());
                    produk.setText("No seri :" +responseData.getData().get(0).getSerial() + "\n"+
                            "Berat : "+responseData.getData().get(0).getBerat());
                    thn_produk.setText(": "+responseData.getData().get(0).getTahun());

                    SpannableString text1 = new SpannableString(": "+"Halaman utama LM ANTAM");
                    text1.setSpan(new UnderlineSpan(), 02, text1.length(), 0);
                    webAntam.setText(text1);

                    SpannableString text2 = new SpannableString(": "+"Pembelian Emas LM Antam");
                    text2.setSpan(new UnderlineSpan(), 2, text2.length(), 0);
                    webPro.setText(text2);


                    webAntam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse("https://www.logammulia.com/id"); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });

                    webPro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse("https://www.logammulia.com/id/purchase/gold"); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });

                }else{
                    noData();
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                String err = "Error " + t.getMessage();
                Toast.makeText(HasilScan_Activity.this, err, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void getCoor(double lat, double longi){
        test[0] = lat;
        test[1] = longi;
    }

    public void getMaps(String lat, String longi){
        Uri gmmIntentUri = Uri.parse("geo:"+lat+","+longi+"?z=16");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    public void loadLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HasilScan_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCurrentLocation() {
        progressBar.setVisibility(View.VISIBLE);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
        LocationServices.getFusedLocationProviderClient(HasilScan_Activity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(HasilScan_Activity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int lastestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitide = locationResult.getLocations().get(lastestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(lastestLocationIndex).getLongitude();

                            getCoor(latitide,longitude);



                            latlong = String.format("Latitude : %s\nLongitude : %s",latitide,longitude);
                            String address = getAddress(latitide,longitude);
                            String address2 = getFullAddress(latitide,longitude);

                            //get data
                            getData(m1,test[0],test[1],m_szDevIDShort, address2,"Android");

                            SpannableString text = new SpannableString(latlong + "\n" +
                                    address);

                            text.setSpan(new UnderlineSpan(), 0, text.length(), 0);
                            lokasi.setText(text);
/*
                            lokasi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getMaps(String.valueOf(latitide),String.valueOf(longitude));
                                }
                            });

 */
                            progressBar.setVisibility(View.GONE);

                        }else{
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                }, Looper.getMainLooper());

    }

    public String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n"); // kecamatan
                //result.append(address.getAddressLine(0)).append("\n"); // area lokasi
                //result.append(address.getAdminArea()).append("\n"); // provinsi
                //result.append(address.getPostalCode()).append("\n"); // kode pos
                //result.append(address.getFeatureName()).append("\n"); // gak tau ini apa
                result.append(address.getCountryName()); // negara
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    public String getFullAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0)).append("\n");// area lokasi
                //result.append(address.getLocality()).append("\n"); // kecamatan
                //result.append(address.getAdminArea()).append("\n"); // provinsi
                //result.append(address.getPostalCode()).append("\n"); // kode pos
                //result.append(address.getFeatureName()).append("\n"); // gak tau ini apa
                //result.append(address.getCountryName()); // negara
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }
}