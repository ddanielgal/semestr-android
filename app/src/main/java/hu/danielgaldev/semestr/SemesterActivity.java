package hu.danielgaldev.semestr;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Semester;
import hu.danielgaldev.semestr.fragments.dialog.NewSemesterDialogFragment;
import hu.danielgaldev.semestr.adapter.SemesterAdapter;


public class SemesterActivity extends AppCompatActivity
        implements NewSemesterDialogFragment.NewSemesterDialogListener,
        SemesterAdapter.SemesterItemClickListener {

    private SemesterAdapter adapter;
    private RecyclerView recyclerView;
    private SemestrDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_semester);

        database = Room.databaseBuilder(
                getApplicationContext(),
                SemestrDatabase.class,
                "semester-list"
        ).build();

        initRecyclerView();
        initAddSemesterButton();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new SemesterAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initAddSemesterButton() {
        FloatingActionButton fab = findViewById(R.id.AddSemesterButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewSemesterDialogFragment().show(getSupportFragmentManager(), NewSemesterDialogFragment.TAG);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onSemesterCreated(final Semester newItem) {
        new AsyncTask<Void, Void, Semester>() {

            @Override
            protected Semester doInBackground(Void... voids) {
                newItem.id = database.semesterDao().insert(newItem);
                return newItem;
            }

            @Override
            protected void onPostExecute(Semester semester) {
                adapter.addItem(semester);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Semester>>() {

            @Override
            protected List<Semester> doInBackground(Void... voids) {
                return database.semesterDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Semester> semesters) {
                adapter.update(semesters);
            }
        }.execute();
    }

    @Override
    public void onItemChanged(Semester item) {

    }
}
