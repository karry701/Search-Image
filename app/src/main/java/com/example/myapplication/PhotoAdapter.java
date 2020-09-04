package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class PhotoAdapter extends BaseAdapter {
    private Context mContext;
    MyData mdata ;
    LayoutInflater inflater;
    String link;

   // Constructor
    public PhotoAdapter(MainActivity c, MyData objects) {
        mContext = c;
        mdata = objects;
        inflater= c.getLayoutInflater();
    }

    // set search images in griedview
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.support_photoadapter, parent, false);
            holder = new ViewHolder();
            holder.iv_img = view.findViewById(R.id.iv_img);
            view.setTag(holder);

        } else {
            holder = (ViewHolder)view.getTag();
             if(mdata.getData().get(position).getImages() == null) {
                 Log.d("vanilla3", "null");
            } else {
                 if(!mdata.getData().get(position).getImages().get(0).getType().equals("video/mp4") && mdata.getData().get(position).getImages() != null){
                     link = mdata.getData().get(position).getImages().get(0).getLink();
                     Log.d("vanillal", link);

                     // Set Search images into gridview
                     Glide.with(mContext).load(link).asBitmap().placeholder(R.drawable.post).centerCrop().into(new BitmapImageViewTarget(holder.iv_img) {
                         @Override
                         protected void setResource(Bitmap resource) {
                             holder.iv_img.setImageBitmap(resource);
                             holder.iv_img.setOnClickListener(view -> {
                                 String title = mdata.getData().get(position).getTitle();
                                 String linkurl = mdata.getData().get(position).getImages().get(0).getLink();

                                 // open the image in a new activity with the title
                                 Intent intent = new Intent(mContext, ImageDisplay.class);
                                 intent.putExtra("title", title);
                                 intent.putExtra("link", linkurl);
                                 mContext.startActivity(intent);
                             });
                         }
                     });
                 }
             }
    }
        return view;
    }

    private static class ViewHolder {
        ImageView iv_img;
    }

    public int getCount() {
        return mdata == null ? 0 : mdata.getData().size()-1;
    }

    public Object getItem(int position) {
        return 0;
    }

    public long getItemId(int position) {
        return position;
    }
}