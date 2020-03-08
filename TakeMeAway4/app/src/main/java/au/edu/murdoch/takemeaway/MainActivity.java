package au.edu.murdoch.takemeaway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    @TargetApi(Build.VERSION_CODES.O)

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private ListView lvPostList;
    //
    public TakeMeAwayDBHelper myDB = new TakeMeAwayDBHelper(this);
    //
    private List<String> tempArrayList = new ArrayList<String>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //open connection
            myDB.open();
        }
        catch(Exception e){
            //TODO: create a more robust catch mechanism
            Log.i("Database Error: ", e.getMessage());
        }
        setContentView(R.layout.activity_main);
        initials();
        clickListeners();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        String msg ="";
        switch (item.getItemId()){
            case R.id.delete:
                msg = "Delete";
                break;
            case R.id.search_bar:
                msg = "Search";
                break;
            case R.id.edit_query:
                msg = "Edit";
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void initials() {
        coordinatorLayout=findViewById(R.id.mainLayout);
        fab = findViewById(R.id.fab);
        lvPostList = findViewById(R.id.list_item);
        //rvPostList.setLayoutManager(new LinearLayoutManager(this))
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //override default onBackPressed method
    //onBackPressed = method containing logic when back button is clicked
    @Override
    public void onBackPressed() {
        //remove the fragment from fragment container
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit();
        fab.show();
        updatePostListView();
    }

    private void updatePostListView(){
        //Database Loading
        try{
            Cursor myCursor = myDB.QueryRowsNotFlagDelete(-1, "ASC");
            if(myCursor != null && myCursor.getCount() > 0){
                try{
                    //reset list view first
                    tempArrayList.clear();
                    //clear existing content in list view (set listview to empty)
                    if(arrayAdapter != null){
                        arrayAdapter.clear();
                        arrayAdapter.notifyDataSetChanged();
                    }

                    while(myCursor.moveToNext()){
                        tempArrayList.add(myCursor.getString(myCursor.getColumnIndex(myDB.COL_PostTitle)));
                    }
                    //if there was record returned from DB
                    if(!tempArrayList.isEmpty()){
                        //pass value to adapter
                        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tempArrayList);
                        //set listview to latest adapter
                        lvPostList.setAdapter(arrayAdapter);
                    }
                }
                catch(Exception e){
                    //TODO: create a more robust catch mechanism
                    Log.i("Database - Cursor Error: ", e.getMessage());
                }
                finally {
                    myCursor.close();
                }
            }

            //commit transaction
            //myDB.commit();
        }
        catch (Exception e){
            //TODO: create a more robust catch mechanism
            Log.i("Database Error: ", e.getMessage());
        }
//        finally {
//            //close connection
//            myDB.close();
//        }
    }

    private void clickListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                CreateNoteFragment fragment =  CreateNoteFragment.newInstance();
                //Toast.makeText(getApplicationContext(),"TEST"+fragment,Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().addToBackStack(null);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                p.setAnchorId(View.NO_ID);
                fab.setLayoutParams(p);
                fab.setVisibility(View.GONE);
            }
        });
    }

}
