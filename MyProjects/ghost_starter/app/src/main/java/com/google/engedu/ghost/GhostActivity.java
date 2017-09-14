package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN ="Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView wordfragment;
    private TextView gamestatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        wordfragment=(TextView)findViewById(R.id.ghostText);
        gamestatus=(TextView)findViewById(R.id.gameStatus) ;
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int keyunicode=event.getUnicodeChar(event.getMetaState());
        char currentchar=(char)keyunicode;
        if(Character.isLetter(currentchar));
        String current=wordfragment.getText().toString()+currentchar;
        wordfragment.setText(current);
        userTurn=false;
        gamestatus.setText(COMPUTER_TURN);

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    public boolean ChallengeHandler(View view){
        //wordfragment is already a word then the user wins
        //2.word fragment isnot already aword and getanywordstartingwith=null then user wins again.
        //otherwise comp wins.
        return true;
    }
    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);

        // Do computer turn stuff then make it the user's turn again
        //check word fragment is valid
        //find any word starting with that fragment if none, challenge
        //add next character to fragment
        String current = "";
       // String comp;
        if (wordfragment.getText() != ""){
            current = wordfragment.getText().toString();
        String temp = null;
        String comp= dictionary.getAnyWordStartingWith(current);
        if (comp != null) {
            temp = comp.substring(0, 1);

        } else if (comp.length() > current.length())
            temp = current + comp.substring(current.length(), current.length() + 1);
        else
            temp = current;
    }
        String comp= dictionary.getAnyWordStartingWith(current);
        if(comp== null){
        gamestatus.setText("the comp wins,as no word can be formed from the letters");
return;
    }

        if(current.length()>=GhostDictionary.MIN_WORD_LENGTH&&dictionary.isWord(current))
            gamestatus.setText("Computer wins");

        userTurn = true;
        label.setText(USER_TURN);
    }
}
