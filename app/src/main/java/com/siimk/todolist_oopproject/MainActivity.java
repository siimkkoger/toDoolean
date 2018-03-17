package com.siimk.todolist_oopproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.util.Scanner;

/*
    This activity represents the first screen
    TODO add function to button present but finish everything else after other activities are ready
 */

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final String dataFile = "TodoData";

    ToDoList top3todo = new ToDoList();

    Button toMyTasksButton;
    Button button1;
    Button button2;
    Button button3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        setContentView(R.layout.front_page_layout);
        readTaskDataFromAppTextFile();

        top3todo.sortByDateAndImportance();

        ToDoList top3copy = new ToDoList();
        for(Task task : top3todo.getTasks()){
            if(!task.isCompleted()){
                top3copy.add(task);
            }
        }

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        for(int i = 0; i < top3copy.size(); i++){
            switch (i){
                case 0:
                    button1.setText(top3copy.getTaskAt(i).getDescription() + "    " + top3copy.getTaskAt(i).getDeadline());
                    break;
                case 1:
                    button2.setText(top3copy.getTaskAt(i).getDescription() + "    " + top3copy.getTaskAt(i).getDeadline());
                    break;
                case 2:
                    button3.setText(top3copy.getTaskAt(i).getDescription() + "    " + top3copy.getTaskAt(i).getDeadline());
            }
        }

        toMyTasksButton = (Button) findViewById(R.id.buttonStart);
        toMyTasksButton.setOnClickListener(new View.OnClickListener() { // Opens the main listView Activity() and waits for new tasks
            @Override
            public void onClick(View v) {
                Intent toTaskListIntent = new Intent();
                toTaskListIntent.setClass(MainActivity.this, TaskListActivity.class);
                startActivityForResult(toTaskListIntent, IntentConstants.REQUEST_FRONT_TO_LIST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Task task1;
        Task task2;
        Task task3;

        if(resultCode == IntentConstants.C1){

        }
        if(resultCode == IntentConstants.C2){
            task1 = data.getParcelableExtra(IntentConstants.BUTTON1);
            button1.setText(task1.getDescription() + "  " + task1.getDeadline());
        }
        if(resultCode == IntentConstants.C3){
            task1 = data.getParcelableExtra(IntentConstants.BUTTON1);
            button1.setText(task1.getDescription() + "  " + task1.getDeadline());
            task2 = data.getParcelableExtra(IntentConstants.BUTTON2);
            button2.setText(task2.getDescription() + "  " + task2.getDeadline());
        }
        if(resultCode == IntentConstants.C4){
            task1 = data.getParcelableExtra(IntentConstants.BUTTON1);
            button1.setText(task1.getDescription() + "  " + task1.getDeadline());
            task2 = data.getParcelableExtra(IntentConstants.BUTTON2);
            button2.setText(task2.getDescription() + "  " + task2.getDeadline());
            task3 = data.getParcelableExtra(IntentConstants.BUTTON3);
            button3.setText(task3.getDescription() + "  " + task3.getDeadline());
        }

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

                top3todo.add(new Task(taskDescription, taskDeadline, taskCompletitionState, taskStarState, taskNotes));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "readTaskDataFromAppTextFile: file TodoData.txt was not found ----- requires immediate action!");
            return;
        }
    }

}
























































