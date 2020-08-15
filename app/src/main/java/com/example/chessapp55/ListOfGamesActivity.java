package com.example.chessapp55;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ChessModel.SavedGames;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


//implement
//display the games, have user select game at which point app loads recorded game activity
// and passes the game as a parameter to it
//sort by name and date
public class ListOfGamesActivity extends AppCompatActivity {
    CustomAdapter adbPerson;
    ArrayList<SavedGames> myListItems  = new ArrayList<SavedGames>();
    ListView listview;
    Button datesort, namesort, chessgame;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_games);
        loadData();
        adbPerson= new CustomAdapter(this, 0, myListItems);
        listview = (ListView) findViewById(R.id.gamesList);
        datesort = (Button) findViewById(R.id.DateSort);
        namesort = (Button) findViewById(R.id.sortBy);
        chessgame = (Button) findViewById(R.id.playGame);
        chessgame.setOnClickListener(v -> goback());
        datesort.setOnClickListener(v->datesort());
        namesort.setOnClickListener(v->namesort());
        listview.setAdapter(adbPerson);
        listview.setOnItemClickListener(this::onItemClick);
    }
    //when a listview element is clicked it will go onto the next activity
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("ListView", "You clicked Item: " + id + " at position:" + position);
        // Then you start a new Activity via Intent
        Intent intent = new Intent();
        intent.setClass(this, RecordedGameActivity.class);
        saveData(position);
        startActivity(intent);
    }
    //saves the selected game to pass on to RecordedGameActivity
    public void saveData(int position){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myListItems.get(position));
        editor.putString("currgame",json);
        editor.apply();
    }
    //loads all of the games that were previously saved into mListItems
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("loadgames",null);
        Type type = new TypeToken<ArrayList<SavedGames>>(){}.getType();
        myListItems = gson.fromJson(json,type);
        if(myListItems == null){
            myListItems = new ArrayList<>();
        }
    }
    public void goback(){
        Intent menuIntent = new Intent(this, ChessGameActivity.class);
        startActivity(menuIntent);

    }
    public void namesort(){
        Collections.sort(myListItems, SavedGames.NameComparator);
        listview.setAdapter(adbPerson);
    }
    public void datesort(){
        Collections.sort(myListItems,SavedGames.dateComparator);
        listview.setAdapter(adbPerson);

    }

}
/*
Collections.sort(films, new Comparator<Film>() {
@Override
public int compare(Film o1, Film o2) {
        return Double.compare(o1.getRating(), o2.getRating());
        }
        });
*/