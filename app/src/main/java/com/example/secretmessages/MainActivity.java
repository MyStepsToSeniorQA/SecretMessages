package com.example.secretmessages;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText txtIn;
    EditText txtKey;
    EditText txtOut;
    SeekBar sb;
    Button btn;
    Button btnMove;




    public String encode(String message, int keyVal) {
        String output = "";
        char key = (char) keyVal;
        for(int x = 0; x < message.length(); x++){
            char input = message.charAt(x);
            if(input >= 'A' && input <= 'Z') {
                input += key;
                if(input > 'Z')
                    input -= 26;
                if(input < 'A')
                    input += 26;

            }else if(input >= 'a' && input <= 'z') {
                input += key;
                if(input > 'z')
                    input -= 26;
                if(input < 'a')
                    input += 26;

            }else if(input >= '0' && input <= '9') {
                input += (keyVal % 10);
                if (input > '9')
                    input -= 10;
                if (input < '0')
                    input += 10;
            }
            output += input;
        }

        return output;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtIn = (EditText) findViewById(R.id.txtIn);
        txtOut = (EditText) findViewById(R.id.txtOut);
        txtKey = (EditText) findViewById(R.id.txtKey);
        sb = (SeekBar) findViewById(R.id.seekBar);
        btn = (Button) findViewById(R.id.btnEncodeDecode);
        btnMove = (Button) findViewById(R.id.btnMoveUp);
        Intent receivedInternet = getIntent();
        String receivedText = receivedInternet.getStringExtra(Intent.EXTRA_TEXT);
        if (receivedText != null)
            txtIn.setText(receivedText);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int key = Integer.parseInt(((txtKey).getText().toString()));
                String message = txtIn.getText().toString();
                String output = encode(message, key);
                txtOut.setText(output);


            }
        });
        
        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = txtOut.getText().toString();
                txtIn.setText(message);
                int key = Integer.parseInt((txtKey.getText().toString()));
                /*if( key >= 0) {
                    key = key - 13;
                    sb.setProgress(key);
                }else if (key <= 0){
                    key = key + 13;
                    sb.setProgress(key);

                } */
                String output = encode(message, key);
                txtOut.setText(output);


            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int key = sb.getProgress() - 13;
                String message = txtIn.getText().toString();
                String output  = encode(message, key);
                txtOut.setText(output);
                txtKey.setText("" + key);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Secret Message " +
                        DateFormat.getDateTimeInstance().format(new Date()));
                shareIntent.putExtra(Intent.EXTRA_TEXT, txtOut.getText().toString());
                try {
                    startActivity(Intent.createChooser(shareIntent, "Share message..."));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "Error: Couldn't share." ,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}