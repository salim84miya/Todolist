 package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import javax.security.auth.Destroyable;

 public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ArrayList<Notes> notes;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.add_btn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater =(LayoutInflater) (MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                View viewInput = inflater.inflate(R.layout.input_dialog,null,false);

                EditText edtTitle = viewInput.findViewById(R.id.edit_title);
                EditText edtDescription = viewInput.findViewById(R.id.edit_description);

                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewInput)
                        .setTitle("Add Note")
                        .setPositiveButton("Add",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = edtTitle.getText().toString();
                                String description = edtDescription.getText().toString();

                                Notes notes = new Notes(title, description);

                                boolean isCreated = new NotesHandler(MainActivity.this).create(notes);
                                if(isCreated){
                                    Toast.makeText(MainActivity.this,"note created",Toast.LENGTH_SHORT).show();
                                    loadNotes();
                                }else {
                                    Toast.makeText(MainActivity.this,"Note can't be created",Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }
                        }).show();

            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        ItemTouchHelper.SimpleCallback itemCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                new NotesHandler(MainActivity.this).deleteNote(notes.get(viewHolder.getAdapterPosition()).getId());
                notes.remove(viewHolder.getAdapterPosition());
                notesAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        loadNotes();
    }

    public ArrayList<Notes> readNotes(){
        ArrayList<Notes> notes = new NotesHandler(MainActivity.this ).readNotes();
        return notes;
    }

    public void loadNotes(){
        notes = readNotes();
        notesAdapter = new NotesAdapter(notes, this, new NotesAdapter.ItemClick() {
            @Override
            public void Onclick(int id, View view) {
                edit(notes.get(id).getId(),view);
            }
        });
        recyclerView.setAdapter(notesAdapter);

    }



     private void edit(int id, View view){
        NotesHandler notesHandler = new NotesHandler(this);
         Notes note = notesHandler.readSingleNote(id);

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id",note.getId());
        intent.putExtra("title",note.getTitle());
        intent.putExtra("description",note.getDescription());

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,view, ViewCompat.getTransitionName(view));
        startActivityForResult(intent,1,optionsCompat.toBundle());


    }
     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == 1){
             loadNotes();
         }
     }
}