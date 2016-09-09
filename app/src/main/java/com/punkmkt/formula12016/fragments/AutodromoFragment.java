package com.punkmkt.formula12016.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.punkmkt.formula12016.NonSwipeableViewPager;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.adapters.ViewPagerAdapter;

import java.util.List;

/**
 * Created by germanpunk on 16/08/16.
 */
public class AutodromoFragment extends Fragment {
    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;
    int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 4;
    String TAG = AutodromoFragment.class.getName();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_autodromo, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            }else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    Toast.makeText(getActivity().getApplicationContext(),"Your Permission is needed to get access the location",Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        }
        viewPager = (NonSwipeableViewPager) v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setupTabIcons() {

        View view1 = getActivity().getLayoutInflater().inflate(R.layout.custom_tab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_como_llegar_icon_hover);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = getActivity().getLayoutInflater().inflate(R.layout.custom_tab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_asiento_icon);
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = getActivity().getLayoutInflater().inflate(R.layout.custom_tab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.autodromo_servicios_icon);
        tabLayout.getTabAt(2).setCustomView(view3);

        View view4 = getActivity().getLayoutInflater().inflate(R.layout.custom_tab, null);
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

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ComoLlegarFragment(), getResources().getString(R.string.como_llegar));
        adapter.addFragment(new UbicaTuAsientoFragment(), getResources().getString(R.string.ubica_tu_asiento));
        adapter.addFragment(new ServiciosFragment(), getResources().getString(R.string.servicios));
        adapter.addFragment(new UberFragment(), getResources().getString(R.string.uber));
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, String.valueOf(requestCode));
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION && resultCode == getActivity().RESULT_OK) {
            Log.d(TAG,"location ok");
        }
    }

}
