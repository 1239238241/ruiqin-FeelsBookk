package com.CMPUT301.ruiqin.FeelsBook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**https://youtu.be/4uIueyvPCfo
 *
 */
public class MainActivity extends AppCompatActivity {

    private Button LoveButtom,joyButton,surpriseButton,angerButton,fearButton,sadButton, clean2Buttom,clearButton,statButton;
    private EditText yourInput;
    private TextView sec_Text,statCount;/** the 'sec_Text' is used for preview your input, statCount is used for show counts on emotions*/
    private Date date;
    private baseMood emotion = new baseMood(date,"","");//construct
    private SimpleDateFormat simpleDateFormat;/** used to show the time in string and in our required format*/
    private Calendar calendar;
    private ArrayList<baseMood> baseMoodArrayList; /**Array list to store a set of mood inputed by user*/
    private Integer love1,joy1,surprise1,anger1,fear1,sad1;/**used to store the count for each emotion*/

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public String text, currentMood;
    public HashMap<String,Integer> hashMap = new HashMap();/**used to count the elements in array list*/
    View.OnClickListener buttonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initial textInfo

        setContentView(R.layout.activity_main);
        sec_Text = (TextView) findViewById(R.id.sec_Text);
        sec_Text.setMovementMethod(new ScrollingMovementMethod());/**give a scroll bar for textview*/
        yourInput = (EditText) findViewById(R.id.YourMessage);
        setCountView();/**initial the textview for counter*/
        //final Intent passValues = new Intent(this,baseMood.class);
        buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.clean2:/**this button is used to clean the preview*/
                        sec_Text.setText("Welcome");
                        Toast.makeText(MainActivity.this,"Preview Cleared", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.joy:
                        currentMood = "Happy";
                        sec_Text.setText(currentMood);
                        save_current_input();/**save the mood and store it into arraylist*/
                        Toast.makeText(MainActivity.this,"Happy is Joy..", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.anger:
                        currentMood = "Angry";
                        sec_Text.setText(currentMood);
                        save_current_input();
                        Toast.makeText(MainActivity.this,"Angry is anger..", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.fear:
                        currentMood = "scared";
                        sec_Text.setText(currentMood);
                        save_current_input();
                        Toast.makeText(MainActivity.this,"Scared is fear..", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.suprise:
                        currentMood = "surprised";
                        sec_Text.setText(currentMood);
                        save_current_input();
                        Toast.makeText(MainActivity.this,"surprised is surprise..", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sad:
                        currentMood = "sad";
                        sec_Text.setText(currentMood);
                        save_current_input();
                        Toast.makeText(MainActivity.this,"sad is sadness..", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.love:
                        currentMood = "love";
                        sec_Text.setText(currentMood);
                        save_current_input();
                        Toast.makeText(MainActivity.this,"love is love..", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.clear:
                        yourInput.getText().clear();
                        currentMood = null;
                        //arrayList_to_hashMap();
                        //need to do more...
                        Toast.makeText(MainActivity.this,"Input Cleared", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.stat_button:
                        showCount();
                        break;
                }

            }
        };
        set_button_onclick();/**set those button*/

    }
    @Override
    protected void onStart(){
        super.onStart();
        loadHistory();/**when return to this activity loadHistory*/
    }
    @Override
    protected void onStop() {
        super.onStop();
        statCount.setVisibility(View.INVISIBLE);
        saveHistory();/**when leave this activity, save the current information*/
    }
    /**copy the menu function from professor's videos
     * from Abram Hindle
     * published 2014-09-11
     * https://www.youtube.com/watch?v=fxjIA4HIruU&feature=youtu.be*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    //The method we used to call menu//no more change
    public void editHistoy(MenuItem menu){
        saveHistory();
        Toast.makeText(this,"History", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HistoryPageNev.class);
        startActivity(intent);
    }
    /**copy ends*/

    /**sharedperference storing
     * from Google Developer
     * licence: https://developer.android.com/license/w3c.txt
     * https://developer.android.com/reference/android/content/SharedPreferences
     */
    public void save_current_input(){//one click called once, add mood into list once
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String tempMessage = yourInput.getText().toString();
        String tempMood = currentMood;
        date = new Date();
        baseMoodArrayList.add(new baseMood(date,tempMood,tempMessage));
        //2018-09-01T18:30:00
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String tempDate = simpleDateFormat.format(calendar.getTime());
        sec_Text.setText("Here is your input:"+ "\n"+ tempDate + "\n"+ tempMood +"\n"+ tempMessage);/**set the content for each mood choose*/
        editor.putString(TEXT,sec_Text.getText().toString());
        Toast.makeText(this,"Words saved", Toast.LENGTH_SHORT);
        editor.apply();


    }
    public void setCountView(){/**initialize statCount textview*/
        statCount =(TextView) findViewById(R.id.statCount);
        statCount.setVisibility(View.INVISIBLE);

    }
    /**purpose here is to
     * not always show the counter. when press the button, the state changed
     */

    public void showCount(){
        if (statCount.getVisibility() == View.VISIBLE) {
            statCount.setVisibility(View.INVISIBLE);
        }
        else{
            statCount.setVisibility(View.VISIBLE);
        }
        arrayList_to_hashMap();

    }

    /** use hashmap to counter the elements in arraylist
     * inital the keys in hashmap first, in order to prevent null pointer error
     */
    public void arrayList_to_hashMap(){
        hashMap.put("love",0);
        hashMap.put("surprised",0);
        hashMap.put("sad",0);
        hashMap.put("scared",0);
        hashMap.put("Angry",0);
        hashMap.put("Happy",0);
        for(baseMood mood : baseMoodArrayList){
            hashMap.put(mood.getMood(), hashMap.get(mood.getMood())+1);
        }
        love1 = hashMap.get("love");
        joy1 = hashMap.get("Happy");
        surprise1 = hashMap.get("surprised");
        anger1 =hashMap.get("Angry");
        fear1 = hashMap.get("scared");
        sad1 = hashMap.get("sad");
        statCount.setText("Love has: "+ love1.toString() + "\n"+
                          "Happy has: " + joy1.toString() + "\n"+
                          "Surprised has: " + surprise1.toString() +"\n"+
                          "Angry has: "+anger1.toString()+"\n"+
                          "Scared has: "+fear1.toString() +"\n"+
                          "Sad has: "+sad1.toString());/** the format printed in textview */
    }

    /** initialize the buttons
     *
     */
    public void set_button_onclick(){
        LoveButtom = (Button) findViewById(R.id.love);
        LoveButtom.setOnClickListener(buttonListener);
        joyButton = (Button) findViewById(R.id.joy);
        joyButton.setOnClickListener(buttonListener);
        surpriseButton = (Button) findViewById(R.id.suprise);
        surpriseButton.setOnClickListener(buttonListener);
        angerButton = (Button) findViewById(R.id.anger);
        angerButton.setOnClickListener(buttonListener);
        fearButton = (Button) findViewById(R.id.fear);
        fearButton.setOnClickListener(buttonListener);
        sadButton = (Button) findViewById(R.id.sad);
        sadButton.setOnClickListener(buttonListener);
        clean2Buttom = (Button) findViewById(R.id.clean2);
        clean2Buttom.setOnClickListener(buttonListener);
        clearButton = (Button) findViewById(R.id.clear);
        clearButton.setOnClickListener(buttonListener);
        statButton = (Button) findViewById(R.id.stat_button);
        statButton.setOnClickListener(buttonListener);

    }

    /** save and load Gson method are referenced and learned from
     * name: Jake Wharton
     * Date: 2015
     * url: https://github.com/google/gson
     * license: Copyright 2008 Google Inc.

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
     */
    public void saveHistory(){
        SharedPreferences sharedPreferences2 = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        Gson gson = new Gson();
        String json = gson.toJson(baseMoodArrayList);/**save in gson format*/
        editor2.putString("baseMoodList",json);
        editor2.apply();

    }
    public void loadHistory(){
        SharedPreferences sharedPreferences2 = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences2.getString("baseMoodList",null);
        Type type = new TypeToken<ArrayList<baseMood>>(){}.getType();
        baseMoodArrayList = gson.fromJson(json,type);
        if (baseMoodArrayList == null){/**if this list is null, then initialize it*/
            baseMoodArrayList = new ArrayList<>();
        }
    }




}
