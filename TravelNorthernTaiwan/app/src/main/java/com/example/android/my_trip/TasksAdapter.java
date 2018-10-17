package com.example.android.my_trip;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;
import com.example.android.trip_organizer.TripsAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder>{
    ArrayList<Task> DataList;
    Context context;
    private DatabaseReference mRootReference;

    public TasksAdapter(ArrayList<Task> newTripList, Context newContext) {
        DataList = newTripList;
        context = newContext;
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travel-northern-taiwan.firebaseio.com/");
    }

    @NonNull
    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_template, parent, false);
        return new TasksAdapter.ViewHolder(view);
    }

    public void clearData() {
        DataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final TasksAdapter.ViewHolder holder, final int position) {
        String task = DataList.get(position).getTask();
        String isDone = task.substring(task.length()-1);
        holder.taskName.setText(task);
        if(isDone.equals("1")){
            holder.isDoneChecked.setChecked(true);
        }
        else{
            holder.isDoneChecked.setChecked(false);
        }

        //Deleting a card
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTasks(position);
                updateDatabase();
                DataList.remove(position);
                showAllData();
//                Log.d("delete", DataList.get(position).toString() + " " + DataList.get(position).getTripKey());
//                if(DataList!=null){
//                    for(Task task : DataList){
//                        boolean isDone = holder.isDoneChecked.isChecked();
//                        String check = (isDone) ? "1" : "0";
//                        if(task.getTask()+check.equals())
//                    }
////                    mRootReference.child("TripTasks").child(DataList.get(position).getTripKey()).child(DataList.get(position).toString()).removeValue();
//                }
            }
        });
    }

    private void updateDatabase() {
    }

    private void showAllData() {
        for(Task item : DataList) {
            Log.d("TASKSSSS", item.getTask());
        }
    }

    private void deleteTasks(int position){
        mRootReference.child("TripTasks").child(DataList.get(position).getTripKey()).removeValue();
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        ImageView deleteBtn;
        CheckBox isDoneChecked;
        CardView taskCard;

        public ViewHolder(View itemView) {
            super(itemView);
            taskName = (itemView).findViewById(R.id.taskName);
            deleteBtn = (itemView).findViewById(R.id.deleteBtn);
            taskCard = (itemView).findViewById(R.id.task_card);
            isDoneChecked = (itemView).findViewById(R.id.isDone);
        }
    }
}
