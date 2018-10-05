package com.CMPUT301.ruiqin.FeelsBook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class EditPage extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    private Button LoveButtom,joyButton,surpriseButton,angerButton,fearButton,sadButton,dateButton,timeButton,confirmButton;
    private EditText yourInput;
    private Intent intent;
    private ArrayList<baseMood> baseList;
    private int position;
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
    private String date_in_format;
    public String currentMood;
    public Date date;
    public Calendar calendar;

    View.OnClickListener buttonListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_page);
    }

    /**The following 2 functions
    * On dateSet and OnTimeSet are referenced from
     * name: code in flow
     * time: 2017-10-30 & 2017-10-27
    * https://www.youtube.com/watch?v=33BFCdL0Di0&t=57s
     * https://www.youtube.com/watch?v=QMwaNN_aM3U&t=310s
     * license: https://developer.android.com/license/w3c.txt
    */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
            date_to_calendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        Date date4 = calendar.getTime();
        baseList.get(position).setDate(date4);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {//connected with timePicker
        try {
            date_to_calendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        Date date3 = calendar.getTime();
        baseList.get(position).setDate(date3);
    }
    /**
     * reference ends**/

    @Override
    protected void onStart() {
        loadHistory();
        super.onStart();
        get_pos();
        /**
         * pass the original texts for the user to edit in editview
         */
        yourInput = (EditText) findViewById(R.id.YourMessage);
        yourInput.setText(baseList.get(position).getComment());
        buttonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){/**reset the mood, time, date*/
                case R.id.happy:
                    currentMood = "Happy";
                    baseList.get(position).setMood(currentMood);
                    Toast.makeText(EditPage.this,"Happy is joy..", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.angery:
                    currentMood = "Angry";
                    baseList.get(position).setMood(currentMood);
                    break;
                case R.id.scared:
                    currentMood = "scared";
                    baseList.get(position).setMood(currentMood);
                    Toast.makeText(EditPage.this,"Scared is fear..", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.surprise:
                    currentMood = "surprised";
                    baseList.get(position).setMood(currentMood);
                    break;
                case R.id.sad:
                    currentMood = "sad";
                    baseList.get(position).setMood(currentMood);
                    break;
                case R.id.love:
                    currentMood = "love";
                    baseList.get(position).setMood(currentMood);
                    break;
                case R.id.confirm_buttom:/**confirm the change?*/
                    set_text_back_to_object();
                    Toast.makeText(EditPage.this,"Words saved", Toast.LENGTH_SHORT).show();
                    jump_back();/**jump to history page after confirm*/
                    break;
                case R.id.choose_date:
                    DialogFragment datePicker = new com.CMPUT301.ruiqin.FeelsBook.DatePicker();
                    datePicker.show(getSupportFragmentManager(),"Date picker");
                    break;
                case R.id.choose_time:
                    DialogFragment timePicker = new TimePicker();
                    timePicker.show(getSupportFragmentManager(),"Time picker");
                    break;
                }
            }
        };
        set_button_onclick();


    }

    @Override
    protected void onStop() {//to
        saveHistory();
        super.onStop();
        finish();
    }

    /**
     * we need to change the date in to calender form
     * in order to fit with the elements modified from time&date pickers
     * @throws ParseException
     */
    public void date_to_calendar() throws ParseException {
        date = baseList.get(position).getDate();//get date from the message we chose
        date_in_format = simpleDateFormat2.format(date);
        Date date2 = simpleDateFormat2.parse(date_in_format);
        calendar = Calendar.getInstance();
        calendar.setTime(date2);
    }

    public void set_button_onclick(){
        LoveButtom = (Button) findViewById(R.id.love);
        LoveButtom.setOnClickListener(buttonListener);
        joyButton = (Button) findViewById(R.id.happy);
        joyButton.setOnClickListener(buttonListener);
        surpriseButton = (Button) findViewById(R.id.surprise);
        surpriseButton.setOnClickListener(buttonListener);
        angerButton = (Button) findViewById(R.id.angery);
        angerButton.setOnClickListener(buttonListener);
        fearButton = (Button) findViewById(R.id.scared);
        fearButton.setOnClickListener(buttonListener);
        sadButton = (Button) findViewById(R.id.sad);
        sadButton.setOnClickListener(buttonListener);
        dateButton = (Button) findViewById(R.id.choose_date);
        timeButton = (Button) findViewById(R.id.choose_time);
        confirmButton = (Button) findViewById(R.id.confirm_buttom);
        dateButton.setOnClickListener(buttonListener);
        timeButton.setOnClickListener(buttonListener);
        confirmButton.setOnClickListener(buttonListener);
    }

    /**
     * get the position of each history information from history list,
     * make sure the one we modified is what we clicked
     */
    private void get_pos(){
        intent = getIntent();
        position = intent.getExtras().getInt("position");
    }

    /**a
     * again, save and load methods
     */
    public void loadHistory(){
        SharedPreferences sharedPreferences2 = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences2.getString("baseMoodList",null);
        Type type = new TypeToken<ArrayList<baseMood>>(){}.getType();
        baseList = gson.fromJson(json,type);
        if (baseList == null){
            baseList = new ArrayList<>();
        }
    }
    public void saveHistory(){
        SharedPreferences sharedPreferences2 = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        Gson gson = new Gson();
        if (baseList.size()>=2){
            sort_baseList();
        }
        String json = gson.toJson(baseList);
        editor2.putString("baseMoodList",json);
        editor2.apply();
    }
    private void sort_baseList(){//sort the base_mood_list by date
        Collections.sort(baseList, new Comparator<baseMood>() {
            @Override
            public int compare(baseMood o1, baseMood o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        //RecycleViewAdapter.notifyDataSetChanged();
    }
    private void jump_back(){
        saveHistory();//TO go to new.............
        Intent intent4 = new Intent(EditPage.this,HistoryPageNev.class);
        startActivity(intent4);

    }

    /**
     * set the new message to the mood we chose from history
     */
    private void set_text_back_to_object(){
        String tempMessage = yourInput.getText().toString();
        baseList.get(position).setComment(tempMessage);
    }


}
