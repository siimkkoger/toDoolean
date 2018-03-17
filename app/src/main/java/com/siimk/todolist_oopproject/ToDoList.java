package com.siimk.todolist_oopproject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToDoList implements Parcelable{

    private static final String TAG = "ToDoList";

    /*
    Whole app has one single toDoList (in the beginning at least //TODO different subclass for different lists
    onCreate() method creates the list in the very beginning.
    In the end of the onCreate() method we fill the list with the data saved in the listData.txt file.
    The list acts as an intermediate between taskDescriptionList and ArrayAdapter (as ArrayAdapter takes in and
    formats Strings currently TODO make a custom ArrayAdapter (TaskAdapter).


    1) Read data into ToDoList
    2) Read ToDoList taskDescriptions into another ArrayList which acts as a content provider for ArrayAdapter
    3) Use adapter in listView to structure the tasks
     */


    private List<Task> tasks = new ArrayList<>();
    private List<String> taskDescriptionList = new ArrayList<>();
    private List<String> completedTaskDescriptionList = new ArrayList<>();


    public List<String> getCompletedTaskDescriptionList() {
        return completedTaskDescriptionList;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<String> getTaskDescriptionList() {
        return taskDescriptionList;
    }

    public ToDoList(){

     }

    public Task get(int index){
        return this.tasks.get(index);
    }

    public Task getTaskAt(int position){
        return this.tasks.get(position);
    }

    public void add(Task task){
        Log.d(TAG, "add: ----------- Task adding ---------- " + task.toString());
        this.tasks.add(task);
        this.taskDescriptionList.add(task.getDescription());
        if(task.isCompleted()){
            this.completedTaskDescriptionList.add(task.getDescription());
        }
    }

    public void remove(int position){
//        for(String task : completedTaskDescriptionList){
//            if(taskDescriptionList.get(position).equals(task)){
//                completedTaskDescriptionList.remove(task);
//            }
//        }
        List<String> newList = new ArrayList<>();
        this.tasks.remove(position);
        this.taskDescriptionList.remove(position);

        for(Task task : tasks){
            if(task.isCompleted()){
                newList.add(task.getDescription());
            }
        }
        this.completedTaskDescriptionList = newList;
    }

    public void addToIndex(int position, Task task){
        this.tasks.add(position, task);
        this.taskDescriptionList.add(position, task.getDescription());
        if(task.isCompleted()){
            this.completedTaskDescriptionList.add(task.getDescription());
        }
    }

    public ToDoList(List<Task> tasks) {
        this.tasks = tasks;
        for(Task task : tasks){
            this.taskDescriptionList.add(task.getDescription());
            if(task.isCompleted()){
                this.completedTaskDescriptionList.add(task.getDescription());
            }
        }
    }

    public int size(){
        return this.tasks.size();
    }

    public void removeOutdatedTasks()throws ParseException {
        List<Task> newList = new ArrayList<>();
        for(Task task : this.tasks){
            if(task.onAegunud()){
                newList.add(task);
            }
        }
        this.tasks.removeAll(newList);
    }

    public void sortByDateAndImportance(){
        Collections.sort(this.tasks);
    }

    /*
    ˇˇˇˇˇˇˇˇˇˇ------------ Parcelable ------------ˇˇˇˇˇˇˇˇˇˇ
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(tasks);
        dest.writeStringList(taskDescriptionList);
        dest.writeStringList(completedTaskDescriptionList);
    }

    protected ToDoList(Parcel in) {
        tasks = in.createTypedArrayList(Task.CREATOR);
        taskDescriptionList = in.createStringArrayList();
        completedTaskDescriptionList = in.createStringArrayList();
    }

    public static final Creator<ToDoList> CREATOR = new Creator<ToDoList>() {
        @Override
        public ToDoList createFromParcel(Parcel in) {
            return new ToDoList(in);
        }

        @Override
        public ToDoList[] newArray(int size) {
            return new ToDoList[size];
        }
    };
}
