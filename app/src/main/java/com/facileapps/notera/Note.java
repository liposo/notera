package com.facileapps.notera;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "note_table")
@TypeConverters(DateConverter.class)
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String noteText;
    private Date creationDate;

    public Note(String title, String noteText, Date creationDate) {
        this.title = title;
        this.noteText = noteText;
        this.creationDate = creationDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNoteText() {
        return noteText;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
