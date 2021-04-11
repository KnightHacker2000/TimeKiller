package edu.osu.timekiller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle extras = getIntent().getExtras(); //All activities are started with an Intent!
        String value = extras.getString("post_id");

        Bundle bundle = new Bundle();
        bundle.putString("postId", value);
        Fragment info = new InfoFragment();
        info.setArguments(bundle);
        FragmentTransaction transaction =
                this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout, info);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}