package com.siimk.todolist_oopproject;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;


public class Task implements Parcelable, Comparable<Task>{ //TODO ----- Research Parcelable interface and methods to understand it better!!!

    private static final String TAG = "Task";
    private String description = null;
    private boolean starred = false;
    private String deadline = null;
    private boolean completed = false;
    private String notes = null;

    public Task(String description, String deadline, boolean completed, boolean starred, String notes){
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
        this.starred = starred;
        this.notes = notes;
    }

    protected Task(Parcel in) {
        description = in.readString();
        starred = in.readByte() != 0;
        deadline = in.readString();
        completed = in.readByte() != 0;
        notes = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeByte((byte) (starred ? 1 : 0));
        dest.writeString(deadline);
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeString(notes);
    }

    /*
    Constructoris määrab ka shortDesc(ription)-i - seda kasutame DeadlineTaskide TableViews esimese
    tulbana. Oli plaan teha nii, et peale klikates task-ile tuleb uus aken lahti, kus on tervet teksti näha -
    nt. kui description on terve A4. Lõpuprojekti lisab ka selle, kui jätkame samat (tn Androidil), praegu
    jäi tegemine nii viimsetele minutitele :p
     */

    public String getNotes(){
        return this.notes;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public Task(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public String getDescription(){
        return this.description;
    }


    public String getDeadline() {
        return this.deadline;
    }

    public boolean onAegunud() throws ParseException {
        return false;
    }

    /*
    Txt file lugedes tahame, et oleks listis vaid üks kord samat String väärtust, seega kui Task-e
    võrdleme, siis võrdleme väljade String väärtusi, isegi kui objektid tegelikult erinevad.
     */
    @Override
    public boolean equals(Object objekt){
        if(!(objekt instanceof Task)){
            return false;
        }
        Task otherTask = (Task)objekt;
        return this.getDeadline().equals(otherTask.getDeadline()) &&
                this.getDescription().equals(otherTask.getDescription());
    }

    public String shortenDescription(){
        if(this.description.length() < 50){
            return this.description;
        }else{
            return this.description.substring(0, 50) + "...";
        }
    }

    @Override
    public String toString(){
        return "Description: " + this.description + " --- " +
                "Deadline: " + this.deadline + " --- " +
                "Completed: " + this.completed + " --- " +
                "Starred: " + this.starred + " --- " +
                "Notes: " + this.notes;
    }

    public boolean taskIsEarlierThan(Task otherTask){
        String[] slicedThisDate = this.getDeadline().split(" / ");
        String[] slicedOtherDate = this.getDeadline().split(" / ");
        if(slicedThisDate.length != 3 || slicedOtherDate.length != 3){
            Log.d(TAG, "taskIsEarlierThan: THE DATE IS NOT SAVED CORRECTLY!");
        }

        int year1;
        int year2;
        int month1;
        int month2;
        int day1;
        int day2;

        try{
            year1 = Integer.parseInt(slicedThisDate[2]);
            month1 = Integer.parseInt(slicedThisDate[1]);
            day1 = Integer.parseInt(slicedThisDate[0]);
            year2 = Integer.parseInt(slicedThisDate[2]);
            month2 = Integer.parseInt(slicedThisDate[1]);
            day2 = Integer.parseInt(slicedThisDate[0]);

        }catch (IndexOutOfBoundsException e){
            return true;
        }

        if(year1 == year2){

            if(month1 == month2){

                if(day1 == day2){
                    return false;
                }else if(day1 < day2){
                    return true;
                }else {
                    return false;
                }

            }else if(month1 < month2){
                return true;
            }else {
                return false;
            }

        }else if(year1 < year2){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull Task o) {

        if(this.taskIsEarlierThan(o)){
            return -1;
        }else if(o.taskIsEarlierThan(this)){
            return 1;
        }else {
            return 0;
        }
    }
}

