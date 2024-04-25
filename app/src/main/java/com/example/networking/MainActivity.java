package com.example.networking;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.danieloskarsson.recyclerviewapp.RecyclerViewItem;
import com.danieloskarsson.recyclerviewapp.RecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity implements JsonTask.JsonTaskListener {

    private final String JSON_URL = "https://mobprog.webug.se/json-api?login=brom";
    private final String JSON_FILE = "mountains.json";

    private ArrayList<Mountain> mountainList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new JsonFile(this, this).execute(JSON_FILE);



        ArrayList<RecyclerViewItem> items = new ArrayList<>(Arrays.asList(
                new RecyclerViewItem("Matterhorn"),
                new RecyclerViewItem("Mont Blanc"),
                new RecyclerViewItem("Denali")
        ));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, items, new RecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(RecyclerViewItem item) {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView view = findViewById(R.id.recycler_view);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(adapter);

        getJson();

        for (Mountain mountain : mountainList) {
            items.add(new RecyclerViewItem(mountain.name));
        }
        adapter.notifyDataSetChanged();



    }

    @Override
    public void onPostExecute(String json) {
        Log.d("MainActivity", ""+json);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Mountain>>() {}.getType();
        mountainList = gson.fromJson(json, type);
    }

    private void getJson() {
        new JsonTask(this).execute(JSON_URL);
        new JsonFile(this, this).execute(JSON_FILE);
    }




}
