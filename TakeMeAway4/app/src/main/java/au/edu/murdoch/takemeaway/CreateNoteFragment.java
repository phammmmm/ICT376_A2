package au.edu.murdoch.takemeaway;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class CreateNoteFragment extends Fragment {

    private View rootView;
    private TextView dateTextView;
    private EditText etTitle, etContent;
    private FloatingActionButton fabDone, fabPhoto;
    //
    private String dateFormat = "MMM dd, yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    TakeMeAwayDBHelper myDB;
    //
    final Calendar myCalendar = Calendar.getInstance();


    public static CreateNoteFragment newInstance(){
        Bundle args = new Bundle();
        CreateNoteFragment fragment = new CreateNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @SuppressLint("ValidFragment")
    private CreateNoteFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        //set reference to the initialized db in MainActivity
        myDB = ((MainActivity)getActivity()).myDB;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_note, container, false);
        dateTextView = rootView.findViewById(R.id.dateEditText);
        String currentDateTimeString = sdf.format(new Date());
        dateTextView.setText(currentDateTimeString);
        //
        //FloatingActionButton
        fabDone = rootView.findViewById(R.id.doneButton);
        //Edit Text
        etTitle = rootView.findViewById(R.id.title);
        etContent = rootView.findViewById(R.id.content);



        initials(rootView);
        onClickListeners();
        return rootView;
    }



    private void initials(View rootView){

    }

    private void onClickListeners(){

        //SavePost FloatingActionButton onClick Logic
        View.OnClickListener savePost = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO: touch up flow
                try{
//                    if(!myDB.tmaDB.isOpen()){
//                        //open connection
//                        myDB.open();
//                    }


                    myDB.InsertRow(
                            etTitle.getText().toString(), etContent.getText().toString(),null,
                            null, null, null,
                            null, null
                    );

                    //commit transaction
                    myDB.commit();
                }
                catch (Exception e){
                    //TODO: create a more robust catch mechanism
                    Log.i("Database Error - fragment: ", e.getMessage());
                }
                finally {
                    //close connection
//                    myDB.close();
                    ((MainActivity)getActivity()).onBackPressed();

                }


            }
        };
        fabDone.setOnClickListener(savePost);
        //
        //Datepicker
        //DatePicker OnDateSet Logic
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                updateSelectedDate(year, month, dayOfMonth);
            }
        };

        //dateTextView onClick Logic to open Datepicker
        View.OnClickListener selectDate = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        };
        dateTextView.setOnClickListener(selectDate);
    }

    private void updateSelectedDate(int year, int month, int dayOfMonth) {

        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //set selected date back to textview
        dateTextView.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }

    public interface LayoutFactory {
        View produceLayout(LayoutInflater inflater, @Nullable ViewGroup container);
    }
    public final class IdLayoutFactory implements LayoutFactory {
        private final int layoutId;
        public IdLayoutFactory(int layoutId) {
            this.layoutId = layoutId;
        }
        @Override
        public View produceLayout(LayoutInflater inflater, @Nullable ViewGroup container) {
            return inflater.inflate(layoutId, container, false);
        }
    }
    public final class DummyLayoutFactory implements LayoutFactory {
        private final View view;
        public DummyLayoutFactory(View view) {
            this.view = view;
        }
        @Override
        public View produceLayout(LayoutInflater inflater, @Nullable ViewGroup container) {
            return view;
        }
    }

}
