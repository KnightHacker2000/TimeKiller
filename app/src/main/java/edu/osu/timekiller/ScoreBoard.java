/**
 *  Activity class for Top Players
 *      Fragments: List View Top Players
 *      Fragments: Map View Top Players
 */
package edu.osu.timekiller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ScoreBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
    }
}