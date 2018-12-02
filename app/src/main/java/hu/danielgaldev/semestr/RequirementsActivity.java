package hu.danielgaldev.semestr;

import android.annotation.SuppressLint;
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

import hu.danielgaldev.semestr.adapter.RequirementAdapter;
import hu.danielgaldev.semestr.adapter.SubjectAdapter;
import hu.danielgaldev.semestr.fragments.dialog.NewReqTypeDialogFragment;
import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.RequirementType;
import hu.danielgaldev.semestr.model.pojo.Subject;

public class RequirementsActivity extends AppCompatActivity
implements NewReqTypeDialogFragment.NewReqTypeDialogListener {

    private Long semesterId;
    private Long subjectId;
    private RecyclerView recyclerView;
    private SemestrDatabase database;
    private RequirementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        semesterId = Long.parseLong(intent.getStringExtra("semesterId"));
        subjectId = Long.parseLong(intent.getStringExtra("subjectId"));

        database = SemestrDatabase.getInstance(getApplicationContext());

        initRecyclerView();
        initAddReqTypeButton();
    }

    private void initAddReqTypeButton() {
        com.getbase.floatingactionbutton.FloatingActionButton fab = findViewById(R.id.NewRequirementTypeButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewReqTypeDialogFragment().show(getSupportFragmentManager(), NewReqTypeDialogFragment.TAG);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.RequirementRecyclerView);
        adapter = new RequirementAdapter();
        loadItemsInBackground();
        loadRequirementTypes();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Requirement>>() {

            @Override
            protected List<Requirement> doInBackground(Void... voids) {
                return database.reqDao().getRequirementsForSubject(subjectId);
            }

            @Override
            protected void onPostExecute(List<Requirement> reqs) {
                adapter.update(reqs);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadRequirementTypes() {
        new AsyncTask<Void, Void, List<RequirementType>>() {

            @Override
            protected List<RequirementType> doInBackground(Void... voids) {
                return database.reqTypeDao().getAll();
            }

            protected void onPostExecute(List<RequirementType> reqTypes) {
                adapter.updateReqTypes(reqTypes);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onReqTypeCreated(final RequirementType reqType) {
        new AsyncTask<Void, Void, RequirementType>() {

            @Override
            protected RequirementType doInBackground(Void... voids) {
                reqType.id = database.reqTypeDao().insert(reqType);
                return reqType;
            }
        }.execute();
    }
}
