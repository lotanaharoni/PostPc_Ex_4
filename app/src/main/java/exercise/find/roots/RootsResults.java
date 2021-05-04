package exercise.find.roots;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RootsResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_roots_results);

        TextView firstRoot = findViewById(R.id.FirstRoot);

        Intent intent = getIntent();

        firstRoot.setText(String.valueOf(intent.getLongExtra("root1", 0)));

    }

}
