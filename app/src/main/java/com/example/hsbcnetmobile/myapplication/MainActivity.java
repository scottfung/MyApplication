package com.example.hsbcnetmobile.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button getBtn;
    private TextView result;
    private OkHttpClient client;
    EditText codeTxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);
        getBtn = (Button) findViewById(R.id.getBtn);
        codeTxt = (EditText) findViewById(R.id.codeTxt);



        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int code = Integer.parseInt(codeTxt.getText().toString());
//                Log.i("MainActivity",Integer.toString(code));
                final long period = 1000;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getWebservice();        // do your task here
                    }
                }, 0, period);

            }
        });

        client = new OkHttpClient();
    }

    public String digestBody(String raw){
        int StrBeginIndex = raw.indexOf('际')+2;
        int StrEndIndex = StrBeginIndex+4;
        return raw.substring(StrBeginIndex,StrEndIndex);
    }

    public String getTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }



    private void getWebservice() {
        final Request request = new Request.Builder().url("http://quotese.etnet.com.hk/content/mq3/wl_hkStockCollapse.php?code=354").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText("Failure");
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String Str = response.body().string();
                            result.setText(digestBody(Str)+" "+getTime());
                            //Log.i("MainActivity",price);
                        } catch (IOException ioe){
                            result.setText("Error during get body");
                        }
                    }
                });
            }
        });
    }
}

