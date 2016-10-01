package autodromo.punkmkt.com.ahrapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.lang.ref.WeakReference;

import autodromo.punkmkt.com.ahrapp.ConfActivity;
import autodromo.punkmkt.com.ahrapp.MainActivity;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.RegistroActivity;
import autodromo.punkmkt.com.ahrapp.databases.Notification;
import autodromo.punkmkt.com.ahrapp.databases.Notification_Table;
import autodromo.punkmkt.com.ahrapp.utils.BitmapManager;
import autodromo.punkmkt.com.ahrapp.utils.BitmapWorkerTask;
import autodromo.punkmkt.com.ahrapp.utils.RecyclingBitmapDrawable;

/**
 * Created by DaniPunk on 11/07/16.
 */
public class ConfAdapter extends PagerAdapter {
    Activity activity;
    Resources r;
    final String TAG_NOTIFICATION_NEWS = "news";
    final String TAG_NOTIFICATION_RESULTADOS = "results";
    final String TAG_NOTIFICATION_HORARIOS = "events";

    public ConfAdapter(Resources r, Activity activity){
        this.activity = activity;
        this.r = r;

    }
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position)
    {
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
                ImageView img1 = (ImageView) view.findViewById(R.id.img_step_1);

                //loadBitmap(R.drawable.tutorial1_bg,img1);
                img1.setImageBitmap(decodeSampledBitmapFromResource(activity
                        .getApplicationContext().getResources(),R.drawable.tutorial1_bg,375,610));

                TextView saltar_text = (TextView)view.findViewById(R.id.saltar_text);
                ImageView saltar_x = (ImageView)view.findViewById(R.id.saltar_x);

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
                textView = (TextView) view.findViewById(R.id.step1_number2);

                t1 = r.getString(R.string.step1_number2_1);
                t2 = r.getString(R.string.step1_number2_2);
                t3 = t1 + " " + t2;spannable = new SpannableString(t3);
                spannable.setSpan(new ForegroundColorSpan(Color.WHITE), t1.length(), t3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannable, TextView.BufferType.SPANNABLE);
                break;
            case 1:
                ImageView  img2 = (ImageView) view.findViewById(R.id.img_step_2);
                //loadBitmap(R.drawable.tutorial2_bg,img2);
                img2.setImageBitmap(decodeSampledBitmapFromResource(activity
                        .getApplicationContext().getResources(),R.drawable.tutorial2_bg,375,610));

                TextView saltar_text_2 = (TextView)view.findViewById(R.id.saltar_text_2);
                ImageView saltar_x_2 = (ImageView)view.findViewById(R.id.saltar_x_2);
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

                textView2 = (TextView) view.findViewById(R.id.step2_number3);
                t1 = r.getString(R.string.step2_number3_1);
                t2 = r.getString(R.string.step2_number3_2);
                t3 = t1 + " " + t2;
                spannable = new SpannableString(t3);
                spannable.setSpan(new ForegroundColorSpan(Color.WHITE), t1.length(), t3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView2.setText(spannable, TextView.BufferType.SPANNABLE);
                break;
            case 2:
                TextView next_view = (TextView) view.findViewById(R.id.next_view_3);
                final ImageView notificacion2 = (ImageView) view.findViewById(R.id.notificacion2);
                final ImageView notificacion3 = (ImageView) view.findViewById(R.id.notificacion3);
                //NOTICIAS
                IniciarlizarNotificacion(notificacion2,TAG_NOTIFICATION_NEWS);
                //RESULTADOS
                IniciarlizarNotificacion(notificacion3,TAG_NOTIFICATION_RESULTADOS);
                //noticias
                notificacion2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificacion_update(notificacion2, TAG_NOTIFICATION_NEWS,TAG_NOTIFICATION_NEWS);
                    }
                });

                //resultados
                notificacion3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificacion_update(notificacion3, TAG_NOTIFICATION_RESULTADOS,TAG_NOTIFICATION_RESULTADOS);
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
        //collection.addView(view, 0);

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
    public void destroyItem(ViewGroup collection, int position, Object view)
    {
        collection.removeView((View) view);
    }



    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public void notificacion_update(ImageView image_notification, String notification,String type){
        try {
            final Notification notificacion = new Select().from(Notification.class).where(Notification_Table.name.eq(notification)).querySingle();
            int active = notificacion.getActive();
            if (active == 1) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(notification);
                notificacion.setActive(0);
                notificacion.setType(type);
                notificacion.update();
                image_notification.setImageResource(R.drawable.notificacion_inactiva);
                Log.d("Not Conf", "Unsubscribed to "+notification+" topic");
            }
            else {
                //update
                FirebaseMessaging.getInstance().subscribeToTopic(notification);
                notificacion.setActive(1);
                notificacion.setType(type);
                notificacion.update();
                image_notification.setImageResource(R.drawable.notificacion_activa);
                Log.d("Not Conf", "Subscribed to "+notification+" topic");
            }

        }
        catch (Throwable e) {
            FirebaseMessaging.getInstance().subscribeToTopic(notification);
            Notification notificacion = new Notification();
            notificacion.setName(notification);
            notificacion.setActive(1);
            notificacion.setType(type);
            notificacion.save();
            image_notification.setImageResource(R.drawable.notificacion_activa);
            Log.d("Not Conf", "Subscribed to "+notification+" topic");
        }
    }
    public void IniciarlizarNotificacion(ImageView image_notification, String notification){
        try {
            final Notification notificacion = new Select().from(Notification.class).where(Notification_Table.name.eq(notification)).querySingle();
            int active = notificacion.getActive();
            if (active == 1) {
                image_notification.setImageResource(R.drawable.notificacion_activa);
            } else {
                image_notification.setImageResource(R.drawable.notificacion_inactiva);
            }
        } catch (Throwable e) {
            image_notification.setImageResource(R.drawable.notificacion_inactiva);
        }
    }

}

