package au.edu.murdoch.takemeaway;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteDetailsFragment extends Fragment {
    private TakeMeAwayDBHelper mydb;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int GALLERY_REQUEST_CODE = 100;

    private CoordinatorLayout coordinatorLayout;
    private View mLayoutView;
    private TextView title, content, dateTextView, timeTextView, location;
    private ImageView imvBanner, imvGallery, imvCamera;
    private String dateFormat = "MMM dd, yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    final Calendar myCalendar = Calendar.getInstance();
    private int noteId = 0;
    PopupWindow puwPhotoChoice;
    LocationManager locationManager;
    LocationListener locationListener;

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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        String hourStr = hour < 10 ? "0" + hour : "" + hour;
        String minuteStr = minute < 10 ? "0" + minute : "" + minute;
        timeTextView.setText(hourStr + ":" + minuteStr);

        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mLayoutView == null)
            return;
        title = mLayoutView.findViewById(R.id.title);
        content = mLayoutView.findViewById(R.id.content);
        location = mLayoutView.findViewById(R.id.location);
        dateTextView = mLayoutView.findViewById(R.id.dateTime);
        timeTextView = mLayoutView.findViewById(R.id.textClock);
        mydb = new TakeMeAwayDBHelper(getActivity());


        mSaveButton = mLayoutView.findViewById(R.id.button_save);

        mChangePhoto = mLayoutView.findViewById(R.id.TakePhoto);
        imvBanner = mLayoutView.findViewById(R.id.newImage);

        //toolbar delete button
        //toolbar = mLayoutView.findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_delete_black_24dp);

        //location
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location myLocation) {
                if (myLocation != null) {
                    double lat = myLocation.getLatitude();
                    double lng = myLocation.getLongitude();
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        //decode lat and lng into addresses
                        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                        //create location name format with various location parts
                        String myLocationName = (addresses.get(0).getLocality() == null ? "" : addresses.get(0).getLocality() + ", ") +
                                (addresses.get(0).getAdminArea() == null ? "" : addresses.get(0).getAdminArea() + ", ") +
                                (addresses.get(0).getCountryName() == null ? "" : addresses.get(0).getCountryName());
                        //split up the final location string
                        String[] myLocNameSplit = myLocationName.split(",");
                        //because the string will definitely have a part, save the default part as variable in final locname
                        String myFinalLocName = myLocNameSplit[0].trim();

                        //loop through location parts to check for duplicates
                        for (String myLocNamePart : myLocNameSplit) {
                            //skip location part if duplicate
                            if (!myLocNameSplit[0].trim().equalsIgnoreCase(myLocNamePart.trim())) {
                                //add location part into final locname string if not a duplicate
                                myFinalLocName += ", " + myLocNamePart.trim();
                            }

                        }
                        location.setText(myFinalLocName);
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Error getting location!", Toast.LENGTH_LONG).show();
                        location.setText("UNKNOWN");
                    }
                }
            }

            public void onProviderDisabled(String provider) {
                Toast.makeText(getActivity(),"Error: Please enable location features!", Toast.LENGTH_LONG).show();
            }

            public void onProviderEnabled(String provider) { }

            public void onStatusChanged(String provider, int status, Bundle extras) { }
        };



        noteId = getShownIndex();
        if (noteId > 0) {
            //edit mode
            TMANote note = mydb.getNoteById(noteId);
            title.setText(note.getTitle());
            content.setText(note.getContent());
            dateTextView.setText(note.getDate());
            timeTextView.setText(note.getTime());
            location.setText(note.getLocation());
            if (note.getImage() != null) {
                imvBanner.setImageBitmap(note.getImage());
            }
        } else {
            //add mode
            //check if app has permission,
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                //if permission not available,


                //request for permissions
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                return;
            }
            else{
                //if permission available, update latest location
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
            }



        }

        //on click location, will prompt user if a location refresh is required
        location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Update Location");
                builder.setMessage("Do you want to update your location?");
                builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        //check if app has permission,
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        {
                            //if permission not available,


                            //request for permissions
                            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                            return;
                        }
                        else{
                            //if permission available, update latest location
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        mChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View myPopupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_photo_choice_layout, null, false);
                puwPhotoChoice = new PopupWindow(
                        myPopupView, view.getRootView().findViewById(R.id.myLinear).getWidth(),
                        view.getRootView().findViewById(R.id.myLinear).getHeight(),
                        true
                );
                puwPhotoChoice.showAtLocation(view.getRootView().findViewById(R.id.myLinear), Gravity.CENTER, 0, 0);
                View popContainer = (View) puwPhotoChoice.getContentView().getParent();
                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams p = (WindowManager.LayoutParams) popContainer.getLayoutParams();
                p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                p.dimAmount = 0.6f;
                wm.updateViewLayout(popContainer, p);

                imvCamera = myPopupView.findViewById(R.id.imvCamera);
                imvGallery = myPopupView.findViewById(R.id.imvGallery);

                imvCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkCameraHardware(getContext())) {
                            dispatchTakePictureIntent();
                        }
                        //close popup after setting image after 1s
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        puwPhotoChoice.dismiss();
                                    }
                                }, 1000
                        );
                    }
                });

                imvGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
                        //close popup after setting image after 1s
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        puwPhotoChoice.dismiss();
                                    }
                                }, 1000
                        );
                    }
                });
            }
        });

//TODO: saving location and image
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
                updateSelectedTime(hourOfDay, minute);
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
                new TimePickerDialog(getContext(), time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        };
        timeTextView.setOnClickListener(selectTime);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        // If request is cancelled the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //permission granted
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);

        } else {
            // permission denied
            Toast.makeText(getActivity(), "ERROR: Permission denied to location access", Toast.LENGTH_SHORT).show();
            location.setText("UNKNOWN");
        }
        return;
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
                    imvBanner.setImageURI(selectedImage);
                    break;

                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imvBanner.setImageBitmap(imageBitmap);
//                    imvBanner.setImageURI(Uri.parse(cameraFilePath));
                    break;
            }
    }

    public void onDeleteContactClick(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure you want to delete this note?")
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
        d.setTitle(R.string.deleteNote);
        d.show();

    }

    public void saveNote(){
        TMANote note = new TMANote();
        note.setTitle(title.getText().toString());
        note.setContent(content.getText().toString());
        note.setLocation(location.getText().toString());
        note.setDate(dateTextView.getText().toString());
        note.setTime(timeTextView.getText().toString());
        note.setImage(((BitmapDrawable)imvBanner.getDrawable()).getBitmap());
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

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}