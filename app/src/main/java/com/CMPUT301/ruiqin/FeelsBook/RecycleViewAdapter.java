package com.CMPUT301.ruiqin.FeelsBook;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    //define a list of each cardView category. Each item in list is represented in cardView format
    private ArrayList<baseMood> mbaseMoodsList2;
    private Date date2;
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
    private String date_in_format;
    private OnItemClickListener mlistener;


    /**
     * This part is used to combine cardview and recycleview
     * I learn to build this adapter from many sources
     * name: google, Ashraff Hathibelagal,Rashid Coder
     * time: 2015-03-23, 2018-01-11
     * url:
     * https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder
     * https://developer.android.com/reference/android/support/v7/widget/CardView
     * https://developer.android.com/guide/topics/ui/layout/cardview
     * https://developer.android.com/guide/topics/ui/layout/recyclerview
     * https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
     * https://www.youtube.com/watch?v=QTiQawMBPUA
     * licence: license: https://developer.android.com/license/w3c.txt
     */
    public class ViewHolder extends RecyclerView.ViewHolder{//deleted static
        //define
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;

        public ViewHolder(View itemView, final OnItemClickListener listener2){//link the parts from cardview
            super(itemView);
            textView1 = itemView.findViewById(R.id.text_view_title);//date
            textView2 = itemView.findViewById(R.id.text_view_Priority);//mood
            textView3 = itemView.findViewById(R.id.text_view_message);//message

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener2 != null){
                        listener2.onItemClick(position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener  listener){
        mlistener = listener;
    }

    public RecycleViewAdapter(ArrayList<baseMood> baseMoodsList2){
        //constructor for this list
        //once called by 'new' baseMoodsList2 will past the item to mbaseMoodList2
        mbaseMoodsList2 = baseMoodsList2;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_mood_item,parent,false);
        //once called adapter, then create the holder
        ViewHolder viewHolder =new ViewHolder(v,mlistener);
        return viewHolder;

    }

    /**
     * when swpie to the left, call delete method from mood
     * @param position
     */
    public void deleteMood(int position){//position make sure which info we want to delete connected with onclickListener
        mbaseMoodsList2.remove(position);//remove that info from our universe list that presented in history
        //notifyDataSetChanged(); no animation
        notifyItemRemoved(position);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //set contents to cardView
        baseMood currentMood = mbaseMoodsList2.get(position);
        //set date presenting format
        date2 = currentMood.getDate();
        date_in_format = simpleDateFormat2.format(date2);
        date_in_format = date_in_format.substring(0,10) + 'T' + date_in_format.substring(12,date_in_format.length());
        //format setting ends
        holder.textView1.setText(date_in_format);
        holder.textView2.setText(currentMood.getMood());
        holder.textView3.setText(currentMood.getComment());
    }

    /**
     * this method is used for check if history list is empty or  not
     * @return
     */
    @Override
    public int getItemCount() {

        return mbaseMoodsList2.size();
    }
}
