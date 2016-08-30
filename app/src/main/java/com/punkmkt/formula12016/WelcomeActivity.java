package com.punkmkt.formula12016;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final ImageView speedImg = (ImageView) findViewById(R.id.img_speed_lover);
        final String speed_lover = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("speed_lover",null);
        if(speed_lover.equals("speed_lover_1")){
            speedImg.setImageResource(R.drawable.speed_lovers_logo);
        }
        else if(speed_lover.equals("speed_lover_2")){
            speedImg.setImageResource(R.drawable.speed_lovers_logo);
        }
        new loading().execute();
    }
    private class loading extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            //progressDialog.dismiss();
            try {
                Thread.sleep(1000);
                Intent goHome = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(goHome);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
