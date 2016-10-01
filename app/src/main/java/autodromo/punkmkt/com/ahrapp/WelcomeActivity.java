package autodromo.punkmkt.com.ahrapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final ImageView speedImg = (ImageView) findViewById(R.id.img_speed_lover);
        final LinearLayout background_speed_lover = (LinearLayout) findViewById(R.id.background_speed_lover);
        final TextView text_welcome = (TextView) findViewById(R.id.text_welcome);
        final String speed_lover = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("speed_lover",null);
        if(speed_lover==null){
            Intent goHome = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(goHome);
        }
        else if(speed_lover.equals("speed_lovers")){
            speedImg.setImageResource(R.drawable.speed_lovers_logo);
            background_speed_lover.setBackgroundResource(R.drawable.textura_speed_lovers);
            text_welcome.setText(getResources().getText(R.string.welcome_speed_lover));
            new loading().execute();
        }
        else if(speed_lover.equals("euphoric_fans")){
            speedImg.setImageResource(R.drawable.euphoric_fans_logo);
            background_speed_lover.setBackgroundResource(R.drawable.textura_euphoric_fans);
            text_welcome.setText(getResources().getText(R.string.welcome_euphoric_fans));
            new loading().execute();
        }
        else if(speed_lover.equals("true_racers")){
            speedImg.setImageResource(R.drawable.true_racers_logo);
            background_speed_lover.setBackgroundResource(R.drawable.textura_true_racers);
            text_welcome.setText(getResources().getText(R.string.welcome_true_racers));
            new loading().execute();
        }
        else if(speed_lover.equals("vip_party_racers")){
            speedImg.setImageResource(R.drawable.vip_party_racers_logo);
            background_speed_lover.setBackgroundResource(R.drawable.textura_vip_party_racers);
            text_welcome.setText(getResources().getText(R.string.welcome_vip_party_racers));
            new loading().execute();
        }
        else{
            Intent goHome = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(goHome);
        }




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
