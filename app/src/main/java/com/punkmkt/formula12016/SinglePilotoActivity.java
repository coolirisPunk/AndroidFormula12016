package com.punkmkt.formula12016;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.punkmkt.formula12016.models.Foto;
import com.punkmkt.formula12016.utils.AuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SinglePilotoActivity extends AppCompatActivity {

    private String AHR_PILOTOS_JSON_API_URL = "http://104.236.3.158:82/api/premio/pilotos/";
    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();
    private ArrayList<Foto> photos = new ArrayList<>();

    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager largeViewPager;
    private CustomFragmentPagerAdapter customFragmentPageAdapter;
    private LinearLayout galleryLayout;
    private int currentIndex;

    public SinglePilotoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_piloto);

        largeViewPager = (ViewPager) findViewById(R.id.large_image);

        customFragmentPageAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), photos);
        Log.d(TAG,photos.toString());
        largeViewPager.setOffscreenPageLimit(3);
        largeViewPager.setAdapter(customFragmentPageAdapter);
        galleryLayout = (LinearLayout)findViewById(R.id.my_gallery);

        final TextView nombre = (TextView) findViewById(R.id.descripcion_nombre_piloto);
        final TextView numero = (TextView) findViewById(R.id.descripcion_numero_piloto);
        final TextView nacionalidad = (TextView) findViewById(R.id.descripcion_nacionalidad_piloto);
        final TextView fecha_nacimiento = (TextView) findViewById(R.id.descripcion_fecha_nacimiento_piloto);
        final TextView lugar_nacimiento = (TextView) findViewById(R.id.descripcion_lugar_nacimiento_piloto);
        final TextView campeonatos = (TextView) findViewById(R.id.descripcion_campeonatos_piloto);


        StringRequest request;
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Log.d("piloto",id);


        AHR_PILOTOS_JSON_API_URL = AHR_PILOTOS_JSON_API_URL + id + "/";
        request = new AuthRequest(getApplicationContext(), Request.Method.GET, AHR_PILOTOS_JSON_API_URL, "utf-8", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    nombre.setText(object.optString("name"));
                    numero.setText(object.optString("number"));
                    nacionalidad.setText(object.optString("nationality"));
                    fecha_nacimiento.setText(object.optString("birthday"));
                    lugar_nacimiento.setText(object.optString("place_of_birth"));
                    campeonatos.setText(object.optString("championships"));
                    JSONArray json_photos = object.getJSONArray("photos");
                    for (int count = 0; count < json_photos.length(); count++) {
                        JSONObject anEntry = json_photos.getJSONObject(count);
                        Log.d("foto",anEntry.optString("name"));
                        Foto photo = new Foto();
                        photo.setId(anEntry.optString("id"));
                        photo.setPicture(anEntry.optString("picture"));
                        photo.setName(anEntry.optString("name"));
                        photo.setThumbnail(anEntry.optString("thumbnail"));
                        photos.add(photo);
                    }
                    customFragmentPageAdapter.notifyDataSetChanged();
                    int count = 0;
                    for(Foto photo:photos){
                        String imageAbsolutePath = photo.getThumbnail();
                        ImageView addImageView = getNewImageView(imageAbsolutePath);
                        final int indexJ = count;
                        addImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentIndex = indexJ;
                                largeViewPager.setCurrentItem(currentIndex);
                            }
                        });
                        galleryLayout.addView(addImageView);
                        count ++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MyVolleySingleton.getInstance().addToRequestQueue(request);

    }

    public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Foto> LargePhotos;
        public CustomFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<Foto> LargePhotos){
            super(fragmentManager);
            this.LargePhotos = LargePhotos;
        }
        @Override
        public Fragment getItem(int position) {
            System.out.println("Current position" + position);
            return ImageGalleryFragment.newInstance(position, this.LargePhotos);
        }
        @Override
        public int getCount() {
            return LargePhotos.size();
        }
    }

   static  public class ImageGalleryFragment extends Fragment {
       ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();
        private static final String ARGUMENT = "index";
        private static ArrayList<Foto> LargePhotos;
        private int indexNumber;
        public  static ImageGalleryFragment newInstance(int index, ArrayList<Foto> LargePhoto){
            ImageGalleryFragment imageGalleryFragment = new ImageGalleryFragment();
            Bundle args = new Bundle();
            args.putInt(ARGUMENT, index);
            imageGalleryFragment.setArguments(args);
            LargePhotos = LargePhoto;
            return imageGalleryFragment;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            indexNumber = getArguments() != null ? getArguments().getInt(ARGUMENT) : 1;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.image_display, container, false);
            NetworkImageView mImageView = (NetworkImageView) v.findViewById(R.id.display_image);
            String imageResource = LargePhotos.get(indexNumber).getPicture();
            mImageView.setImageUrl(imageResource, imageLoader);
            return v;
        }
    }
    private NetworkImageView getNewImageView(String photoPath){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,200);
        lp.setMargins(20,0, 20, 0);
        NetworkImageView imageView = new NetworkImageView(getApplicationContext());
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageUrl(photoPath, imageLoader);
        return imageView;
    }
}

