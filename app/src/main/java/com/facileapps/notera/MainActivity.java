package com.facileapps.notera;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addNoteButton = findViewById(R.id.button_add_note);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);

            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter =  new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            Drawable background;
            Drawable deleteIcon;
            int deleteIconMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                deleteIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete);
                if (deleteIcon != null) {
                    deleteIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                }
                deleteIconMargin = (int) MainActivity.this.getResources().getDimension(R.dimen.ic_delete_margin);
                initiated = true;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (!initiated) {
                    init();
                }

                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = deleteIcon.getIntrinsicWidth();
                int intrinsicHeight = deleteIcon.getIntrinsicWidth();

                int deleteIconLeft = 0;
                int deleteIconRight = 0;
                int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int deleteIconBottom = deleteIconTop + intrinsicHeight;

                if (dX < 0) {
                    deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
                    deleteIconRight = itemView.getRight() - deleteIconMargin;
                } else {
                    deleteIconLeft = itemView.getLeft() + deleteIconMargin;
                    deleteIconRight = itemView.getLeft() + deleteIconMargin + intrinsicWidth;
                }

                deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                deleteIcon.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);

        noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                intent.putExtra(NewNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(NewNoteActivity.EXTRA_NOTE, note.getNoteText());
                intent.putExtra(NewNoteActivity.EXTRA_ID, note.getId());

                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(NewNoteActivity.EXTRA_TITLE);
            String note = data.getStringExtra(NewNoteActivity.EXTRA_NOTE);

            Note newNote = new Note(title, note, new Date());
            noteViewModel.insert(newNote);
        } else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id  =  data.getIntExtra(NewNoteActivity.EXTRA_ID, -1);
            String title = data.getStringExtra(NewNoteActivity.EXTRA_TITLE);
            String note = data.getStringExtra(NewNoteActivity.EXTRA_NOTE);

            if(id == -1) {
                Toast.makeText(this, "Note cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            Note newNote = new Note(title, note, new Date());
            newNote.setId(id);
            noteViewModel.update(newNote);
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
