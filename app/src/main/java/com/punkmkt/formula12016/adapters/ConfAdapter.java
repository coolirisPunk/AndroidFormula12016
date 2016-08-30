package com.punkmkt.formula12016.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.punkmkt.formula12016.ConfActivity;
import com.punkmkt.formula12016.LoginActivity;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.RegistroActivity;

/**
 * Created by DaniPunk on 11/07/16.
 */
public class ConfAdapter extends PagerAdapter {
    Activity activity;
    Resources r;
    public ConfAdapter(Resources r, Activity activity){
        this.activity = activity;
        this.r = r;
    }
    public int getCount() {
        return 3;
    }

    public Object instantiateItem(View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int resId = 0;
        switch (position) {
            case 0:
                resId = R.layout.conf_1;
                break;
            case 1:
                resId = R.layout.conf_2;
                break;
            case 2:
                resId = R.layout.conf_3;
                break;
            default:
                break;
        }
        View view = inflater.inflate(resId, null);
        ((ViewPager) collection).addView(view);
        String t1;
        String t2;
        String t3;
        TextView textView,textView2;
        Spannable spannable;
        switch (position) {
            case 0:
                TextView saltar_text = (TextView)activity.findViewById(R.id.saltar_text);
                ImageView saltar_x = (ImageView)activity.findViewById(R.id.saltar_x);

                saltar_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConfActivity.setCurrentItemViewPager(3);
                    }
                });
                saltar_x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConfActivity.setCurrentItemViewPager(3);
                    }
                });
                textView = (TextView) activity.findViewById(R.id.step1_number2);
                t1 = r.getString(R.string.step1_number2_1);
                t2 = r.getString(R.string.step1_number2_2);
                t3 = t1 + " " + t2;spannable = new SpannableString(t3);
                spannable.setSpan(new ForegroundColorSpan(Color.WHITE), t1.length(), t3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannable, TextView.BufferType.SPANNABLE);
                break;
            case 1:
                TextView saltar_text_2 = (TextView)activity.findViewById(R.id.saltar_text_2);
                ImageView saltar_x_2 = (ImageView)activity.findViewById(R.id.saltar_x_2);
                saltar_text_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConfActivity.setCurrentItemViewPager(3);
                    }
                });
                saltar_x_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConfActivity.setCurrentItemViewPager(3);
                    }
                });

                textView2 = (TextView) activity.findViewById(R.id.step2_number3);
                t1 = r.getString(R.string.step2_number3_1);
                t2 = r.getString(R.string.step2_number3_2);
                t3 = t1 + " " + t2;
                spannable = new SpannableString(t3);
                spannable.setSpan(new ForegroundColorSpan(Color.WHITE), t1.length(), t3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView2.setText(spannable, TextView.BufferType.SPANNABLE);
                break;
            case 2:
                TextView next_view = (TextView) activity.findViewById(R.id.next_view_3);
                final ImageView notificacion1 = (ImageView) activity.findViewById(R.id.notificacion1);
                final ImageView notificacion2 = (ImageView) activity.findViewById(R.id.notificacion2);
                final ImageView notificacion3 = (ImageView) activity.findViewById(R.id.notificacion3);


                notificacion1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificacion1.setImageResource(R.drawable.notificacion_activa);
                    }
                });
                notificacion2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificacion2.setImageResource(R.drawable.notificacion_activa);
                    }
                });
                notificacion3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificacion3.setImageResource(R.drawable.notificacion_activa);
                    }
                });

                SwitchCompat mSwitchGPS = (SwitchCompat) view.findViewById(R.id.mySwitch);
                mSwitchGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            ConfActivity.onCheckedChanged(activity, isChecked);
                        }
                    }
                });
                next_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent goLogin = new Intent(activity, RegistroActivity.class);
                        goLogin.putExtra("intent_from", "CONF");
                        activity.startActivity(goLogin);

                    }
                });

                break;
            default:
                break;

       }
        view.setTag("myview" + position);
        return view;
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);

    }
    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub

    }
    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

}

