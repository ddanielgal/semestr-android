package hu.danielgaldev.semestr;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hu.danielgaldev.semestr.adapter.RequirementAdapter;
import hu.danielgaldev.semestr.adapter.SubjectAdapter;
import hu.danielgaldev.semestr.fragments.dialog.NewReqTypeDialogFragment;
import hu.danielgaldev.semestr.fragments.dialog.NewRequirementDialogFragment;
import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.RequirementType;
import hu.danielgaldev.semestr.model.pojo.Subject;

public class RequirementsActivity extends AppCompatActivity
implements NewReqTypeDialogFragment.NewReqTypeDialogListener,
        NewRequirementDialogFragment.NewRequirementDialogListener,
        RequirementAdapter.RequirementClickListener {

    private Long semesterId;
    private Long subjectId;
    private String subjectName;
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
        subjectName = intent.getStringExtra("subjectName");

        database = SemestrDatabase.getInstance(getApplicationContext());

        initRecyclerView();
        initAddReqTypeButton();
        initAddReqButton();
    }

    private void initAddReqButton() {
        com.getbase.floatingactionbutton.FloatingActionButton fab = findViewById(R.id.NewRequirementButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewRequirementDialogFragment frag = new NewRequirementDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("subjectId", subjectId);
                frag.setArguments(bundle);
                frag.show(getSupportFragmentManager(), NewReqTypeDialogFragment.TAG);
            }
        });
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
        adapter = new RequirementAdapter(this);
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

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onRequirementCreated(final Requirement req) {
        new AsyncTask<Void, Void, Requirement>() {

            @Override
            protected Requirement doInBackground(Void... voids) {
                req.id = database.reqDao().insert(req);
                return req;
            }

            @Override
            protected void onPostExecute(Requirement requirement) {
                adapter.addItem(requirement);
                createNotificationEvent(requirement);
            }
        }.execute();
    }

    private void createNotificationEvent(Requirement req) {
        Calendar notificationDate = Calendar.getInstance();
        notificationDate.set(req.deadline.getYear(), req.deadline.getMonth(), req.deadline.getDay());
        notificationDate.add(Calendar.DAY_OF_MONTH, -3);

        Intent intent = new Intent(this, StudyNotificationService.class);
        intent.putExtra("requirementName", req.name);
        intent.putExtra("subjectName", subjectName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), pendingIntent);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onRequirementChanged(final Requirement requirement) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                database.reqDao().update(requirement);
                return true;
            }
        }.execute();
    }
}
