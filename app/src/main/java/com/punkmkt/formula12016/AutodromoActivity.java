package com.punkmkt.formula12016;

import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.punkmkt.formula12016.fragments.ComoLlegarFragment;
import com.punkmkt.formula12016.fragments.ServiciosFragment;
import com.punkmkt.formula12016.fragments.UberFragment;
import com.punkmkt.formula12016.fragments.UbicaAsientoDialogFragment;
import com.punkmkt.formula12016.fragments.UbicaTuAsientoFragment;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutodromoActivity extends FragmentActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autodromo);
        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("ltiTmkxxyYbvWWC02eufPwwsfaTCjGYT") //This is necessary
                .setRedirectUri("YOUR_REDIRECT_URI") //This is necessary if you'll be using implicit grant
                .setEnvironment(SessionConfiguration.Environment.SANDBOX) //Useful for testing your app in the sandbox environment
                .setScopes(Arrays.asList(Scope.PROFILE)) //Your scopes for authentication here
                .build();

//This is a convenience method and will set the default config to be used in other components without passing it directly.
        UberSdk.initialize(config);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        fragmentManager = getSupportFragmentManager();

    }

    private void setupTabIcons() {

        View view1 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_como_llegar_icon_hover);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_asiento_icon);
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_servicios_icon);
        tabLayout.getTabAt(2).setCustomView(view3);

        View view4 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_uber_icon);
        tabLayout.getTabAt(3).setCustomView(view4);


        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        tab.getPosition();
                        switch (tab.getPosition()){
                            case 0:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_como_llegar_icon_hover);
                                break;
                            case 1:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_asiento_icon_hover);
                            break;
                            case 2:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_servicios_icon_hover);
                                break;
                            case 3:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_uber_icon_hover);
                                break;
                        }
                        viewPager.setCurrentItem(tab.getPosition());

                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        switch (tab.getPosition()){
                            case 0:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_como_llegar_icon);
                                break;
                            case 1:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_asiento_icon);
                                break;
                            case 2:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_servicios_icon);
                                break;
                            case 3:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_uber_icon);
                                break;
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ComoLlegarFragment(), getResources().getString(R.string.como_llegar));
        adapter.addFragment(new UbicaTuAsientoFragment(), getResources().getString(R.string.ubica_tu_asiento));
        adapter.addFragment(new ServiciosFragment(), getResources().getString(R.string.servicios));
        adapter.addFragment(new UberFragment(), getResources().getString(R.string.uber));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
