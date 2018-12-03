package hu.danielgaldev.semestr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hu.danielgaldev.semestr.adapter.SubjectAdapter;
import hu.danielgaldev.semestr.adapter.tools.SubjectCollection;
import hu.danielgaldev.semestr.fragments.dialog.NewSubjectDialogFragment;
import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.RequirementType;
import hu.danielgaldev.semestr.model.pojo.Subject;

public class SubjectActivity extends AppCompatActivity
implements NewSubjectDialogFragment.NewSubjectDialogListener,
           SubjectAdapter.SubjectClickListener {

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

        database = SemestrDatabase.getInstance(getApplicationContext());

        initRecyclerView();
        initAddSubjectButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItemsInBackground();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.SubjectRecyclerView);
        adapter = new SubjectAdapter(this);
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
        new AsyncTask<Void, Void, SubjectCollection>() {

            @Override
            protected SubjectCollection doInBackground(Void... voids) {
                HashMap<Subject, List<Requirement>> map = new HashMap<>();
                List<Subject> subs = new ArrayList<>(database.subjectDao().getSubjectsForSemester(semesterId));
                for (Subject s : subs) {
                    map.put(s, database.reqDao().getRequirementsForSubject(s.id));
                }
                List<RequirementType> reqTypeList = database.reqTypeDao().getAll();
                return new SubjectCollection(map, reqTypeList);
            }

            @Override
            protected void onPostExecute(SubjectCollection collection) {
                adapter.update(new ArrayList<>(collection.getSubReqMap().keySet()));
                adapter.setMap(collection.getSubReqMap());
                adapter.setReqTypeList(collection.getReqTypeList());
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

    @Override
    public void onItemClicked(Subject subject) {
        Intent myIntent = new Intent(SubjectActivity.this, RequirementsActivity.class);
        myIntent.putExtra("subjectId", subject.id.toString());
        myIntent.putExtra("semesterId", semesterId.toString());
        myIntent.putExtra("subjectName", subject.name);
        SubjectActivity.this.startActivity(myIntent);
    }
}
