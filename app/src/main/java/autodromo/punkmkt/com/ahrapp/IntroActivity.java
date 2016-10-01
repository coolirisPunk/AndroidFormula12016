package autodromo.punkmkt.com.ahrapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        //getSupportActionBar().hide();
        //getActionBar().hide();
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        //isFirstRun = true;
        if (isFirstRun) {
            Intent myIntent = new Intent(getApplicationContext(), ConfActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(myIntent);
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
        }
        else{
            new loading().execute();
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
                final String speed_lover = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("speed_lover",null);
                if(speed_lover!=null){
                    Intent goWelcome = new Intent(IntroActivity.this, WelcomeActivity.class);
                    startActivity(goWelcome);
                }
                else{
                    Intent goHome = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(goHome);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}