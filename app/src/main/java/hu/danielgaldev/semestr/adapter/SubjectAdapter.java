package hu.danielgaldev.semestr.adapter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.RequirementType;
import hu.danielgaldev.semestr.model.pojo.Subject;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private final List<Subject> subjects;
    private List<RequirementType> reqTypes;
    private HashMap<Subject, List<Requirement>> subToReqsMap;
    private SubjectClickListener listener;

    private SemestrDatabase db;

    public SubjectAdapter(SubjectClickListener listener) {
        subjects = new ArrayList<>();
        reqTypes = new ArrayList<>();
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
        loadReqTypes();

        return new SubjectViewHolder(itemView);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadReqTypes() {
        new AsyncTask<Void, Void, List<RequirementType>>() {

            @Override
            protected List<RequirementType> doInBackground(Void... voids) {
                return db.reqTypeDao().getAll();
            }

            @Override
            protected void onPostExecute(List<RequirementType> rts) {
                reqTypes.addAll(rts);
            }
        }.execute();
    }

    private RequirementType getReqTypeById(Long id) {
        for (RequirementType rt : reqTypes) {
            if (rt.id == id) {
                return rt;
            }
        }
        return null;
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
        int progress = calculateProgress(sub);
        holder.bar.setProgress(progress);
        holder.sub = sub;
    }

    private int calculateProgress(Subject sub) {
        List<Requirement> reqs = subToReqsMap.get(sub);
        if (reqs == null) return 0;
        double sum = 0.0;
        double completed = 0.0;
        for (Requirement r : reqs) {
            int reqWeight = getReqTypeById(r.requirementTypeId).weight;
            sum += reqWeight;
            if (r.completed) completed += reqWeight;
        }
        return (int) Math.round(completed/sum*100);
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

        TextView subjectNameTextView;
        TextView creditsTextView;
        ProgressBar bar;

        Subject sub;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.SubjectNameTextView);
            creditsTextView = itemView.findViewById(R.id.CreditsTextView);
            bar = itemView.findViewById(R.id.SubjectProgressBar);
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
