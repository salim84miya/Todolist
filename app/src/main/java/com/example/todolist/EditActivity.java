package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    EditText edtTitle,edtDescription;
    Button saveBtn,cancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();

        edtTitle = findViewById(R.id.edit_edit_title);
        edtDescription = findViewById(R.id.edit_edit_description);

        cancelBtn = findViewById(R.id.cancel_btn);
        saveBtn = findViewById(R.id.save_btn);

        linearLayout =findViewById(R.id.btn_holder);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                saveBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(linearLayout);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notes note = new Notes(edtTitle.getText().toString(),edtDescription.getText().toString());
                note.setId(intent.getIntExtra("id",1));

                if(new NotesHandler(EditActivity.this).updateNote(note)){
                    Toast.makeText(EditActivity.this,"Notes Updated",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditActivity.this,"Notes not updated",Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }
        });


        edtTitle.setText(intent.getStringExtra("title"));
        edtDescription.setText(intent.getStringExtra("description"));

    }
}