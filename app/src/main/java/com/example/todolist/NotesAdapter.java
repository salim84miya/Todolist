package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.temporal.Temporal;
import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Noteholder> {

    ArrayList<Notes> notes;
    Context context;
    ItemClick itemClick;
    ViewGroup parent;

    public  NotesAdapter (ArrayList<Notes> arrayList,Context context,ItemClick itemClick){
        notes = arrayList;
        this.context = context;
        this.itemClick = itemClick;

    }

    @NonNull
    @Override
    public Noteholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.notes_holder,parent,false);
        this.parent = parent;
        return new Noteholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Noteholder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
        holder.description.setText(notes.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }



    public class Noteholder extends RecyclerView.ViewHolder{

        ImageView edit;
        TextView title;
        TextView description;

        public Noteholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_txt);
            description = itemView.findViewById(R.id.description);
            edit = itemView.findViewById(R.id.edit_img);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.Onclick(getAdapterPosition(),itemView);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(description.getMaxLines()==1){
                        description.setMaxLines(Integer.MAX_VALUE);
                    }else{
                        description.setMaxLines(1);
                    }
                }
            });

        }
    }

    interface ItemClick{
        void Onclick(int id,View view);

    }

}
