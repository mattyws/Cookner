package com.mattyws.udacity.cookner.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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
        this.mPictures =pictures;
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
        File file = new File(mContext.getFilesDir().toString() , mPictures.get(position).getFilename());
        Uri fileUri = Uri.fromFile(file);
        Log.d(TAG, "instantiateItem: " + fileUri.toString());
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), fileUri);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
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
        if (pictures == null){
            pictures = new ArrayList<>();
        }
        if(pictures.isEmpty()){
            String noImageFile = "drawable://" + R.drawable.noimage;
            String filename = "noimage.jpg";
            Picture picture = new Picture();
            picture.setAbsoluteFilepath(noImageFile);
            picture.setFilename(filename);
            pictures.add(picture);
        }
        this.mPictures = pictures;
        notifyDataSetChanged();
    }
}
