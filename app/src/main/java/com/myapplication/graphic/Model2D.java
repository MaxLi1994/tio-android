package com.myapplication.graphic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class Model2D {
    private Bitmap image;
    private Uri uri;
    private Context context;

    public Model2D(Context context, String uri) {
        this.uri = Uri.parse(uri);
        this.context = context;
    }

    public void load() {

        Picasso.with(context).load(this.uri).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                image = bitmap;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.e("picasso", "failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    public boolean isReady() {
        return this.image != null;
    }

    public Bitmap getImage() {
        return image;
    }

    public void destroy() {
        image.recycle();
        image = null;
    }
}
