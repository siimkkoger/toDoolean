package com.siimk.todolist_oopproject;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.content.ContentValues.TAG;

/**
 * Created by siimk on 5/9/2017.
 */

public class AddItemActivity extends Activity {

    Button saveTaskButton;
    EditText chooseDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_task_layout);

        saveTaskButton = (Button) findViewById(R.id.buttonAddTaskConfirm);
        chooseDate = (EditText) findViewById(R.id.plainTextChooseDate);

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = ((EditText) findViewById(R.id.plainTextNewTask)).getText().toString();
                String notes = ((EditText) findViewById(R.id.plainTextNotes)).getText().toString();
                String deadline = ((EditText) findViewById(R.id.plainTextChooseDate)).getText().toString();
                if(description.equals("")){
                    finish();
                }else{
                    Intent outputIntent = new Intent();
                    boolean starred = false;
                    boolean completed = false;

                    Task newTask = new Task(description, deadline, completed, starred, notes);
                    Log.d(TAG, "Just sai task tehtud: " + newTask.getDeadline());
                    outputIntent.putExtra(IntentConstants.TASK_DATA, newTask);
                    setResult(IntentConstants.REQUEST_ADD_TO_LIST, outputIntent);
                    finish();
                }
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void setDatePicked(String date){
        this.chooseDate.setText(date);
    }
}






















