package com.facileapps.notera;

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
    private List<String> noteComments;
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

    public void setNoteComments(List<String> noteComments) {
        this.noteComments = noteComments;
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

    public List<String> getNoteComments() {
        return noteComments;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }
}
