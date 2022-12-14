package com.example.mobilproject2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilproject2.R;
import com.example.mobilproject2.database.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder>
{
    private List<Subject> subjects = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        onItemClickListener = listener;
    }

    public void setSubjects(List<Subject> subjects)
    {
        this.subjects = subjects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position)
    {
        if (!subjects.isEmpty())
            holder.setInfo(subjects.get(position));
    }

    @Override
    public int getItemCount()
    {
        return subjects.size();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameTextView, professorTextView, addressTextView, markTextView;

        public SubjectViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.subject_name_text_view);
            professorTextView = itemView.findViewById(R.id.subject_professor_text_view);
            addressTextView = itemView.findViewById(R.id.subject_address_text_view);
            markTextView = itemView.findViewById(R.id.subject_mark_text_view);
        }

        public void setInfo(Subject subject)
        {
            nameTextView.setText(String.format(itemView.getContext().getString(R.string.name_text), subject.getName()));
            professorTextView.setText(String.format(itemView.getContext().getString(R.string.professor_text), subject.getProfessor()));
            addressTextView.setText(String.format(itemView.getContext().getString(R.string.address_text), subject.getAddress()));
            markTextView.setText(String.format(itemView.getContext().getString(R.string.mark_text), subject.getMark()));
            itemView.setOnClickListener(view -> onItemClickListener.onClick(subject.getId()));
        }
    }

    public interface OnItemClickListener
    {
        void onClick(int studentId);
    }
}
