package edu.osu.timekiller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mShowScroeBoard; // Button to show Top Players Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ScoreBoard Button Resource, Create an Intent to start ScoreBoard Activity
        mShowScroeBoard = findViewById(R.id.button_show_score_board);

        mShowScroeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.button_show_score_board){
                    Intent intent = new Intent(MainActivity.this, ScoreBoard.class);
                    startActivity(intent);
                }
            }
        });
    }
}