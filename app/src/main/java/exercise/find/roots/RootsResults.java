package exercise.find.roots;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RootsResults extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_roots_results);

        TextView firstRoot = findViewById(R.id.FirstRoot);
        TextView secondRoot = findViewById(R.id.SecondRoot);
        TextView numberToCalculateRootsFor = findViewById(R.id.numberToCalculateRootsFor);
        TextView timeToCalculate = findViewById(R.id.TimePassed);

        Intent intent = getIntent();
        firstRoot.setText(String.valueOf(intent.getLongExtra("root1", 0)));
        secondRoot.setText(String.valueOf(intent.getLongExtra("root2", 0)));
        String originalNumberText = "The roots for the number " + String.valueOf(intent.getLongExtra("original_number", 0)) + ":";
        timeToCalculate.setText(String.valueOf(intent.getLongExtra("time_to_calculate", 0)));
        numberToCalculateRootsFor.setText(originalNumberText);


    }

}
