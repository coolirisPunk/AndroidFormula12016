package autodromo.punkmkt.com.ahrapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import autodromo.punkmkt.com.ahrapp.MainActivity;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.utils.BitmapManager;
import autodromo.punkmkt.com.ahrapp.utils.BitmapWorkerTask;
import autodromo.punkmkt.com.ahrapp.utils.RecyclingBitmapDrawable;

import java.lang.ref.WeakReference;


/**
 * Created by sebastianmendezgiron on 05/08/16.
 */

public class TiendaPageAdapter extends PagerAdapter {

    Activity activity;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    Resources r;

    public TiendaPageAdapter(Resources r, Activity activity){
        this.activity = activity;
        this.r = r;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position)
    {
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int opcId = 0;
        switch (position) {
            case 0:
                opcId = R.layout.tienda_tuto_uno;
                break;
            case 1:
                opcId = R.layout.tienda_tuto_dos;
                break;
            case 2:
                opcId = R.layout.tienda_tuto_tres;
                break;
        }

        View view = inflater.inflate(opcId, null);

        ((ViewPager) collection).addView(view);
        int width = 100;
        int height = 250;
        switch (position) {
            case 0:
                img1 = (ImageView) view.findViewById(R.id.tienda_tuto_img1);
                img1.setImageBitmap(decodeSampledBitmapFromResource(activity
                        .getApplicationContext().getResources(),R.drawable.tutorial1_img,306,333));
                break;
            case 1:
                img2= (ImageView) view.findViewById(R.id.tienda_tutoimg2);
                img2.setImageBitmap(decodeSampledBitmapFromResource(activity
                        .getApplicationContext().getResources(),R.drawable.tutorial2_img,306,333));
                break;
            case 2:
                img3= (ImageView) view.findViewById(R.id.tienda_tutoimg3);
                //img3.setImageBitmap(BitmapManager.decodeSampledBitmapFromResource(r, R.drawable.home_tutorial_3, width, height));
                img3.setImageBitmap(decodeSampledBitmapFromResource(activity
                        .getApplicationContext().getResources(),R.drawable.tutorial3_img,306,333));
                break;
        }
        view.setTag("myview" + position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view)
    {
        collection.removeView((View) view);
    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub

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
    public void startUpdate(View arg0) {
        // TODO Auto-generated method stub

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

}
