package com.mattyws.udacity.cookner.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Picture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImagesPagerAdapter extends PagerAdapter {

    private static final String TAG = ImagesPagerAdapter.class.getCanonicalName();
    private List<Picture> mPictures;
    private LayoutInflater inflater;
    private Context mContext;


    public ImagesPagerAdapter(Context mContext, List<Picture> pictures) {
        this.mContext = mContext;
        this.mPictures = pictures;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if(mPictures == null) return 0;
        return mPictures.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.image_cell, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.recipe_image);
        Picture picture = mPictures.get(position);

        if(picture == null && mPictures.size() <= 1){
            Log.d(TAG, "instantiateItem: picture null");
            imageView.setImageResource(R.drawable.noimage);
        } else if (picture != null){
            Log.d(TAG, "instantiateItem: picture not null");
            File file = new File(mContext.getFilesDir().toString(), picture.getFilename());
            Uri fileUri = Uri.fromFile(file);
            Log.d(TAG, "instantiateItem: " + fileUri.toString());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), fileUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Glide.with(mContext)
//                .load(file)
//                .into(imageView);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void setPictures(List<Picture> pictures) {
        if(pictures == null){
            pictures = new ArrayList<>();
        }
        if(pictures.size() == 0){
            pictures.add(null);
        }
        this.mPictures = pictures;
        notifyDataSetChanged();
    }
}
