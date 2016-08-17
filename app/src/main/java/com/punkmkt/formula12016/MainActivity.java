package com.punkmkt.formula12016;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.punkmkt.formula12016.fragments.HomeFragment;
import com.punkmkt.formula12016.fragments.HorariosFragment;
import com.punkmkt.formula12016.fragments.Pasion_f1;
import com.punkmkt.formula12016.fragments.PilotosFragment;
import com.punkmkt.formula12016.fragments.ResultadosFragment;
import com.punkmkt.formula12016.fragments.SocialHubFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Fragment f1 = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame, f1).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
            return true;
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
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
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

        }
        else if (id == R.id.nav_beyond_the_track) {

        }
        else if (id == R.id.nav_iniciar_sesion) {

        }
        else if (id == R.id.nav_configuracion) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}