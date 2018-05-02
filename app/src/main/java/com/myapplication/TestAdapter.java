package com.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TestAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int layoutID;
    private String[] namelist;
    private String[] emaillist;
    private Bitmap[] photolist;

    static class ViewHolder {
        TextView text;
        TextView email;
        ImageView img;
    }

    TestAdapter(Context context, int itemLayoutId,
                String[] names, String[] emails, int[] photos ){

        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;

        namelist = names;
        emaillist = emails;
        // bitmapの配列
        photolist = new Bitmap[photos.length];
        // drawableのIDからbitmapに変換
        for( int i = 0; i< photos.length; i++){
            photolist[i] = BitmapFactory.decodeResource(context.getResources(), photos[i]);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
/*
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(layoutID, null);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.img_item);
            holder.text = convertView.findViewById(R.id.text_view);
            holder.email = convertView.findViewById(R.id.text_mail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.img.setImageBitmap(photolist[position]);
        } catch (Exception e) {
        }

        String str = "ID: "+ String.valueOf(position+1)
                + "\n\n" + emaillist[position];
        holder.email.setText(str);

        holder.text.setText(namelist[position]);
*/
        return convertView;
    }

    @Override
    public int getCount() {
        return namelist.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
