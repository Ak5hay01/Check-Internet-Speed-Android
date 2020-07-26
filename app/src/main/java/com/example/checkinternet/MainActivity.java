package com.example.checkinternet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private final OkHttpClient nclient = new OkHttpClient();
    private long startTime;
    private long endTime;
    private long fileSize;
    // Bandwidth range in kbps copied from FBConnect Class
    private int POOR_BANDWIDTH = 20;
    private int AVERAGE_BANDWIDTH = 550;
    private int GOOD_BANDWIDTH = 2000;
    boolean userDataMustDelete = false;
    private static final String TAG = "";

    private TextView mConnected;
    private TextView mSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnected = findViewById(R.id.txtConnect);
        mSpeed = findViewById(R.id.txtNetworkSpeed);

//        Regular check when no internet available
        if(isConnected(this)) {
            mConnected.setText("Connected");
            mConnected.setTextColor(getResources().getColor(R.color.success));
            downloadInfo();
        }
        else{
            mConnected.setText("No active internet,\n Please connect to mobile network or WiFI");
            mConnected.setTextColor(getResources().getColor(R.color.error));
            mSpeed.setText("0");
        }

    }

    private void downloadInfo() {
        Log.d(TAG, "downloadInfo: iN downloadInfo");
        Request request = new Request.Builder()
                .url("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png") // replace image url
                .build();
        startTime = System.currentTimeMillis();
        nclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "downloadInfo: iN failure");
                //check when device is connected to Router but there is no internet
                mConnected.setText("Device connected to internet/ Router but no Internet detected");
                mConnected.setTextColor(getResources().getColor(R.color.error));
                mSpeed.setText("0");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "downloadInfo: iN success");
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                InputStream input = response.body().byteStream();
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    while (input.read(buffer) != -1) {
                        bos.write(buffer);
                    }
                    byte[] docBuffer = bos.toByteArray();
                    fileSize = bos.size();
                } finally {
                    input.close();
                }
                endTime = System.currentTimeMillis();
                // calculate how long it took by subtracting endtime from starttime
                final double timeTakenMills = Math.floor(endTime - startTime);  // time taken in milliseconds
                final double timeTakenInSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds
                final int kilobytePerSec = (int) Math.round(1024 / timeTakenInSecs);
                final double speed = Math.round(fileSize / timeTakenMills);
                Log.d(TAG, "Time taken in secs: " + timeTakenInSecs);
                Log.d(TAG, "Kb per sec: " + kilobytePerSec);
                Log.d(TAG, "Download Speed: " + speed);
                Log.d(TAG, "File size in kb: " + fileSize);
                // update the UI with the speed test results
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (kilobytePerSec <= POOR_BANDWIDTH) {
                            // slow connection
                            mConnected.setText("Connected");
                            mSpeed.setText("Slow Connection: \nyour speed in kilobytePerSec is "  + kilobytePerSec );
                            mConnected.setTextColor(getResources().getColor(R.color.success));
                            mSpeed.setTextColor(getResources().getColor(R.color.success));
                        } else if (kilobytePerSec <= AVERAGE_BANDWIDTH) {
                            // Average connection
                            mConnected.setText("Connected");
                            mSpeed.setText("Average Connection: \nyour speed in kilobytePerSec is "  + kilobytePerSec );
                            mConnected.setTextColor(getResources().getColor(R.color.success));
                            mSpeed.setTextColor(getResources().getColor(R.color.success));
                        }
                        else if (kilobytePerSec <= GOOD_BANDWIDTH){
                            //Good connection
                            mConnected.setText("Connected");
                            mSpeed.setText("Good Connection: \nyour speed in kilobytePerSec is "  + kilobytePerSec );
                            mConnected.setTextColor(getResources().getColor(R.color.success));
                            mSpeed.setTextColor(getResources().getColor(R.color.success));
                        }
                    }
                });
            }
        });
    }

    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr != null) {
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                return (networkInfo != null && networkInfo.isConnected());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
