package hu.danielgaldev.semestr.fragments.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.model.pojo.Semester;

public class NewSemesterDialogFragment extends DialogFragment {

    public static final String TAG = "NewSemesterDialogFragment";
    private NumberPicker numberPicker;
    private Spinner universitySpinner;
    private Spinner degreeSpinner;

    public interface NewSemesterDialogListener {
        void onSemesterCreated(Semester newItem);
    }

    private NewSemesterDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewSemesterDialogListener) {
            listener = (NewSemesterDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!");
        }
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_semester, null);
        numberPicker = contentView.findViewById(R.id.SemesterNumberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        universitySpinner = contentView.findViewById(R.id.UniversitySpinner);
        universitySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.university_items)));
        universitySpinner.setSelection(0);
        degreeSpinner = contentView.findViewById(R.id.DegreeSpinner);
        degreeSpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.degree_items)));
        degreeSpinner.setSelection(0);
        return contentView;
    }

    private Semester getSemester() {
        Semester semester = new Semester();
        semester.number = numberPicker.getValue();
        semester.university = Semester.University.getByOrdinal(universitySpinner.getSelectedItemPosition());
        semester.degree = Semester.Degree.getByOrdinal(degreeSpinner.getSelectedItemPosition());
        return semester;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_semester)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onSemesterCreated(getSemester());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
}
