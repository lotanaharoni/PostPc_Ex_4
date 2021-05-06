package exercise.find.roots;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest extends TestCase {

  @Test
  public void when_activityIsLaunching_then_theButtonShouldStartDisabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "calculate" button is disabled
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
    assertFalse(button.isEnabled());
  }

  @Test
  public void when_activityIsLaunching_then_theEditTextShouldStartEmpty(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "input" edit-text has no text
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    String input = inputEditText.getText().toString();
    assertTrue(input == null || input.isEmpty());
  }

  @Test
  public void when_userIsEnteringNumberInput_and_noCalculationAlreadyHappned_then_theButtonShouldBeEnabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and the button
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    inputEditText.setText("32");
    assertTrue(button.isEnabled());

  }

  @Test
  public void when_activityIsLaunching_then_theProgressBarShouldStartGone(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "calculate" button is disabled
    ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);
    assertEquals(progressBar.getVisibility(), View.GONE);
  }

  @Test
  public void when_user_enter_valid_number_and_then_delete_it_then_theButtonShouldBeEnabledAndThenDisabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "calculate" button is disabled
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
    EditText editTextUserInput = mainActivity.findViewById(R.id.editTextInputNumber);
    assertFalse(button.isEnabled());

    editTextUserInput.setText("32");
    assertTrue(button.isEnabled());

    editTextUserInput.setText("");
    assertFalse(button.isEnabled());
  }

  @Test
  public void when_user_enter_invalid_number_then_theButtonShouldBeDisabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "calculate" button is disabled
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
    EditText editTextUserInput = mainActivity.findViewById(R.id.editTextInputNumber);
    assertFalse(button.isEnabled());

    editTextUserInput.setText("-2");
    assertFalse(button.isEnabled());
  }
}