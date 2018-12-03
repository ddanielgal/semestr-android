package hu.danielgaldev.semestr.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.RequirementType;

public class RequirementAdapter
        extends RecyclerView.Adapter<RequirementAdapter.RequirementViewHolder> {

    private final List<Requirement> items;
    private final List<RequirementType> requirementTypes;
    private final SemestrDatabase db;

    private RequirementClickListener listener;

    public RequirementAdapter(RequirementClickListener listener) {
        this.listener = listener;
        this.items = new ArrayList<>();
        this.requirementTypes = new ArrayList<>();
        this.db = SemestrDatabase.getInstance(null);
    }

    public interface RequirementClickListener {
        void onRequirementChanged(Requirement requirement);
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
        Calendar cal = Calendar.getInstance();
        cal.set(req.deadline.getYear(), req.deadline.getMonth(), req.deadline.getDay());
        DateFormat f = new SimpleDateFormat("yyyy.MM.dd.", Locale.FRANCE);
        holder.requirementDeadlineTV.setText(f.format(cal.getTime()));
        RequirementType reqType = findReqTypeById(req.requirementTypeId);
        if (reqType == null) {
            holder.requirementTypeTV.setText(holder.itemView.getContext().getString(R.string.na));
        } else {
            holder.requirementTypeTV.setText(reqType.name);
        }
        holder.requirementDetailsTV.setText(req.details);
        holder.completedCheckBox.setChecked(req.completed);

        holder.requirement = req;
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
        CheckBox completedCheckBox;

        Requirement requirement;

        RequirementViewHolder(@NonNull View itemView) {
            super(itemView);
            requirementNameTV = itemView.findViewById(R.id.RequirementNameTextView);
            requirementDeadlineTV = itemView.findViewById(R.id.RequirementDeadlineTextView);
            requirementTypeTV = itemView.findViewById(R.id.RequirementTypeTextView);
            requirementDetailsTV = itemView.findViewById(R.id.RequirementDetailsTextView);
            completedCheckBox = itemView.findViewById(R.id.RequirementCompletedCheckBox);
            completedCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (requirement != null) {
                        requirement.completed = isChecked;
                        listener.onRequirementChanged(requirement);
                    }
                }
            });
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
