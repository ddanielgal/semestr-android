package hu.danielgaldev.semestr.adapter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.Subject;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private final List<Subject> subjects;
    private HashMap<Subject, List<Requirement>> subToReqsMap;
    private SubjectClickListener listener;

    private SemestrDatabase db;

    public SubjectAdapter(SubjectClickListener listener) {
        subjects = new ArrayList<>();
        subToReqsMap = new HashMap<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_subject_list, viewGroup, false);

        db = SemestrDatabase.getInstance(viewGroup.getContext());

        return new SubjectViewHolder(itemView);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadRequirements(final Subject subject) {
        new AsyncTask<Void, Void, List<Requirement>>() {

            @Override
            protected List<Requirement> doInBackground(Void... voids) {
                return db.reqDao().getRequirementsForSubject(subject.id);
            }

            @Override
            protected void onPostExecute(List<Requirement> reqs) {
                subToReqsMap.put(subject, reqs);
            }
        }.execute();
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int i) {
        Subject sub = subjects.get(i);
        holder.subjectNameTextView.setText(sub.name);
        holder.creditsTextView.setText(
                new StringBuilder()
                        .append(sub.credits)
                        .append(" ")
                        .append(holder.itemView.getContext().getString(R.string.credits_text))
        );
        holder.sub = sub;
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

        TextView subjectNameTextView;
        TextView creditsTextView;

        Subject sub;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.SubjectNameTextView);
            creditsTextView = itemView.findViewById(R.id.CreditsTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClicked(sub);
        }
    }

    public void addItem(Subject subject) {
        subjects.add(subject);
        notifyItemInserted(subjects.size() - 1);
        loadRequirements(subject);
    }

    public void update(List<Subject> subject) {
        subjects.clear();
        subjects.addAll(subject);
        notifyDataSetChanged();
    }

    public interface SubjectClickListener {
        void onItemClicked(Subject subject);
    }
}
