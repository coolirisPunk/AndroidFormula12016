package autodromo.punkmkt.com.ahrapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import autodromo.punkmkt.com.ahrapp.fragments.AutodromoFragment;
import autodromo.punkmkt.com.ahrapp.fragments.BeyondTheTrackFragment;
import autodromo.punkmkt.com.ahrapp.fragments.ConfiguracionFragment;
import autodromo.punkmkt.com.ahrapp.fragments.HomeFragment;
import autodromo.punkmkt.com.ahrapp.fragments.HorariosFragment;
import autodromo.punkmkt.com.ahrapp.fragments.LaciudadFragment;
import autodromo.punkmkt.com.ahrapp.fragments.NoticiasFragment;
import autodromo.punkmkt.com.ahrapp.fragments.Pasion_f1;
import autodromo.punkmkt.com.ahrapp.fragments.PilotosFragment;
import autodromo.punkmkt.com.ahrapp.fragments.ResultadosFragment;
import autodromo.punkmkt.com.ahrapp.fragments.SocialHubFragment;
import autodromo.punkmkt.com.ahrapp.fragments.TiendaFragment;
import com.umbel.UmbelSDK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    String TAG = MainActivity.class.getName();
    String shared_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shared_token = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("access_token", null);

        UmbelSDK.setup("yeabdfystgordvmg", this);


        UmbelSDK.setup("xxxxxxxx", this);
        Intent intent = getIntent();
        String inf = intent.getStringExtra("fragment");

        /*try{
            if(inf.equals("noticias")){
                if (savedInstanceState == null) {
                    Fragment f1 = new NoticiasFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, f1).commit();
                }
            }
            else if(inf.equals("mexico")){
                if (savedInstanceState == null) {
                    Fragment f1 = new LaciudadFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, f1).commit();
                }
            }
            else if (inf.equals("tiendaRestaurantes")){
                if (savedInstanceState == null) {
                    Fragment f1 = new TiendaFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, f1).commit();
                }
            }
        }catch (Exception e){
            if (savedInstanceState == null) {
                Fragment f1 = new HomeFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, f1).commit();

            }
        }*/


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
            else if(inf.equals("tiendaRestaurantes")){
                Fragment fH = new TiendaFragment();
                FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            }
            else if(inf.equals("horarios")){
                Fragment fH = new HorariosFragment();
                FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            }
            else if(inf.equals("noticias")){
                Fragment fH = new NoticiasFragment();
                FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            }
            else if(inf.equals("resultados")){
                Fragment fH = new ResultadosFragment();
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



        //hide beyond the track

        if(shared_token==null){
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_beyond_the_track).setVisible(false);
            Log.d("mainac","esta");
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //MenuItem item = menu.getItem(1).setVisible(false);
        if(shared_token!=null){
            menu.getItem(1).setTitle(getResources().getString(R.string.logout));
        }
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
            if(shared_token!=null){
                intent_login.putExtra("intent_from","LOGOUT");

            }
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
            Fragment fH = new BeyondTheTrackFragment();
            FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
            ftH.replace(R.id.frame, fH);
            ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftH.addToBackStack(null);
            ftH.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}



