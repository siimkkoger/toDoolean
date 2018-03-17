package com.siimk.todolist_oopproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by siimk on 5/9/2017.
 */

public class TaskListActivity extends Activity {

    private static final String TAG = "TaskListActivity";
    private static final String dataFile = "TodoData";
    ToDoList ourToDoList = new ToDoList();
    ListView taskListView;
    ListView completedTasksListView;

    ArrayAdapter<String> taskListAdapter;
    ArrayAdapter<String> completedTaskListAdapter;

    List<String> tasks = new ArrayList<>();
    List<String> completedTasks = new ArrayList<>();

    Button addTaskButton;
    Button showCompletedButton;

    boolean listVisible = true;


    //---------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.task_list_layout);
        readTaskDataFromAppTextFile();


        completedTasksListView = (ListView) findViewById(R.id.completed_tasks);
        completedTasks = ourToDoList.getCompletedTaskDescriptionList();
        completedTaskListAdapter = new ArrayAdapter<String>(this, R.layout.single_row_2, R.id.textView5, completedTasks);
        completedTasksListView.setAdapter(completedTaskListAdapter);


        taskListView = (ListView) findViewById(R.id.task_list_main);
        tasks = ourToDoList.getTaskDescriptionList();
        taskListAdapter = new ArrayAdapter<String>(this, R.layout.single_row, R.id.textView4, tasks);
        taskListView.setAdapter(taskListAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // Choose an item from the ListView and send it to viewTask Activity()
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent listToViewIntent = new Intent();
                listToViewIntent.setClass(TaskListActivity.this, ViewTaskActivity.class);
                Task task = ourToDoList.getTaskAt(position);

                listToViewIntent.putExtra(IntentConstants.TASK_DATA_TO_VIEW, task);
                listToViewIntent.putExtra(IntentConstants.LIST_VIEW_TASK_POSITION_ONE, position);
                startActivityForResult(listToViewIntent, IntentConstants.REQUEST_LIST_TO_VIEW);
            }
        });


        addTaskButton = (Button) findViewById(R.id.buttonAddTask);
        addTaskButton.setOnClickListener(new View.OnClickListener() {     // Choose an item from the ListView and send it to addTask Activity()
            @Override
            public void onClick(View v) {
                Intent intentAddTask = new Intent();
                intentAddTask.setClass(TaskListActivity.this, AddItemActivity.class);
                startActivityForResult(intentAddTask, IntentConstants.REQUEST_LIST_TO_ADD);
            }
        });


        showCompletedButton = (Button) findViewById(R.id.buttonShowCompleted);
        showCompletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listVisible){
                    showCompletedButton.setText("SHOW ALL");
                    taskListView.setVisibility(View.INVISIBLE);
                    completedTasksListView.setVisibility(View.VISIBLE);
                    listVisible = false;
                }else{
                    showCompletedButton.setText("SHOW COMPLETED");
                    completedTasksListView.setVisibility(View.INVISIBLE);
                    taskListView.setVisibility(View.VISIBLE);
                    listVisible = true;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        Adds and item to the list
         */
        if(resultCode == IntentConstants.REQUEST_ADD_TO_LIST){
            Task task = (Task) data.getParcelableExtra(IntentConstants.TASK_DATA);
            ourToDoList.add(task);
            taskListAdapter.notifyDataSetChanged();
            completedTaskListAdapter.notifyDataSetChanged();
        }
        if(resultCode == IntentConstants.REQUEST_VIEW_TO_LIST){
            Task task1 = (Task) data.getParcelableExtra(IntentConstants.TASK_DATA_TWO);
            int position = data.getIntExtra(IntentConstants.LIST_VIEW_TASK_POSITION_ONE, -1);
            ourToDoList.remove(position);
            ourToDoList.addToIndex(position, task1);
            for(int i = 0; i < ourToDoList.size(); i++){
                Log.d(TAG, "onActivityResult: Task index " + i + " " + ourToDoList.get(i).toString());
            }
            taskListAdapter.notifyDataSetChanged();
            completedTaskListAdapter.notifyDataSetChanged();
        }
        if(resultCode == IntentConstants.REQUEST_VIEW_TO_LIST_DELETE){
            int position = data.getIntExtra(IntentConstants.LIST_VIEW_TASK_POSITION_ONE, -1);
            ourToDoList.remove(position);
            taskListAdapter.notifyDataSetChanged();
            completedTaskListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        saveTaskData();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        //TODO update the MainActivity layout's TOP 3 tasks - after everything else is working.
        ToDoList todoCopy = new ToDoList();
        for(Task task : ourToDoList.getTasks()){
            if(!task.isCompleted()){
                todoCopy.add(task);
            }
        }
        todoCopy.sortByDateAndImportance();
        Intent intent = new Intent();
        if(todoCopy.size() == 0){
            setResult(IntentConstants.C1, intent);
        }
        if(todoCopy.size() == 1){
            intent.putExtra(IntentConstants.BUTTON1, todoCopy.getTaskAt(0));
            setResult(IntentConstants.C2, intent);
        }
        if(todoCopy.size() == 2){
            intent.putExtra(IntentConstants.BUTTON1, todoCopy.getTaskAt(0));
            intent.putExtra(IntentConstants.BUTTON2, todoCopy.getTaskAt(1));
            setResult(IntentConstants.C3, intent);
        }
        if(todoCopy.size() >= 3){
            intent.putExtra(IntentConstants.BUTTON1, todoCopy.getTaskAt(0));
            intent.putExtra(IntentConstants.BUTTON2, todoCopy.getTaskAt(1));
            intent.putExtra(IntentConstants.BUTTON3, todoCopy.getTaskAt(2));
            setResult(IntentConstants.C4, intent);
        }
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void readTaskDataFromAppTextFile(){
        try {
            Scanner scanner = new Scanner(openFileInput(dataFile));
            while(scanner.hasNextLine()){
                String[] slicedLine = scanner.nextLine().split("~!~");
                String taskDescription = slicedLine[0];
                String taskDeadline = slicedLine[1];
                String taskNotes;
                if(slicedLine[4].equals("???x???")){
                    taskNotes = "";
                }else{
                    taskNotes = slicedLine[4];
                }
                boolean taskCompletitionState;
                boolean taskStarState;
                if(slicedLine[2].equalsIgnoreCase("true")){
                    taskCompletitionState = true;
                }else{
                    taskCompletitionState = false;
                }
                if(slicedLine[3].equalsIgnoreCase("true")){
                    taskStarState = true;
                }else{
                    taskStarState = false;
                }

                ourToDoList.add(new Task(taskDescription, taskDeadline, taskCompletitionState, taskStarState, taskNotes));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            //Log.d(TAG, "readTaskDataFromAppTextFile: file TodoData.txt was not found ----- requires immediate action!");
        }
    }

    private void saveTaskData(){
        try {
            PrintWriter printWriter = new PrintWriter(openFileOutput(dataFile, Context.MODE_PRIVATE));
            for(Task task : ourToDoList.getTasks()){
                // -------------- TODO deadline passed - kui aega jääb enne projekti tähtaega
                String isCompleted = "false";
                String isStarred = "false";
                String notes;
                if(task.isStarred()){
                    isStarred = "true";
                }
                if(task.isCompleted()){
                    isCompleted = "true";
                }
                if(task.getNotes().equals("")){
                    notes = "???x???";
                }else{
                    notes = task.getNotes();
                }

                printWriter.print(task.getDescription() + "~!~" +
                        task.getDeadline() + "~!~" +
                        isCompleted + "~!~" +
                        isStarred + "~!~" + notes + "\n");

                Log.d(TAG, "SAVE SUCCESSFUL: " + task.getDescription() + "~!~" +
                        task.getDeadline() + "~!~" +
                        isCompleted + "~!~" +
                        isStarred + "~!~" + notes + "\n");
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            //Log.d(TAG, "onPause: Sth wrong with writing into file TodoData.txt ----- requires immediate action!");
        }
    }

}

























































