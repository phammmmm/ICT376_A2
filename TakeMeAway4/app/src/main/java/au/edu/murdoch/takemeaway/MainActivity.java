package au.edu.murdoch.takemeaway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NoteListFragment noteListFragment;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            noteListFragment = NoteListFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.notes_list_fragment_container,noteListFragment).commit();
        }else{
            noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentById(R.id.notes_list_fragment_container);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
}
