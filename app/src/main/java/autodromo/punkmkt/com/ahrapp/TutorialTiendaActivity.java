package autodromo.punkmkt.com.ahrapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import autodromo.punkmkt.com.ahrapp.adapters.TiendaPageAdapter;

/**
 * Created by sebastianmendezgiron on 05/08/16.
 */

public class TutorialTiendaActivity extends AppCompatActivity {

    private ViewPager myPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conf);
        TiendaPageAdapter pageAdapter = new TiendaPageAdapter(getResources(),this);
        myPager = (ViewPager) findViewById(R.id.pager);
        myPager.setAdapter(pageAdapter);
        myPager.setCurrentItem(0);

    }

    private int getItem(int i) {
        return myPager.getCurrentItem() + i;
    }

    public void NextViewPager(View v){
        myPager.setCurrentItem(getItem(+1),true);
    }

    public void BackViewPager(View v){
        myPager.setCurrentItem(getItem(-1), true);
    }

    public void EndViewPager(View v){
        Intent myIntent = new Intent(TutorialTiendaActivity.this, MainActivity.class);
        myIntent.putExtra("fragment","tiendaRestaurantes");
        this.startActivity(myIntent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindDrawables(findViewById(R.id.container));
        System.gc();
    }
    public void unbindDrawables(View view) {//pass your parent view here
        try {
            if (view.getBackground() != null)
                view.getBackground().setCallback(null);

            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(null);
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    unbindDrawables(viewGroup.getChildAt(i));

                if (!(view instanceof AdapterView))
                    viewGroup.removeAllViews();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}