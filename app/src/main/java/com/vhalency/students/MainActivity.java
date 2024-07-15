package com.vhalency.students;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EstudentAdapter.StudentAdapterListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private EstudentAdapter mAdapter;
    private SearchView searchView;

    RecyclerView recyclerView;
    ArrayList<Student> list;

    FloatingActionButton fab;

    public static SQLiteHelper sqLiteHelper;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.gridView);
        searchView = (SearchView) findViewById(R.id.searchView);



      //  mAdapter.notifyDataSetChanged();

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddStudent.class);
                startActivity(intent);
            }
        });


        //search

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

    }

    private void initi() {

        sqLiteHelper = new SQLiteHelper(this, "StudentDB.sqlite", null, 1);
        list = new ArrayList<>();
        // get all data from sqlite
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS STUDENT (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, lastname VARCHAR, image BLOB, observation VARCHAR)");

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM STUDENT");
        if (cursor != null && cursor.moveToFirst()) {
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String lastname = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            String observation = cursor.getString(4);

            list.add(new Student(name, lastname, image, id, observation));
        }

        mAdapter = new EstudentAdapter(this, list, this);

        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

    }
    }

    @Override
    public void onContactSelected(Student contact) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        initi();
    }
}