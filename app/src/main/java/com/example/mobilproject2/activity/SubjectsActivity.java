package com.example.mobilproject2.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilproject2.R;
import com.example.mobilproject2.adapter.SubjectsAdapter;
import com.example.mobilproject2.database.DatabaseManager;
import com.example.mobilproject2.database.model.Subject;

import java.util.List;

public class SubjectsActivity extends AppCompatActivity
{
    private EditText nameEditText;
    private EditText professorEditText;
    private EditText addressEditText;
    private EditText markEditText;
    private Button addButton;
    private RecyclerView recyclerView;
    private CheckBox selectionCheckBox;
    private TextView selectionValueTextView;
    private DatabaseManager databaseManager;
    private SubjectsAdapter subjectsAdapter = new SubjectsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        this.databaseManager = new DatabaseManager(this);
        this.nameEditText = findViewById(R.id.subject_name_edit_text);
        this.professorEditText = findViewById(R.id.subject_professor_edit_text);
        this.addressEditText = findViewById(R.id.subject_address_edit_text);
        this.markEditText = findViewById(R.id.subject_mark_edit_text);
        this.addButton = findViewById(R.id.subject_add_button);
        this.recyclerView = findViewById(R.id.subject_recycler_view);
        this.selectionCheckBox = findViewById(R.id.subject_selection_check_box);
        this.selectionValueTextView = findViewById(R.id.subject_selection_value_text_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());

        this.selectionCheckBox.setText(String.format(getString(R.string.database_selection_text), DatabaseManager.SELECTION_PROFESSOR, DatabaseManager.SELECTION_MINIMUM_MARK));
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.addItemDecoration(itemDecoration);
        this.recyclerView.setAdapter(this.subjectsAdapter);
        this.subjectsAdapter.setOnItemClickListener(id -> new AlertDialog.Builder(this)
                .setMessage(getString(R.string.database_delete_text))
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> new Thread(() ->
                {
                    this.databaseManager.deleteSubject(id);
                    updateList();
                }).start()).show());

        this.addButton.setOnClickListener(view ->
        {
            String nameString = this.nameEditText.getText().toString();
            String professorString = this.professorEditText.getText().toString();
            String addressString = this.addressEditText.getText().toString();
            String markString = this.markEditText.getText().toString();

            if (nameString.isEmpty())
            {
                this.nameEditText.setError(getString(R.string.empty_row_error));
                return;
            }

            if (professorString.isEmpty())
            {
                this.professorEditText.setError(getString(R.string.empty_row_error));
                return;
            }

            if (addressString.isEmpty())
            {
                this.addressEditText.setError(getString(R.string.empty_row_error));
                return;
            }

            if (markString.isEmpty())
            {
                this.addressEditText.setError(getString(R.string.empty_row_error));
                return;
            }

            int mark;

            try
            {
                mark = Integer.parseInt(markString);
            }
            catch (NumberFormatException e)
            {
                this.nameEditText.setError(getString(R.string.mark_error));
                return;
            }

            if (mark < 0 || mark > 100)
            {
                this.markEditText.setError(getString(R.string.mark_error));
                return;
            }

            new Thread(() ->
            {
                this.databaseManager.insertSubject(new Subject(nameString, professorString, addressString, mark));
                updateList();
                this.nameEditText.setText("");
                this.professorEditText.setText("");
                this.addressEditText.setText("");
                this.markEditText.setText("");
            }).start();
        });

        this.selectionCheckBox.setOnCheckedChangeListener((compoundButton, b) -> new Thread(() -> updateList()).start());

        new Thread(() -> updateList()).start();
    }

    private void updateList()
    {
        List<Subject> subjects;

        if (this.selectionCheckBox.isChecked())
            subjects = this.databaseManager.getSelectionSubject();
        else
            subjects = this.databaseManager.getAllSubjects();

        runOnUiThread(() ->
        {
            this.subjectsAdapter.setSubjects(subjects);
            setSelectionValue(this.databaseManager.getSelectionSubjectValue());
        });
    }

    private void setSelectionValue(int selectionValue)
    {
        this.selectionValueTextView.setText(String.format(getString(R.string.database_selection_value_text), selectionValue));
    }
}