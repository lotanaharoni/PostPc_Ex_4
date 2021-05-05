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
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private BroadcastReceiver broadcastReceiverForSuccess = null;
  private BroadcastReceiver broadcastReceiverForFail = null;
  // TODO: add any other fields to the activity as you want


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
      //long userInputLong = 0;
      long userInputLong = Long.parseLong(userInputString);

      //9181531581341931811


      // todo: check that `userInputString` is a number. handle bad input. convert `userInputString` to long
       // todo this should be the converted string from the user
      intentToOpenService.putExtra("number_for_service", userInputLong);
      startService(intentToOpenService);
      buttonCalculateRoots.setEnabled(false);
      editTextUserInput.setEnabled(false);
      progressBar.setVisibility(View.VISIBLE);
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
      }
    };
    registerReceiver(broadcastReceiverForSuccess, new IntentFilter("found_roots"));
   // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();


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
    // TODO: put relevant data into bundle as you see fit
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    // TODO: load data from bundle and set screen state (see spec below)
  }
}


/*

TODO:
the spec is:

the progress behavior is: //todo: check
* when there is a calculation in the BG, progress is showing
* otherwise (not calculating anything in the BG), progress is hidden

when "calculate roots" button is clicked:
* change states for the progress, edit-text and button as needed, so user can't interact with the screen //todo: progres?

upon screen rotation (saveState && loadState) the new screen should show exactly the same state as the old screen. this means:
* edit-text shows the same input
* edit-text is disabled/enabled based on current "is waiting for calculation?" state
* progress is showing/hidden based on current "is waiting for calculation?" state
* button is enabled/disabled based on current "is waiting for calculation?" state && there is a valid number in the edit-text input


 */