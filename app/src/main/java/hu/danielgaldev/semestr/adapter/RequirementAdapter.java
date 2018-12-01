package hu.danielgaldev.semestr.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.model.DatabaseClient;
import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Requirement;

public class RequirementAdapter
        extends RecyclerView.Adapter<RequirementAdapter.RequirementViewHolder> {


    private final List<Requirement> items;
    private final SemestrDatabase db;

    public RequirementAdapter() {
        this.items = new ArrayList<>();
        this.db = DatabaseClient.getInstance(null).getDb();
    }

    @NonNull
    @Override
    public RequirementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_requirement_list, parent, false);

        return new RequirementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequirementViewHolder holder, int i) {
        Requirement req = items.get(i);
        holder.requirementNameTV.setText(req.name);
        holder.requirementDeadlineTV.setText(req.deadline.toString());
        holder.requirementTypeTV.setText(
                db.reqTypeDao().getById(req.requirementTypeId).name
        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class RequirementViewHolder extends RecyclerView.ViewHolder{

        TextView requirementNameTV;
        TextView requirementDeadlineTV;
        TextView requirementTypeTV;

        RequirementViewHolder(@NonNull View itemView) {
            super(itemView);
            requirementNameTV = itemView.findViewById(R.id.RequirementNameTextView);
            requirementDeadlineTV = itemView.findViewById(R.id.RequirementDeadlineTextView);
            requirementTypeTV = itemView.findViewById(R.id.RequirementTypeTextView);
        }
    }

    public void addItem(Requirement req) {
        items.add(req);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<Requirement> req) {
        items.clear();
        items.addAll(req);
        notifyDataSetChanged();
    }
}
