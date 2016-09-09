package com.punkmkt.formula12016;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.punkmkt.formula12016.fragments.AutodromoFragment;
import com.punkmkt.formula12016.fragments.ConfiguracionFragment;
import com.punkmkt.formula12016.fragments.HomeFragment;
import com.punkmkt.formula12016.fragments.HorariosFragment;
import com.punkmkt.formula12016.fragments.LaciudadFragment;
import com.punkmkt.formula12016.fragments.NoticiasFragment;
import com.punkmkt.formula12016.fragments.Pasion_f1;
import com.punkmkt.formula12016.fragments.PilotosFragment;
import com.punkmkt.formula12016.fragments.ResultadosFragment;
import com.punkmkt.formula12016.fragments.SocialHubFragment;
import com.punkmkt.formula12016.fragments.TiendaFragment;
import com.umbel.UmbelSDK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    static public LruCache<String, Bitmap> mMemoryCache;
    String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UmbelSDK.setup("yeabdfystgordvmg", this);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
        UmbelSDK.setup("xxxxxxxx", this);
        Intent intent = getIntent();
        String inf = intent.getStringExtra("fragment");




        if (savedInstanceState == null && inf== null) {
            Fragment f1 = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame, f1).commit();
        }
        else if(savedInstanceState == null && inf!="" && inf!=null){
            Log.d(TAG,inf);
            if(inf.equals("select-photo")){
                Fragment fH = new Pasion_f1();
                FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
          //      this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
 //       } else {
 //           super.onBackPressed();
  //      }
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Fragment fH = new ConfiguracionFragment();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
        }

        if (id == R.id.action_login) {
            Intent intent_login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent_login);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
        //Closing drawer on item click
        drawerLayout.closeDrawers();

        if (id == R.id.nav_home) {
            //getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Fragment fH = new HomeFragment();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
            return true;
        } else if (id == R.id.nav_autodromo) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Fragment fH = new AutodromoFragment();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
            return true;
        } else if (id == R.id.nav_horarios) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Fragment fH = new HorariosFragment();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
        } else if (id == R.id.nav_noticias) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Fragment fH = new NoticiasFragment();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
        } else if (id == R.id.nav_resultados) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Fragment fH = new ResultadosFragment();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
        } else if (id == R.id.nav_pilotos) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Fragment fH = new PilotosFragment();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
        }
        else if (id == R.id.nav_pasion_f1) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Fragment fH = new Pasion_f1();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
        }
        else if (id == R.id.nav_la_ciudad) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Fragment fH = new LaciudadFragment();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
        }
        else if (id == R.id.nav_social_hub) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Fragment fH = new SocialHubFragment();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
        }
        else if (id == R.id.nav_store) {
            Log.d("main","store");
            Boolean isFirstRunShop = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getBoolean("isFirstRunShop", true);

            //isFirstRunShop = true;
            if (isFirstRunShop) {
                Intent tiendaIntent = new Intent(getApplicationContext(), TutorialTiendaActivity.class);
                tiendaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(tiendaIntent);
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                        .putBoolean("isFirstRunShop", false).commit();
            }else{
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_tienda));
                Fragment fT = new TiendaFragment();
                FragmentTransaction ftT = getSupportFragmentManager().beginTransaction();
                ftT.replace(R.id.frame, fT);
                ftT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftT.addToBackStack(null);
                ftT.commit();
            }
        }
        else if (id == R.id.nav_beyond_the_track) {

        }
//        else if (id == R.id.nav_iniciar_sesion) {
//            Intent intent_login = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent_login);
//        }
//        else if (id == R.id.nav_configuracion) {
//            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
//            Fragment fH = new ConfiguracionFragment();
//            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
//            ftH.replace(R.id.frame, fH);
//            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ftH.addToBackStack(null);
//            ftH.commit();
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}



