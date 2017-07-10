package com.example.hsbcnetmobile.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private TextView result2;
    private OkHttpClient client;
    EditText codeTxt;
    Timer timer = new Timer(true);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);
        result2 = (TextView) findViewById(R.id.result2);
//        getBtn = (Button) findViewById(R.id.getBtn);
        codeTxt = (EditText) findViewById(R.id.codeTxt);


//        getBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //int code = Integer.parseInt(codeTxt.getText().toString());
//                //Log.i("MainActivity",Integer.toString(code));
//                final long period = 1000;
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        getWebservice();        // do your task here
//                    }
//                }, 0, period);
//            }
//        });


        client = new OkHttpClient();

        final long period = 1000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getWebservice();
                getWebservice2();
            }
        }, 0, period);

    }

    public String digestBody(String raw){
        Log.i("MainActivity",String.valueOf(nthIndexOf(raw,',',23)));
        //int StrBeginIndex = raw.indexOf('际')+2;
        int StrBeginIndex = nthIndexOf(raw,',',23)+1;
        int StrEndIndex = nthIndexOf(raw,',',24);
        return raw.substring(StrBeginIndex,StrEndIndex);
    }

    public String getTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static int nthIndexOf(String s, char c, int n) {
        int i = -1;
        while (n-- > 0) {
            i = s.indexOf(c, i + 1);
            if (i == -1)
                break;
        }
        return i;
    }


    private void getWebservice() {
        String rawCode = codeTxt.getText().toString();
        if (rawCode.equals("") == true){rawCode="1";}
        int code = Integer.parseInt(rawCode);
        String codeURL = Integer.toString(code);
        Log.i("MainActivity",codeURL);
        final Request request = new Request.Builder().url("http://quotese.etnet.com.hk/content/mq3/wl_hkStockCollapse.php?code="+codeURL).build();
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

    private void getWebservice2() {
        final Request request = new Request.Builder().url("http://quotese.etnet.com.hk/content/mq3/wl_hkStockCollapse.php?code=700").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result2.setText("Failure");
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
                            result2.setText(digestBody(Str)+" "+getTime());
                            //Log.i("MainActivity",price);
                        } catch (IOException ioe){
                            result2.setText("Error during get body");
                        }
                    }
                });
            }
        });
    }


}

