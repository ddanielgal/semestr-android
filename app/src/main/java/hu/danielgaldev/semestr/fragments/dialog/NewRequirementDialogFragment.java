package hu.danielgaldev.semestr.fragments.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.model.SemestrDatabase;
import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.RequirementType;

public class NewRequirementDialogFragment extends DialogFragment {

    public static final String TAG = "NewRequirementDialogFragment";
    private EditText nameEditText;
    private DatePicker deadlineDatePicker;
    private Spinner requirementTypeSpinner;

    private HashMap<String, Long> reqTypeMap;
    private SemestrDatabase database;
    private Long subjectId;

    public interface NewRequirementDialogListener {
        void onRequirementCreated(Requirement req);
    }

    private NewRequirementDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewRequirementDialogFragment.NewRequirementDialogListener) {
            listener = (NewRequirementDialogFragment.NewRequirementDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewRequirementDialogListener interface!");
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            subjectId = bundle.getLong("subjectId", 0);
        }
        database = SemestrDatabase.getInstance(requireContext());
    }

    @SuppressLint("StaticFieldLeak")
    private void loadReqTypes() {
        new AsyncTask<Void, Void, List<RequirementType>>() {

            @Override
            protected List<RequirementType> doInBackground(Void... voids) {
                return database.reqTypeDao().getAll();
            }

            protected void onPostExecute(List<RequirementType> reqTypes) {
                reqTypeMap = new HashMap<>();
                for (RequirementType r : reqTypes) {
                    reqTypeMap.put(r.name, r.id);
                }
                List<String> reqTypeList = new ArrayList<>(reqTypeMap.keySet());
                requirementTypeSpinner.setAdapter(new ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        reqTypeList));
            }
        }.execute();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_requirement, null);
        nameEditText = contentView.findViewById(R.id.RequirementNameEditText);
        //deadlineDatePicker = contentView.findViewById(R.id.DeadlineDatePicker);
        requirementTypeSpinner = contentView.findViewById(R.id.RequirementTypeSpinner);
        loadReqTypes();
        return contentView;
    }

    @NonNull
    private Requirement getRequirement() {
        return new Requirement(
                nameEditText.getText().toString(),
                new Date(deadlineDatePicker.getYear(), deadlineDatePicker.getMonth(), deadlineDatePicker.getDayOfMonth()),
                reqTypeMap.get(requirementTypeSpinner.getSelectedItem()),
                subjectId
        );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_requirement)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onRequirementCreated(getRequirement());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }


}
