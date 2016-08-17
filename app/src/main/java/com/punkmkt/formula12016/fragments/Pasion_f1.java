package com.punkmkt.formula12016.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.punkmkt.formula12016.AlbumStorageDirFactory;
import com.punkmkt.formula12016.BaseAlbumDirFactory;
import com.punkmkt.formula12016.FroyoAlbumDirFactory;
import com.punkmkt.formula12016.StickersActivity;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.TakeSelectPhotoActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by germanpunk on 15/08/16.
 */
public class Pasion_f1 extends Fragment {

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_PICTURE = 2;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    String TAG = TakeSelectPhotoActivity.class.getName();
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int REQUEST_CAMERARESULT=201;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_take_select_photo,container,false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        RelativeLayout container_take_photo = (RelativeLayout) v.findViewById(R.id.container_take_photo);
        RelativeLayout container_select_photo = (RelativeLayout) v.findViewById(R.id.container_select_photo);
        ImageButton take_photo_button = (ImageButton) v.findViewById(R.id.take_photo_button);
        TextView take_photo_text = (TextView) v.findViewById(R.id.take_photo_text);
        ImageButton select_photo_button = (ImageButton) v.findViewById(R.id.select_photo_button);
        TextView select_photo_text = (TextView) v.findViewById(R.id.select_photo_text);

        container_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(getActivity().checkSelfPermission(android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                        ///method to get Images
                        dispatchTakePictureIntent();
                    }else{
                        if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                            Toast.makeText(getActivity().getApplicationContext(),"Your Permission is needed to get access the camera",Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA}, REQUEST_CAMERARESULT);
                    }
                }else{
                    dispatchTakePictureIntent();
                }
            }
        });

        container_select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPictureFromGallery();
            }
        });

        take_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(getActivity().checkSelfPermission(android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                        ///method to get Images
                        dispatchTakePictureIntent();
                    }else{
                        if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                            Toast.makeText(getActivity().getApplicationContext(),"Your Permission is needed to get access the camera",Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA}, REQUEST_CAMERARESULT);
                    }
                }else{
                    dispatchTakePictureIntent();
                }
            }
        });
        take_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        take_photo_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(getActivity().checkSelfPermission(android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                        ///method to get Images
                        dispatchTakePictureIntent();
                    }else{
                        if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                            Toast.makeText(getActivity().getApplicationContext(),"Your Permission is needed to get access the camera",Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA}, REQUEST_CAMERARESULT);
                    }
                }else{
                    dispatchTakePictureIntent();
                }
            }
        });

        select_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPictureFromGallery();
            }
        });
        select_photo_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPictureFromGallery();
            }
        });

        return v;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                //photoFile = createImageFile();
                photoFile = setUpPhotoFile();
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } catch (IOException ex) {
                Log.d(TAG,ex.getMessage().toString());
                Log.d(TAG,"Error occurred while creating the File");
                photoFile = null;
                mCurrentPhotoPath = null;
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Uri photoURI = FileProvider.getUriForFile(this,
                //"com.andoroto.musicprojet.fileprovider",
                //photoFile);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();
        return f;
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            if (mCurrentPhotoPath != null) {
                galleryAddPic();
                Log.d(TAG,mCurrentPhotoPath);
                Intent intent = new Intent(getActivity(), StickersActivity.class);
                intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath);
                mCurrentPhotoPath = null;
                startActivity(intent);
            }
            else{
                //Error
                Log.d(TAG,"Error onactivityresult");
            }
        }
        else if(requestCode == SELECT_PICTURE && resultCode == getActivity().RESULT_OK){
            mCurrentPhotoPath = getPicture(data.getData());
            if (mCurrentPhotoPath != null) {
                galleryAddPic();
                Log.d(TAG,mCurrentPhotoPath);
                Intent intent = new Intent(getActivity(), StickersActivity.class);
                intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath);
                mCurrentPhotoPath = null;
                startActivity(intent);
            }
            else{
                //Error
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
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
    public void selectPictureFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,getString(R.string.select_picture)),SELECT_PICTURE);
    }
    public  String getPicture(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
}
