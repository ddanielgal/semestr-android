package hu.danielgaldev.semestr;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import hu.danielgaldev.semestr.adapter.SemesterAdapter;
import hu.danielgaldev.semestr.adapter.SubjectAdapter;
import hu.danielgaldev.semestr.adapter.tools.RecyclerItemClickListener;
import hu.danielgaldev.semestr.fragments.dialog.NewSemesterDialogFragment;
import hu.danielgaldev.semestr.fragments.dialog.NewSubjectDialogFragment;
import hu.danielgaldev.semestr.model.DatabaseClient;
import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Semester;
import hu.danielgaldev.semestr.model.pojo.Subject;

public class SubjectActivity extends AppCompatActivity
implements NewSubjectDialogFragment.NewSubjectDialogListener {

    private Long semesterId;
    private RecyclerView recyclerView;
    private SemestrDatabase database;
    private SubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get userId from Intent
        Intent intent = getIntent();
        semesterId = Long.parseLong(intent.getStringExtra("semesterId"));

        database = DatabaseClient.getInstance(getApplicationContext()).getDb();

        initRecyclerView();
        initAddSubjectButton();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.SubjectRecyclerView);
        adapter = new SubjectAdapter();
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initAddSubjectButton() {
        FloatingActionButton fab = findViewById(R.id.AddSubjectButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewSubjectDialogFragment frag = new NewSubjectDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("semesterId", semesterId);
                frag.setArguments(bundle);
                frag.show(getSupportFragmentManager(), NewSubjectDialogFragment.TAG);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Subject>>() {

            @Override
            protected List<Subject> doInBackground(Void... voids) {
                return database.subjectDao().getSubjectsForSemester(semesterId);
            }

            @Override
            protected void onPostExecute(List<Subject> subs) {
                adapter.update(subs);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onSubjectCreated(final Subject newItem) {
        new AsyncTask<Void, Void, Subject>() {

            @Override
            protected Subject doInBackground(Void... voids) {
                newItem.id = database.subjectDao().insert(newItem);
                return newItem;
            }

            @Override
            protected void onPostExecute(Subject sub) {
                adapter.addItem(sub);
            }
        }.execute();
    }

}