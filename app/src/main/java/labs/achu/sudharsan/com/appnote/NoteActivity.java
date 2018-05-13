package labs.achu.sudharsan.com.appnote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

import labs.achu.sudharsan.com.appnote.constants.PocketNotes;

public class NoteActivity extends AppCompatActivity {

    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        final EditText editText = findViewById(R.id.editText);
        Intent intent = getIntent();

        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1) {
            editText.setText(MainActivity.notes.get(noteId));
        } else {
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.adapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                MainActivity.notes.set(noteId, String.valueOf(s));
                MainActivity.adapter.notifyDataSetChanged();

                SharedPreferences preferences = getSharedPreferences(PocketNotes.PACKAGET_NAME, MODE_PRIVATE);

                final Set<String> notes = new HashSet<>(MainActivity.notes);
                preferences.edit().putStringSet("notes", notes).apply();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}
