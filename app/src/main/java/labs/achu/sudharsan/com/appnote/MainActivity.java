package labs.achu.sudharsan.com.appnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import labs.achu.sudharsan.com.appnote.constants.PocketNotes;

public class MainActivity extends AppCompatActivity {

    public static List<String> notes = new ArrayList<>();

    public static ArrayAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_note_id) {
            final Intent intent = new Intent(getApplicationContext(), NoteActivity.class);

            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = findViewById(R.id.list_view);

        // Fetch the values from shared preferences.
        final SharedPreferences preferences = getApplicationContext().getSharedPreferences(PocketNotes.PACKAGET_NAME, MODE_PRIVATE);
        final Set<String> noteSet = preferences.getStringSet(PocketNotes.NOTES, null);

        if (noteSet == null) {

            this.notes.add("Sample note.");
        } else {
            this.notes = new ArrayList<>(noteSet);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(adapter);

        // Start a new activity.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra("noteId", position);

                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);

                                preferences.edit().putStringSet(PocketNotes.NOTES, new HashSet<String>(notes)).apply();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
}
