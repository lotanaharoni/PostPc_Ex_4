package exercise.find.roots;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private BroadcastReceiver broadcastReceiverForSuccess = null;
  private BroadcastReceiver broadcastReceiverForFail = null;
  private boolean isWaitingForCalculation;
  private String numberToCalculate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ProgressBar progressBar = findViewById(R.id.progressBar);
    EditText editTextUserInput = findViewById(R.id.editTextInputNumber);
    Button buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);

    // set initial UI:
    progressBar.setVisibility(View.GONE); // hide progress
    editTextUserInput.setText(""); // cleanup text in edit-text
    editTextUserInput.setEnabled(true); // set edit-text as enabled (user can input text)
    buttonCalculateRoots.setEnabled(false); // set button as disabled (user can't click)
    numberToCalculate = "";
    isWaitingForCalculation = false;

    // set listener on the input written by the keyboard to the edit-text
    editTextUserInput.addTextChangedListener(new TextWatcher() {
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      public void afterTextChanged(Editable s) {
        long userInputLong = 0;
        String userInputString = editTextUserInput.getText().toString();
        // text did change
        String newText = editTextUserInput.getText().toString();
        try {
          userInputLong = Long.parseLong(userInputString);
          buttonCalculateRoots.setEnabled(true);
          if (userInputLong <= 0){
            buttonCalculateRoots.setEnabled(false);
          }
        } catch(NumberFormatException e){
          buttonCalculateRoots.setEnabled(false);
        }
      }
    });

    // set click-listener to the button
    buttonCalculateRoots.setOnClickListener(v -> {
      Intent intentToOpenService = new Intent(MainActivity.this, CalculateRootsService.class);
      String userInputString = editTextUserInput.getText().toString();
      long userInputLong = Long.parseLong(userInputString);

      //9181531581341931811

      intentToOpenService.putExtra("number_for_service", userInputLong);
      startService(intentToOpenService);
      buttonCalculateRoots.setEnabled(false);
      editTextUserInput.setEnabled(false);
      progressBar.setVisibility(View.VISIBLE);
      this.numberToCalculate = userInputString;
      this.isWaitingForCalculation = true;
    });

    // register a broadcast-receiver to handle action "found_roots"
    broadcastReceiverForSuccess = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("found_roots")) return;
        long root1 = incomingIntent.getLongExtra("root1", 0);
        long root2 = incomingIntent.getLongExtra("root2", 0);
        long originalNumber = incomingIntent.getLongExtra("original_number", 0);
        long timePassed = incomingIntent.getLongExtra("time_to_calculate", 0);
        Intent successIntent = new Intent(MainActivity.this, RootsResults.class);
        successIntent.putExtra("root1", root1);
        successIntent.putExtra("root2", root2);
        successIntent.putExtra("original_number", originalNumber);
        successIntent.putExtra("time_to_calculate", timePassed);
        startActivity(successIntent);

        progressBar.setVisibility(View.GONE);
        editTextUserInput.setText("");
        editTextUserInput.setEnabled(true);
        buttonCalculateRoots.setEnabled(false);
        isWaitingForCalculation = false;
        numberToCalculate = "";
      }
    };
    registerReceiver(broadcastReceiverForSuccess, new IntentFilter("found_roots"));

    // register a broadcast-receiver to handle action "found_roots"
    broadcastReceiverForFail = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("stopped_calculations")) return;
        long originalNumber = incomingIntent.getLongExtra("original_number", 0);
        long timeUntilGiveUpSecond = incomingIntent.getLongExtra("time_until_give_up_second", 0);
        String messageTimeItTooks = "calculation aborted after " + String.valueOf(timeUntilGiveUpSecond) + "seconds"; //todo
        Toast.makeText(MainActivity.this, messageTimeItTooks, Toast.LENGTH_LONG).show(); //todo

        progressBar.setVisibility(View.GONE);
        editTextUserInput.setText("");
        editTextUserInput.setEnabled(true);
        buttonCalculateRoots.setEnabled(false);
        isWaitingForCalculation = false;
        numberToCalculate = "";
      }
    };
    registerReceiver(broadcastReceiverForFail, new IntentFilter("stopped_calculations"));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.unregisterReceiver(broadcastReceiverForSuccess);
    this.unregisterReceiver(broadcastReceiverForFail);
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean("is_waiting_for_calculation", isWaitingForCalculation);
    outState.putString("number_to_calculate", numberToCalculate);
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    this.isWaitingForCalculation = savedInstanceState.getBoolean("is_waiting_for_calculation");
    this.numberToCalculate = savedInstanceState.getString("number_to_calculate");

    ProgressBar progressBar = findViewById(R.id.progressBar);
    EditText editTextUserInput = findViewById(R.id.editTextInputNumber);
    Button buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);
    editTextUserInput.setEnabled(!this.isWaitingForCalculation);
    buttonCalculateRoots.setEnabled(!this.isWaitingForCalculation);
    editTextUserInput.setText(this.numberToCalculate);

    if (this.isWaitingForCalculation){
      progressBar.setVisibility(View.VISIBLE);
    }
    else{
      progressBar.setVisibility(View.GONE);
    }
  }
}
