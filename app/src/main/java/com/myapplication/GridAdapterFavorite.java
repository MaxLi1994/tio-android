package com.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;

public class GridAdapterFavorite extends BaseAdapter {

    class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView textView2;
    }

    private List<Integer> imageList = new ArrayList<>();
    private List<String> commodityNames, url;
    private LayoutInflater inflater;
    private int layoutId;
    private Context context;

    // 引数がMainActivityからの設定と合わせる
    GridAdapterFavorite(Context context,
                int layoutId,
                List<Integer> iList,
                List<String> commodityName,
                List<String> URLs) {

        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.context = context;

        imageList = iList;
        commodityNames = commodityName;
        url = URLs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // main.xml の <GridView .../> に grid_items.xml を inflate して convertView とする
            convertView = inflater.inflate(layoutId, parent, false);

            // ViewHolder を生成
            holder = new ViewHolder();

            holder.imageView = convertView.findViewById(R.id.image_view);
            holder.textView = convertView.findViewById(R.id.text_view);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.imageView.setImageResource(imageList.get(position));
            holder.textView.setText(commodityNames.get(position));

            //Add Image from URL
            addUrlImage(url.get(position), convertView);
        } catch (Exception e) {

        }

        //read color info
        SharedPreferences preferences;
        preferences = this.context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String color = preferences.getString("theme", "bluebutton");
        //If logged in
        if (color.equals("bluebutton")) {
            convertView.setBackgroundResource(R.drawable.frame_style2);
        } else {
            convertView.setBackgroundResource(R.drawable.frame_style3);
        }

        return convertView;
    }

    // ネットワークアクセスするURLを設定する
    private void addUrlImage(String url, View v){
        ImageView img = v.findViewById(R.id.image_view);
        Picasso.with(context)
                .load(url)
                .into(img);
    }

    @Override
    public int getCount() {
        // List<String> imgList の全要素数を返す
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}