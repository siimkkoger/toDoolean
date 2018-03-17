package com.siimk.todolist_oopproject;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ViewTaskActivity extends Activity {

    private static final String TAG = "ViewTaskActivity";

    TextView taskDescription;
    EditText notes;
    Button completedButton;
    Button starredButton;
    Button deleteButton;
    Button saveButton;
    EditText chooseDate;
    Task viewableTask;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.task_view_layout);

        Intent inputIntent = getIntent();
        viewableTask = (Task) inputIntent.getParcelableExtra(IntentConstants.TASK_DATA_TO_VIEW);
        position = inputIntent.getIntExtra(IntentConstants.LIST_VIEW_TASK_POSITION_ONE, -1);

        Log.d(TAG, "onCreate: ............ Task: " + viewableTask.toString());

        completedButton = (Button) findViewById(R.id.buttonSetCompleted);
        starredButton = (Button) findViewById(R.id.buttonSetImportant);
        deleteButton = (Button) findViewById(R.id.buttonRemoveTask);
        saveButton = (Button) findViewById(R.id.buttonSaveTask);
        chooseDate = (EditText) findViewById(R.id.editTextChooseDate);
        notes = (EditText) findViewById(R.id.plainTextNotes);
        taskDescription = (TextView) findViewById(R.id.textViewTaskDesc);


        if(viewableTask.isCompleted()){
            taskDescription.setText(viewableTask.getDescription() + "   " + GlobalFunctions.setEmoji(GlobalFunctions.TICK));
        }else {
            taskDescription.setText(viewableTask.getDescription());
        }

        notes.setText(viewableTask.getNotes());
        chooseDate.setText(viewableTask.getDeadline());

        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewableTask.isCompleted()){
                    //viewableTask.setDescription(viewableTask.getDescription().replace("   " + GlobalFunctions.setEmoji(GlobalFunctions.TICK), ""));
                    taskDescription.setText(viewableTask.getDescription().replace("   " + GlobalFunctions.setEmoji(GlobalFunctions.TICK), ""));
                    viewableTask.setCompleted(false);
                }else{
                    //viewableTask.setDescription(viewableTask.getDescription() + "   " + GlobalFunctions.setEmoji(GlobalFunctions.TICK));
                    taskDescription.setText(viewableTask.getDescription() + "   " + GlobalFunctions.setEmoji(GlobalFunctions.TICK));
                    viewableTask.setCompleted(true);
                }
                Log.d(TAG, "onClick: ----- viewable Task is complete?: " + viewableTask.isCompleted());
            }
        });

        starredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewableTask.isStarred()){
                    viewableTask.setDescription(viewableTask.getDescription().replace("   " + GlobalFunctions.setEmoji(GlobalFunctions.STAR), ""));
                    taskDescription.setText(viewableTask.getDescription());
                    viewableTask.setStarred(false);
                }else{
                    viewableTask.setDescription(viewableTask.getDescription() + "   " + GlobalFunctions.setEmoji(GlobalFunctions.STAR));
                    taskDescription.setText(viewableTask.getDescription());
                    viewableTask.setStarred(true);
                }
                Log.d(TAG, "onClick: ----- viewable Task is starred?: " + viewableTask.isStarred());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outputIntent1 = new Intent();
                outputIntent1.putExtra(IntentConstants.LIST_VIEW_TASK_POSITION_ONE, position);
                setResult(IntentConstants.REQUEST_VIEW_TO_LIST_DELETE, outputIntent1);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outputIntent2 = new Intent();
                viewableTask.setNotes(((EditText)findViewById(R.id.plainTextNotes)).getText().toString());
                viewableTask.setDeadline(((EditText)findViewById(R.id.editTextChooseDate)).getText().toString());
                viewableTask.setDescription(((TextView)findViewById(R.id.textViewTaskDesc)).getText().toString().replace("   " + GlobalFunctions.setEmoji(GlobalFunctions.TICK), ""));
                outputIntent2.putExtra(IntentConstants.TASK_DATA_TWO, viewableTask);
                outputIntent2.putExtra(IntentConstants.LIST_VIEW_TASK_POSITION_ONE, position);
                setResult(IntentConstants.REQUEST_VIEW_TO_LIST, outputIntent2);
                finish();
            }
        });

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                newFragment.show(fragmentTransaction, "pick_date");
            }
        });
    }

    protected void setDatePicked(String date){
        this.chooseDate.setText(date);
    }

}



































