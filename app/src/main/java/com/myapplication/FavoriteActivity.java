package com.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class FavoriteActivity extends AppCompatActivity {

    private String[] favoriteBrand;
    private String[] favoriteCommodity;
    private int[] favoriteImgInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        favoriteBrand = new String[]{
                "glass",
                "lip",
                "hat",
                "watch"
        };

        favoriteCommodity = new String[]{
                "xxx",
                "xxx",
                "xxx",
                "xxx"
        };

        // drawableに画像を入れる、R.id.xxx はint型
        favoriteImgInt = new int[]{
                R.drawable.glass,
                R.drawable.lip,
                R.drawable.hat,
                R.drawable.watch
        };

        updateListView();
    }

    public void updateListView(){
        // ListViewのインスタンスを生成
        ListView listView = findViewById(R.id.listFavorite);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list_items.xml を
        // activity_main.xml に inflate するためにadapterに引数として渡す
        BaseAdapter ba = new TestAdapter(this.getApplicationContext(),
                R.layout.list_items, favoriteBrand, favoriteCommodity, favoriteImgInt);

        // ListViewにadapterをセット
        listView.setAdapter(ba);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ListView listView = (ListView) parent;
                // クリックされたアイテムを取得します
                String item = listView.getItemAtPosition(position).toString();

                //Feedback
                AlertDialog.Builder dl = new AlertDialog.Builder(FavoriteActivity.this);
                dl.setTitle(item);
                dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dl.show();
            }
        });
    }

}
