package exercise.find.roots;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import java.lang.Math;
import java.util.concurrent.TimeUnit;

public class CalculateRootsService extends IntentService {


  public CalculateRootsService() {
    super("CalculateRootsService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent == null) return;
    long timeStartMs = System.currentTimeMillis();
    long numberToCalculateRootsFor = intent.getLongExtra("number_for_service", 0);
    if (numberToCalculateRootsFor <= 0) {
      Log.e("CalculateRootsService", "can't calculate roots for non-positive input" + numberToCalculateRootsFor);
      return;
    }
    Intent resultIntent = new Intent();
    if (numberToCalculateRootsFor == 1){
      sendSuccessResults(resultIntent, 1, 1, 1);
      return;
    }

    long root1 = 1;
    long root2 = 1;
    long timePassed = 0;
    double sqrtNumber = Math.sqrt(numberToCalculateRootsFor);
    for (long i = 2; i <sqrtNumber; i ++){
      timePassed = System.currentTimeMillis() - timeStartMs;
      if (numberToCalculateRootsFor % i == 0){
        if (TimeUnit.MILLISECONDS.toSeconds(timePassed) <= 20) {
          root1 = i;
          root2 = numberToCalculateRootsFor / i;
          sendSuccessResults(resultIntent, root1, root2, numberToCalculateRootsFor);
          return;
        }
      }
      if (TimeUnit.MILLISECONDS.toSeconds(timePassed) > 20){
        sendFailResults(resultIntent,timePassed, numberToCalculateRootsFor);
        return;
      }
    }
    timePassed = System.currentTimeMillis() - timeStartMs;
    sendSuccessResults(resultIntent, numberToCalculateRootsFor, 1, numberToCalculateRootsFor);
    /*
    TODO:
     calculate the roots.
     check the time (using `System.currentTimeMillis()`) and stop calculations if can't find an answer after 20 seconds
     upon success (found a root, or found that the input number is prime):
      send broadcast with action "found_roots" and with extras:
       - "original_number"(long)
       - "root1"(long)
       - "root2"(long)
     upon failure (giving up after 20 seconds without an answer):
      send broadcast with action "stopped_calculations" and with extras:
       - "original_number"(long)
       - "time_until_give_up_seconds"(long) the time we tried calculating

      examples:
       for input "33", roots are (3, 11)
       for input "30", roots can be (3, 10) or (2, 15) or other options
       for input "17", roots are (17, 1)
       for input "829851628752296034247307144300617649465159", after 20 seconds give up

     */
  }

  private void sendSuccessResults(Intent resultIntent, long root1, long root2,
                                  long numberToCalculateRootsFor){
    resultIntent.setAction("found_roots");
    resultIntent.putExtra("root1", root1);
    resultIntent.putExtra("root2", root2);
    resultIntent.putExtra("original_number", numberToCalculateRootsFor);
    this.sendBroadcast(resultIntent);
  }

  private void sendFailResults(Intent resultIntent,long timePass, long numberToCalculateRootsFor){
    resultIntent.setAction("stopped_calculations");
    resultIntent.putExtra("original_number", numberToCalculateRootsFor);
    resultIntent.putExtra("time_until_give_up_second", timePass);
    this.sendBroadcast(resultIntent);
  }
}