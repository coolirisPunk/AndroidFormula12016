package com.punkmkt.formula12016;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.punkmkt.formula12016.models.ImgSticker;
import com.punkmkt.formula12016.utils.BitmapManager;
import com.punkmkt.formula12016.utils.RecyclingBitmapDrawable;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

public class StickersActivity extends AppCompatActivity {


    String mCurrentPhotoPath;
    String TAG = StickersActivity.class.getName();
    PhotoSortrView photoSorter;
    private ArrayList<ImgSticker> mImages = new ArrayList<>();
    private static final int[] STICKERS = { R.drawable.euphoric_200,R.drawable.speed_lovers_200,R.drawable.true_racers_200,R.drawable.vip_party_200};
    private LinearLayout stickersLayout;
    private RelativeLayout canvas_container;
    private int currentIndex;
    Button cancel_button,continue_button;
    ImageButton save_button, share_button;
    boolean isTouchable = true;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    ImageView photo;
    ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;
    static public LruCache<String, Bitmap> mMemoryCache;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasion);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        cancel_button = (Button) findViewById(R.id.cancel_button);
        continue_button = (Button) findViewById(R.id.continue_button);
        save_button = (ImageButton) findViewById(R.id.save_button);
        share_button = (ImageButton) findViewById(R.id.share_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getBaseContext(), TakeSelectPhotoActivity.class);
                //startActivity(intent);
                onBackPressed();
            }
        });
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_button.setVisibility(View.GONE);
                continue_button.setVisibility(View.GONE);
                save_button.setVisibility(View.VISIBLE);
                share_button.setVisibility(View.VISIBLE);
                isTouchable = false;
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(false);
            }
        });
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(true);
            }
        });


        Intent intent = getIntent();
        mCurrentPhotoPath = intent.getStringExtra("mCurrentPhotoPath");
        Log.d(TAG,mCurrentPhotoPath);

        stickersLayout = (LinearLayout) findViewById(R.id.stickers);
        photo = (ImageView) findViewById(R.id.photo);

        photo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                photo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ; //height is ready
                photo.getWidth(); //height is ready
                photo.setImageBitmap(getBitmapResized(photo.getWidth(),photo.getHeight(),mCurrentPhotoPath));

            }
        });

        canvas_container = (RelativeLayout) findViewById(R.id.canvas_container);
        Log.d(TAG,"entro nuevamente");
        stickers_init();
        photoSorter = new PhotoSortrView(this,mImages, mCurrentPhotoPath);

        canvas_container.addView(photoSorter);
    }
    private void stickers_init(){
        Resources res = getApplicationContext().getResources();
        for (int i = 0; i < STICKERS.length; i++){
            mImages.add(new ImgSticker(STICKERS[i], res));
            ImageView addImageView = getNewImageView(STICKERS[i]);
            final int indexJ = i;
            addImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIndex = indexJ;
                    Log.d(TAG,String.valueOf(currentIndex));
                    photoSorter.addImagetoCanvas(getApplicationContext(),currentIndex);
                }
            });
            stickersLayout.addView(addImageView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"Onresume");
        //photoSorter.addImagetoCanvas(getApplicationContext(),0);
    }
    @Override
    protected void onPause() {
        super.onPause();
        photoSorter.unloadImages();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println(event.getAction() + " " + event.getKeyCode() + " - " + (char) event.getUnicodeChar());

        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            Log.d(TAG,"keyEvent");
            photoSorter.trackballClicked();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        Log.d(TAG,String.valueOf(photo.getWidth()));
//        Log.d(TAG,String.valueOf(photo.getHeight()));
    }

    private ImageView getNewImageView(int resource_id){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,200);
        lp.setMargins(20,0, 20, 0);
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        loadBitmap(resource_id, imageView);
        //imageView.setImageResource(resource_id);
        //Bitmap icon = BitmapFactory.decodeResource(getResources(), resource_id);

       // imageView.setImageBitmap(getBitmapResized(photo.getWidth(),photo.getHeight()
        return imageView;
    }

    private Bitmap getBitmapResized(int targetW, int targetH, String mCurrentPhotoPath){
        Log.d(TAG,String.valueOf(targetW));
        Log.d(TAG,String.valueOf(targetH));

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }
    public void overlay(Bitmap bmp1, Bitmap bmp2, boolean share) {
        if (share){

            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.compartir_usando));
           // i.putExtra(Intent.EXTRA_STREAM,contentUri );
            i.setType("image/png");
            startActivity(Intent.createChooser(i, getResources().getText(R.string.share)));
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isTouchable;
    }
    void saveImage(final boolean share){
        //initialize the progress dialog and show it
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        canvas_container.setDrawingCacheEnabled(true);
        Bitmap b = canvas_container.getDrawingCache();
        File myDir = getAlbumDir();

        String fname = "pasion-"+ GetRandomName() +".jpg";

        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();

        try {
            FileOutputStream out = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 95, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Error al intentar guardar la imagen.", Snackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                        progressDialog.dismiss();
                        if (!share){
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Imagen guardada.", Snackbar.LENGTH_SHORT);
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();
                            //Intent goHome = new Intent(StickersActivity.this, MainActivity.class);
                            //goHome.putExtra("fragment","select-photo");
                            //startActivity(goHome);
                        }
                    }
                });
        if (share){
            Uri contentUri= Uri.fromFile(file);

            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.compartir_usando));
            i.putExtra(Intent.EXTRA_STREAM,contentUri );
            i.setType("image/png");
            startActivity(Intent.createChooser(i, getResources().getText(R.string.share)));
        }

    }
    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            }else{

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs storage access");
                builder.setMessage("Please grant storage access so this app can write the results in background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(PERMISSIONS_STORAGE,
                                REQUEST_EXTERNAL_STORAGE);
                    }

                });
                builder.show();
            }

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }
    private String getAlbumName() {
        return getString(R.string.album_name);
    }
    private int GetRandomName(){
        Random generator = new Random();
        int n = 10000;
        return generator.nextInt(n);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //    finish();

    }

    public void loadBitmap(int resId, ImageView imageView) {
        if (cancelPotentialWork(resId, imageView)) {
            Bitmap bitmap;
            bitmap =  BitmapManager.decodeSampledBitmapFromResource(getApplicationContext().getResources(), R.drawable.loading, 100, 100);
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(getApplicationContext().getResources(),bitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
            new RecyclingBitmapDrawable(getResources(),bitmap);
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            if (bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            final Bitmap bitmap = decodeSampledBitmapFromResource(getApplicationContext().getResources(), params[0], 150, 100);
            addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
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

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
