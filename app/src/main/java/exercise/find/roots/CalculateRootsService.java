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
      sendSuccessResults(resultIntent, 1, 1, 1, 0);
      return;
    }

    long root1 = 1;
    long root2 = 1;
    double sqrtNumber = Math.sqrt(numberToCalculateRootsFor);
    for (long i = 2; i <sqrtNumber; i ++){
      long timePassed = System.currentTimeMillis() - timeStartMs;
      if (numberToCalculateRootsFor % i == 0){
        if (TimeUnit.MILLISECONDS.toSeconds(timePassed) <= 20) {
          root1 = i;
          root2 = numberToCalculateRootsFor / i;
          sendSuccessResults(resultIntent, root1, root2, numberToCalculateRootsFor, TimeUnit.MILLISECONDS.toSeconds(timePassed));
          return;
        }
      }
      if (TimeUnit.MILLISECONDS.toSeconds(timePassed) > 20){
        sendFailResults(resultIntent,timePassed, numberToCalculateRootsFor);
        return;
      }
    }
    long timePassed = System.currentTimeMillis() - timeStartMs;
    sendSuccessResults(resultIntent, numberToCalculateRootsFor, 1, numberToCalculateRootsFor, TimeUnit.MILLISECONDS.toSeconds(timePassed));
  }

  private void sendSuccessResults(Intent resultIntent, long root1, long root2,
                                  long numberToCalculateRootsFor, long timePassed){
    resultIntent.setAction("found_roots");
    resultIntent.putExtra("root1", root1);
    resultIntent.putExtra("root2", root2);
    resultIntent.putExtra("original_number", numberToCalculateRootsFor);
    resultIntent.putExtra("time_to_calculate", timePassed);
    this.sendBroadcast(resultIntent);
  }

  private void sendFailResults(Intent resultIntent,long timePass, long numberToCalculateRootsFor){
    resultIntent.setAction("stopped_calculations");
    resultIntent.putExtra("original_number", numberToCalculateRootsFor);
    resultIntent.putExtra("time_until_give_up_second", timePass);
    this.sendBroadcast(resultIntent);
  }
}