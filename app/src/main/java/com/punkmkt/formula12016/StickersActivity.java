package com.punkmkt.formula12016;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.punkmkt.formula12016.models.ImgSticker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class StickersActivity extends AppCompatActivity {


    String mCurrentPhotoPath;
    String TAG = StickersActivity.class.getName();
    PhotoSortrView photoSorter;
    private ArrayList<ImgSticker> mImages = new ArrayList<>();
    private static final int[] STICKERS = { R.drawable.m74hubble, R.drawable.catarina, R.drawable.tahiti, R.drawable.sunset, R.drawable.lake };
    private LinearLayout stickersLayout;
    private RelativeLayout canvas_container;
    private int currentIndex;
    Button cancel_button,continue_button, save_button, share_button;
    boolean isTouchable = true;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    ImageView photo;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasion);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        cancel_button = (Button) findViewById(R.id.cancel_button);
        continue_button = (Button) findViewById(R.id.continue_button);
        save_button = (Button) findViewById(R.id.save_button);
        share_button = (Button) findViewById(R.id.share_button);
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
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(resource_id);
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
    void saveImage(boolean share){
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
        }
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
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
}
