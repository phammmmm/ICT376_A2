package au.edu.murdoch.takemeaway;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteListFragment extends Fragment {

    public final static int REQUEST_CODE_NEW_NOTE = 1;

    // Views
    private View mLayoutView;
    private FloatingActionButton createButton;
    private ListView noteListView;
    ImageView imageView;
    CustomAdapter arrayAdapter;

    // Database
    TakeMeAwayDBHelper mydb=null;
    ArrayList noteList;  // the list of all contacts


    public NoteListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment DonatorList.
     */
    public static NoteListFragment newInstance() {
        NoteListFragment fragment = new NoteListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        mLayoutView = inflater.inflate(R.layout.note_list_layout, container, false);
        //noteListView = (RecyclerView)mLayoutView.findViewById(R.id.note_list);
        return mLayoutView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageView = mLayoutView.findViewById(R.id.logo);
        //imageView.setVisibility(View.GONE);
        refresh();


        //At the click on an item, start a new activity that will start EditNoteActivity
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                TMANote note = (TMANote)noteList.get(position);
//                Toast.makeText(getContext(),note.getId()+note.getTitle(),Toast.LENGTH_LONG).show();
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", note.getId());
                Intent intent = new Intent(getActivity().getApplicationContext(), DisplayNoteActivity.class);
                intent.putExtras(dataBundle);
                startActivityForResult(intent, REQUEST_CODE_NEW_NOTE);
            }
        });
    }

    public void refresh(){
        mydb = new TakeMeAwayDBHelper(getActivity());

        noteList = mydb.getNoteList(TakeMeAwayDBHelper.ROW_COUNT_SMALL, TakeMeAwayDBHelper.DESC_ORDER);
        if(imageView!= null) {
            if(noteList.size()==0){
            imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
        ArrayList<String> array_list = new ArrayList<String>();

        for (int i = 0; i < noteList.size(); i++) {
            TMANote note = (TMANote) noteList.get(i);
            array_list.add(note.getDate()+" " +note.getTime()+ "     " + note.getTitle());
        }
        // Put all the contacts in an array
        arrayAdapter = new CustomAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, array_list);

        // Display the contacts in the ListView object
        noteListView = mLayoutView.findViewById(R.id.note_list);
        noteListView.setAdapter(arrayAdapter);

        createButton = getActivity().findViewById(R.id.create_new_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DisplayNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_btn:
                // Do Activity menu item stuff here
                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_LONG).show();
                SearchList(item);
                return true;
            default:
                break;
        }

        return false;
    }

    public void SearchList(MenuItem item){
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

}
