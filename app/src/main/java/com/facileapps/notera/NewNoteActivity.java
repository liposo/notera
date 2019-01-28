package com.facileapps.notera;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class NewNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE =
            "com.facileapps.notera.EXTRA_TITLE";
    public static final String EXTRA_NOTE =
            "com.facileapps.notera.EXTRA_NOTE";
    public static final String EXTRA_ID =
            "com.facileapps.notera.EXTRA_ID";

    private EditText editTextTitle;
    private EditText editTextNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextNote = findViewById(R.id.edit_text_note);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(NewNoteActivity.EXTRA_TITLE));
            editTextNote.setText(intent.getStringExtra(NewNoteActivity.EXTRA_NOTE));
        } else {
            setTitle("Add Note");
            editTextNote.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String note = editTextNote.getText().toString();

        if (note.trim().isEmpty()) {
            Toast.makeText(this, "Note is empty", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_NOTE, note);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
