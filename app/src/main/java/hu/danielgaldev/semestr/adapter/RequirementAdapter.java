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
import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.RequirementType;

public class RequirementAdapter
        extends RecyclerView.Adapter<RequirementAdapter.RequirementViewHolder> {


    private final List<Requirement> items;
    private final List<RequirementType> requirementTypes;
    private final SemestrDatabase db;

    public RequirementAdapter() {
        this.items = new ArrayList<>();
        this.requirementTypes = new ArrayList<>();
        this.db = SemestrDatabase.getInstance(null);
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
        RequirementType reqType = findReqTypeById(req.requirementTypeId);
        if (reqType == null) {
            holder.requirementTypeTV.setText(holder.itemView.getContext().getString(R.string.na));
        } else {
            holder.requirementTypeTV.setText(reqType.name);
        }
        holder.requirementDetailsTV.setText(req.details);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class RequirementViewHolder extends RecyclerView.ViewHolder{

        TextView requirementNameTV;
        TextView requirementDeadlineTV;
        TextView requirementTypeTV;
        TextView requirementDetailsTV;

        RequirementViewHolder(@NonNull View itemView) {
            super(itemView);
            requirementNameTV = itemView.findViewById(R.id.RequirementNameTextView);
            requirementDeadlineTV = itemView.findViewById(R.id.RequirementDeadlineTextView);
            requirementTypeTV = itemView.findViewById(R.id.RequirementTypeTextView);
            requirementDetailsTV = itemView.findViewById(R.id.RequirementDetailsTextView);
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

    public void updateReqTypes(List<RequirementType> reqTypes) {
        requirementTypes.clear();
        requirementTypes.addAll(reqTypes);
        notifyDataSetChanged();
    }

    private RequirementType findReqTypeById(Long id) {
        for (RequirementType rt : requirementTypes) {
            if (rt.id == id) return rt;
        }
        return null;
    }
}
