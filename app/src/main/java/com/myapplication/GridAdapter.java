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

public class GridAdapter extends BaseAdapter
{
    class ViewHolder
    {
        ImageView imageView;
        TextView textView;
        TextView textView2;
    }

    private List<Integer> imageList = new ArrayList<>();
    private List<String> brandNames, commodityNames, url;
    private LayoutInflater inflater;
    private int layoutId;
    private Context context;
    private SharedPreferences preferences;
    private String color;

    GridAdapter(Context context,
                int layoutId,
                List<Integer> iList,
                List<String> brandName,
                List<String> commodityName,
                List<String> URLs)
    {
        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.context = context;

        imageList = iList;
        brandNames = brandName;
        commodityNames = commodityName;
        url = URLs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            //inflate grid items and make convertView
            convertView = inflater.inflate(layoutId, parent, false);

            //create ViewHolder
            holder = new ViewHolder();

            holder.imageView = convertView.findViewById(R.id.image_view);
            holder.textView = convertView.findViewById(R.id.text_view);
            holder.textView2 = convertView.findViewById(R.id.text_view2);
            holder.textView.setTextColor(Color.WHITE);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        try
        {
            holder.imageView.setImageResource(imageList.get(position));
            holder.textView.setText(brandNames.get(position));
            holder.textView2.setText(commodityNames.get(position));

            //read color info
            preferences = this.context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
            color = preferences.getString("theme", "bluebutton");
            if (color.equals("bluebutton"))
            {
                holder.textView.setBackgroundResource(R.drawable.frame_style2);
            }
            else {
                holder.textView.setBackgroundResource(R.drawable.frame_style3);
                holder.textView.setTextColor(Color.WHITE);
            }

            //Add Image from URL
            addUrlImage(url.get(position), convertView);
        }
        catch (Exception e)
        {
        }

        return convertView;
    }

    private void addUrlImage(String url, View v)
    {
        ImageView img = v.findViewById(R.id.image_view);
        Picasso.with(context)
                .load(url)
                .transform(new CircleTransform())
                .into(img);
    }

    @Override
    public int getCount()
    {
        //return the size of the image list
        return imageList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }
}