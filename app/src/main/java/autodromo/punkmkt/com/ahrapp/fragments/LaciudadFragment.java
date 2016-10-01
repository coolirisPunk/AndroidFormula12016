package autodromo.punkmkt.com.ahrapp.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import autodromo.punkmkt.com.ahrapp.NonSwipeableViewPager;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.adapters.ViewPagerAdapter;

/**
 * Created by germanpunk on 30/08/16.
 */
public class LaciudadFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static FragmentManager fragmentManager;
    String TAG = LaciudadFragment.class.getName();
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_laciudad, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){

            }else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    Toast.makeText(getActivity().getApplicationContext(),"Your Permission is needed to get access the call phone",Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        fragmentManager = getChildFragmentManager();
        return v;
    }

    private void setupTabIcons() {

        View view1 = getActivity().getLayoutInflater().inflate(R.layout.custom_tab_la_ciudad, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.laciudad_hotel_icon_hover);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = getActivity().getLayoutInflater().inflate(R.layout.custom_tab_la_ciudad, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.saborf1);
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = getActivity().getLayoutInflater().inflate(R.layout.custom_tab_la_ciudad, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.laciudad_que_hacer_icon);
        tabLayout.getTabAt(2).setCustomView(view3);

        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        tab.getPosition();
                        switch (tab.getPosition()){
                            case 0:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.laciudad_hotel_icon_hover);
                                break;
                            case 1:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.saborf1_hover);
                                break;
                            case 2:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.laciudad_que_hacer_icon_hover);
                                break;
                        }
                        viewPager.setCurrentItem(tab.getPosition());
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        switch (tab.getPosition()){
                            case 0:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.laciudad_hotel_icon);
                                break;
                            case 1:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.saborf1);
                                break;
                            case 2:
                                tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.laciudad_que_hacer_icon);
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
        adapter.addFragment(new HotelesFragment(), getResources().getString(R.string.hoteles));
        adapter.addFragment(new RestaurantesFragment(), getResources().getString(R.string.restaurantes));
        adapter.addFragment(new AdondeIrFragment(), getResources().getString(R.string.a_donde_ir));
        viewPager.setAdapter(adapter);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, String.valueOf(requestCode));
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE && resultCode == getActivity().RESULT_OK) {
            Log.d(TAG,"CALL PHONE OK");
        }

    }
}