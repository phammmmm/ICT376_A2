package au.edu.murdoch.takemeaway;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.TimeZoneFormat;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteDetailsFragment extends Fragment {
    private TakeMeAwayDBHelper mydb ;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private String cameraFilePath;

    private CoordinatorLayout coordinatorLayout;
    private View mLayoutView;
    private TextView title ;
    private TextView content;
    //TextView dateTime;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView location;
    private ImageView imageView;
    private String dateFormat = "MMM dd, yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    final Calendar myCalendar = Calendar.getInstance();
    private TimePickerDialog timePickerDialog;
    Toolbar toolbar;
    private int noteId = 0;

    // the buttons
    FloatingActionButton mSaveButton;
    FloatingActionButton mChangePhoto;


    public NoteDetailsFragment() {
        // Required empty public constructor
    }
    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'id'.
     */
    public static NoteDetailsFragment newInstance(int id) {

        NoteDetailsFragment f = new NoteDetailsFragment();

        // Supply index input as an argument.
        // Google recommends using bundles to pass in arguments
        Bundle args = new Bundle();
        args.putInt("id", id);
        f.setArguments(args);

        return f;
    }

    // Retrieve the index of the note that will be displayed
    public int getShownIndex() {
        return getArguments().getInt("id", 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                // Do Activity menu item stuff here
                onDeleteContactClick();
                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayoutView = inflater.inflate(R.layout.note_details_layout, null);
        coordinatorLayout = mLayoutView.findViewById(R.id.note_details_layout);


        //Date picker
        dateTextView = mLayoutView.findViewById(R.id.dateTime);
        String currentDateTimeString = sdf.format(new Date());
        dateTextView.setText(currentDateTimeString);

        //Time picker
        timeTextView = mLayoutView.findViewById(R.id.textClock);
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        String hourStr = hour<10? "0"+hour : ""+hour;
        String minuteStr = minute<10? "0"+minute : ""+minute;
        timeTextView.setText(hourStr+":"+minuteStr);

        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mLayoutView == null)
            return;
        title   = mLayoutView.findViewById(R.id.title);
        content  = mLayoutView.findViewById(R.id.content);
        location = mLayoutView.findViewById(R.id.location);
        dateTextView = mLayoutView.findViewById(R.id.dateTime);
        timeTextView = mLayoutView.findViewById(R.id.textClock);
        mydb = new TakeMeAwayDBHelper(getActivity());


        mSaveButton =  mLayoutView.findViewById(R.id.button_save);

        mChangePhoto = mLayoutView.findViewById(R.id.TakePhoto);
        imageView = mLayoutView.findViewById(R.id.newImage);

        //toolbar delete button
        //toolbar = mLayoutView.findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_delete_black_24dp);

        noteId = getShownIndex();
        if(noteId>0){
            TMANote note = mydb.getNoteById(noteId);
            title.setText(note.getTitle());
            content.setText(note.getContent());
            dateTextView.setText(note.getDate());
            timeTextView.setText(note.getTime());
            location.setText(note.getLocation());
            if(note.getImage()!=null){
                imageView.setImageBitmap(note.getImage());
            }
        }

        mChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
            }
        });


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                updateSelectedDate(year, month, dayOfMonth);
            }
        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                updateSelectedTime(hourOfDay,minute);
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


        //timeTextView onClick Logic to open Timepicker
        View.OnClickListener selectTime = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(),time,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
            }
        };
        timeTextView.setOnClickListener(selectTime);
    }

    private void updateSelectedTime(int hour, int minute) {
        String hourStr = hour<10? "0"+hour : ""+hour;
        String minuteStr = minute<10? "0"+minute : ""+minute;
        timeTextView.setText(hourStr + ":" + minuteStr);
    }

    private void updateSelectedDate(int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //set selected date back to textview
        dateTextView.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    imageView.setImageURI(selectedImage);
                    break;

                case CAMERA_REQUEST_CODE:
                    imageView.setImageURI(Uri.parse(cameraFilePath));
                    break;
            }
    }


    public void onDeleteContactClick(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.deleteNote)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mydb.FlagDeleteRow(getShownIndex());
                        Toast.makeText(getActivity().getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                        startActivity(intent);  // back to main activity
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog d = builder.create();
        d.setTitle("Are you sure?");
        d.show();

    }


    public void saveNote(){
        TMANote note = new TMANote();
        note.setTitle(title.getText().toString());
        note.setContent(content.getText().toString());
        note.setLocation(location.getText().toString());
        note.setDate(dateTextView.getText().toString());
        note.setTime(timeTextView.getText().toString());
        note.setImage(((BitmapDrawable)imageView.getDrawable()).getBitmap());
        if(noteId>0){
            int result = mydb.updateNote(noteId,note);
            if(result>0){
                Toast.makeText(getActivity().getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Failed to Update"+result, Toast.LENGTH_SHORT).show();
            }
        }
        else{
            // inserting a new note
            long newNoteId = mydb.insertNote(note);
            if(newNoteId>0){
                Toast.makeText(getActivity().getApplicationContext(), "Note inserted!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "Note NOT inserted. ", Toast.LENGTH_SHORT).show();
            }
        }

        Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
        startActivity(intent);

        // Refresh the fragment in which the list of data is re-displayed
        NoteListFragment noteListFragment = (NoteListFragment) getFragmentManager().findFragmentById(R.id.notes_list_fragment_container);
        if (noteListFragment!=null){
            noteListFragment.refresh();
        }

    }

}