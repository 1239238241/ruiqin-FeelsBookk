package com.CMPUT301.ruiqin.FeelsBook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HistoryPageNev extends AppCompatActivity {
    /** I use recycleView to create history pages
     * it need an adapter to catch information
     * also a cardview is used to make the page more beautiful
     */
    private RecyclerView historyRecycleView;
    private RecycleViewAdapter RecycleViewAdapterX;
    private RecyclerView.LayoutManager RecycleViewLayout;

    private ArrayList<baseMood> baseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_page);

    }
    @Override
    protected void onStart() {
            super.onStart();
            loadHistory();/**load history when start*/

        historyRecycleView = findViewById(R.id.recycler_view);
        historyRecycleView.setHasFixedSize(false);
        RecycleViewLayout = new LinearLayoutManager(this);
        RecycleViewAdapterX = new RecycleViewAdapter(baseList);
        historyRecycleView.setLayoutManager(RecycleViewLayout);
        historyRecycleView.setAdapter(RecycleViewAdapterX);/**link adapter and recycleview*/

        /** I learn to use adapter onclick from google, android developer and reference from stackoverflow
         * name Google,
         * date published in stackoverflow: 2015-12-05
         * url:
         * https://developer.android.com/reference/android/widget/AdapterView.OnItemClickListener
         * https://stackoverflow.com/questions/34110497/how-to-implement-a-setonitemclicklistener-firebaserecyclerviewadapter
         * licence: https://developer.android.com/license/w3c.txt
         */
        RecycleViewAdapterX.setOnItemClickListener(new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(HistoryPageNev.this,"Edit pages", Toast.LENGTH_SHORT).show();
                saveHistory();
                Intent intent3 = new Intent(HistoryPageNev.this,EditPage.class);
                intent3.putExtra("position",position);
                startActivity(intent3);
            }
        });
        /**
         * learned this function from youtube videos
         * name: code in flow
         * published date: 2017-11-14
         * url: https://www.youtube.com/watch?v=dvDTmJtGE_I
         * license: https://developer.android.com/license/w3c.txt
         * -----------------------
         * when swiped to the left delete this message from history
         * and save changes
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction ) {
                if (direction == ItemTouchHelper.LEFT){
                    RecycleViewAdapterX.deleteMood(viewHolder.getAdapterPosition());
                    saveHistory();
                }
            }
        }).attachToRecyclerView(historyRecycleView);
        //RecycleViewAdapterX.notifyDataSetChanged();
        historyRecycleView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(RecycleViewAdapterX.getItemCount() == 0){
                    saveHistory();
                }
            }
        });
    }




    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    /**again use sharedPreference and gson here...*/
    public void saveHistory(){
        SharedPreferences sharedPreferences2 = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        Gson gson = new Gson();
        sort_baseList();
        String json = gson.toJson(baseList);
        editor2.putString("baseMoodList",json);
        editor2.apply();

    }
    public void loadHistory(){
        SharedPreferences sharedPreferences2 = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences2.getString("baseMoodList",null);
        Type type = new TypeToken<ArrayList<baseMood>>(){}.getType();
        baseList = gson.fromJson(json,type);
        if (baseList == null){
            baseList = new ArrayList<>();
        }
        sort_baseList();
    }
    private void sort_baseList(){/** basic sort method*/
        Collections.sort(baseList, new Comparator<baseMood>() {
            @Override
            public int compare(baseMood o1, baseMood o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }

}