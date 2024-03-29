package hu.danielgaldev.semestr.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.model.pojo.Semester;

public class SemesterAdapter
        extends RecyclerView.Adapter<SemesterAdapter.SemesterViewHolder> {

    private final List<Semester> items;

    private SemesterItemClickListener listener;

    public SemesterAdapter(SemesterItemClickListener listener) {
        this.listener = listener;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public SemesterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_semester_list, parent, false);

        return new SemesterViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SemesterViewHolder holder, int position) {
        Semester sem = items.get(position);
        String semNumberText = holder.itemView.getContext().getString(R.string.sem_number_text);
        holder.semesterNumberTextView.setText(sem.number + semNumberText);
        holder.universityTextView.setText(
                holder.itemView.getResources().getStringArray(R.array.university_items)
                        [Semester.University.toInt(sem.university)]
        );
        holder.degreeTextView.setText(
                holder.itemView.getResources().getStringArray(R.array.degree_items)
                        [Semester.Degree.toInt(sem.degree)]
        );

        holder.semester = sem;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Semester getItem(int position) {
        return items.get(position);
    }

    public interface SemesterItemClickListener{
        void onItemClicked(Semester semester);
    }

    class SemesterViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        TextView semesterNumberTextView;
        TextView universityTextView;
        TextView degreeTextView;

        Semester semester;

        SemesterViewHolder(View itemView) {
            super(itemView);
            semesterNumberTextView = itemView.findViewById(R.id.SemesterNumberTextView);
            universityTextView = itemView.findViewById(R.id.UniversityTextView);
            degreeTextView = itemView.findViewById(R.id.DegreeTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClicked(semester);
        }

    }

    public void addItem(Semester semester) {
        items.add(semester);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<Semester> semester) {
        items.clear();
        items.addAll(semester);
        notifyDataSetChanged();
    }

}