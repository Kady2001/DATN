package com.example.mycartoon;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.mycartoon.MyImage;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<MyImage> mImageList;
    public GalleryAdapter(Context context,ArrayList<MyImage> imageList) {
        mContext = context;

        mImageList = imageList;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getItemPath(int position) {
        return mImageList.get(position).getPath();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        Uri imageUri = Uri.parse(mImageList.get(position).getPath());
        // Sử dụng thư viện Glide để hiển thị ảnh từ Uri
        Glide.with(mContext)
                .load(mImageList.get(position))
                .into(imageView);

        return imageView;
    }
}
