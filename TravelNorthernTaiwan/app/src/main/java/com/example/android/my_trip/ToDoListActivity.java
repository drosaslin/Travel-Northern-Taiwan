package com.example.android.my_trip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.travelnortherntaiwan.R;
import com.example.android.trip_organizer.ChooseRegionActivity;
import com.example.android.trip_organizer.TripsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {
    private ImageView addTaskBtn;
    private RecyclerView mRecyclerView;
    private TasksAdapter mAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRootReference;
    private ArrayList<Task> DataList;
    private String currentKey;
    private EditText taskInput;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list);
        DataList = new ArrayList<Task>();

        taskInput = findViewById(R.id.task_input);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        currentKey = getIntent().getStringExtra("tripKey");
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com/TripTasks/" + currentKey);
        mRecyclerView = findViewById(R.id.task_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Log.d("ON CREATE", "on create");
        mRootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearCards();
                showData(dataSnapshot);
                mAdapter = new TasksAdapter(DataList, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addTaskBtn = findViewById(R.id.add_task);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!taskInput.getText().toString().trim().equals("")) {
                    addTask(taskInput.getText().toString());
                }
                else {
                    Toast.makeText(ToDoListActivity.this, "Please input a task first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Task task = new Task();
            String tripTask = ds.getValue().toString();
            tripTask = tripTask.substring(0,tripTask.length() - 1);

            task.setTask(tripTask);
            task.setIsDone(tripTask.substring(tripTask.length() - 1));
            task.setTripKey(currentKey);

            DataList.add(task);
        }
    }

    private void addTask(String newTask){
        int n = DataList.size();
        String child = Integer.toString(n);
        Toast.makeText(this, "add task func", Toast.LENGTH_SHORT).show();
        mRootReference.child(child).setValue(newTask + "0");
        taskInput.setText("");
    }

    private void clearCards(){
        if(mAdapter != null) {
            mAdapter.clearData();
        }
    }

}
