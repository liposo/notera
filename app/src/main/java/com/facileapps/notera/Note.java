package com.facileapps.notera;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String noteText;
    private String noteComment;
    private Date creationDate;
    private Date updateDate;

    public Note(String title, String noteText, Date creationDate) {
        this.title = title;
        this.noteText = noteText;
        this.creationDate = creationDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addNoteComment(String noteComment) {
        this.noteComment = noteComment;
}

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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

    public String getNoteComment() {
        return noteComment;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }
}
