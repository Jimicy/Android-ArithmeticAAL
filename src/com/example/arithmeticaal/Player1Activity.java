package com.example.arithmeticaal;

import com.example.arithmeticaal.R;
import android.app.Activity;
import android.os.Bundle;

//alert dialog popups for when you press one of the menu options
import android.app.AlertDialog;
import android.content.DialogInterface;

//widget classes
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//Used to provide a Timer and classes related to timer
import java.util.Timer;
import java.util.TimerTask;

//used to create delay
import android.os.Handler;

//Used to provide Settings menu at top-right of app
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

//Used to load and play sounds effects, and control volume
import android.media.SoundPool;
import android.media.AudioManager;

/**
 * This class contains the methods player1Activity
 * It is responsible for handling the time, score, displaying question, displaying appropriate responses, playing sound effects and getting input
 * It is also responsible for generating the menu
 *
 * @author Jimmy Wang
 * @version May 28 2013
 */
public class Player1Activity extends Activity 
{
	private int timeLeft;
	private int score;
	
	private AndroidAssistedLearning aal;
	
	//time and score textView widgets
	private TextView timeLeftTextView;
	private TextView scoreTextView;
	private TextView responseTextView;
	private TextView questionTextView;
	private EditText inputEditText;
	
	//create instance variables needed for timer
	private Timer timer;
	private TimerTask task;
	
	//create instance variable for handler which will be used to as a delay
	private Handler handler;
	
	// instance variables for playing sound effects
	private SoundPool soundPool;
	private int yes_sound_id;
	private int no_sound_id;
	
	/*
	 * Creates the layout
	 * Creates necessary object and widget references and instantiates instance variables/objects
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player1);
		
		//Initialize androidAssistedLearning
		aal=new AndroidAssistedLearning();
		
		//Initialize time
		timeLeft=aal.getTimeInterval();
		timeLeftTextView = (TextView) findViewById(R.id.timeLeftTextView);
		timeLeftTextView.setText(String.format("Time Left: %ds",timeLeft));
		createTimer();
		
		//Initialize handler
        handler = new Handler();
		
		//Initialize score
		score=0;
		scoreTextView = (TextView) findViewById(R.id.scoreTextView);
		scoreTextView.setText(String.format("Score: %d",score));
		
		//initialize responseTextView
		responseTextView = (TextView) findViewById(R.id.responseTextView);
		
		//initialize questionTextView
		questionTextView = (TextView) findViewById(R.id.questionTextView);
		questionTextView.setText(aal.generateQuestion());
		
		//initialize inputEditText
		inputEditText = (EditText) findViewById(R.id.inputEditText);
		
		// Allow volume buttons to set the game volume
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		 
		// Create a SoundPool object, and use it to load the two sound effects
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		yes_sound_id = soundPool.load(this, R.raw.yes, 1);
		no_sound_id = soundPool.load(this, R.raw.no, 1);
		
	}
	
	/*
	 *  Called automatically when app is launched to configure Settings menu at top-right.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);
	     
	    // Add our "Number Of Hints" menu option as the FIRST item
	    menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, R.string.grade_level);
	    
	    // Add our "Time Interval" menu option as the second (FIRST+1) second item
	    menu.add(Menu.NONE, Menu.FIRST+1, Menu.NONE, R.string.time_interval);
	    
	    // Add our "Reset Quiz" menu option as the third (FIRST+2) item
	    menu.add(Menu.NONE, Menu.FIRST+2, Menu.NONE, R.string.reset_quiz);
	 
	    return true;
	}
	
	/*
	 * Called automatically when an option in the Settings menu at top-right is selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    // If the user has selected our "Grade Level" menu option
	    if (item.getItemId() == Menu.FIRST) {
	        // Create an AlertDialog Builder and set its title to "Select Grade Level" string resource
	        AlertDialog.Builder choicesBuilder = new AlertDialog.Builder(this);
	        choicesBuilder.setTitle(R.string.select_grade_level);
	 
	        // Read array of the possible grade levels (1-6) from string-array resource
	        final String[] gradeLevelOptions = getResources().getStringArray(R.array.gradeLevels);
	         
	        // Add array of possible choices to the Dialog and set the behavior when one is tapped.
	        choicesBuilder.setItems(R.array.gradeLevels,
	                new DialogInterface.OnClickListener()
	                {
	        			//when one of the dialogs is pressed
	                    public void onClick(DialogInterface dialog, int item)
	                    {
	                    	//set grade in aal(Android Assisted Learning object) to the gradeLevelOption selected
	                    	aal.setGrade(Integer.parseInt(gradeLevelOptions[item]));
	                    	questionTextView.setText(aal.generateQuestion());//generate a new question and set it to questionTextView
	                    	inputEditText.setText(""); //clears text in inputEditText
	                    	
	                    }
	                }
	        );
	        
	        // Create and display the dialog
	        AlertDialog choicesDialog = choicesBuilder.create();
	        choicesDialog.show();
	        return true;
	    }
	    
	 // If the user has selected our "Time Interval" menu option
	    else if (item.getItemId() == Menu.FIRST+1) {
	        // Create an AlertDialog Builder and set its title to "Select Time Interval" string resource
	        AlertDialog.Builder choicesBuilder = new AlertDialog.Builder(this);
	        choicesBuilder.setTitle(R.string.select_time_interval);
	 
	        // Read array of the possible time intervals (30,60,120,150,180,210) from string-array resource
	        final String[] timeIntervalOptions = getResources().getStringArray(R.array.timeIntervals);
	         
	        // Add array of possible choices to the Dialog and set the behavior when one is tapped.
	        choicesBuilder.setItems(R.array.timeIntervals,
	                new DialogInterface.OnClickListener()
	                {
	                    public void onClick(DialogInterface dialog, int item)
	                    {
	                    	//set time in aal(Android Assisted Learning object) to the gradeLevelOption selected
	                    	aal.setTimeInterval(Integer.parseInt(timeIntervalOptions[item]));
	                    	reset();
	                    	
	                    }
	                }
	        );
	        // Create and display the dialog
	        AlertDialog choicesDialog = choicesBuilder.create();
	        choicesDialog.show();
	        return true;
	    }
	    
	    // Otherwise, if they selected "Reset Quiz", then reset the game
	    else if (item.getItemId() == Menu.FIRST+2) {
	    	reset();
	        return true;           
	    }
	     
	    // If the user did not pick any of our menu items
	    return false;
	}

	/*
	 * This method is called by the buttons (except backspace) in player1 activity when they are pressed.
	 * It adds the string representation of the button pressed to the value in inputEditText and checks if the new value matches the answer
	 * If so generate a new question
	 */
	public void submitNumber(View v)
	{
		//only occurs if there is still timeLeft
		if (timeLeft>0)
		{
			//sets the inputEditText to the current value in inputEditText plus the string representation of the button added
			inputEditText.setText(inputEditText.getText()+((Button)v).getText().toString());
			checkAnswer(); //call check answer to see if the new number is correct
		}
	}

	/*
	 * This method is called by the backspace button in player1 activity when it is pressed
	 */
	public void deleteNumber(View v)
	{
		//only occurs if there is still timeLeft
		if (timeLeft>0)
		{
			String input=inputEditText.getText().toString(); //gets string value of inputEditText

			//only delete if the length of the input in inputEditText is greater than 0
			if (input.length()>0)
			{
				//the new value in inputEditText is a substring excluding the last index value
				inputEditText.setText(input.substring(0,input.length()-1));
				
				//checks that the new value in inputEditText has a length that's not 0
				if (inputEditText.getText().toString().length()!=0)
				{
					checkAnswer(); //call check answer to see if the new number is correct
				}
			}
			
		}
	}
	
	/*
	 * Get int value from inputEditText and checks if the answer is correct
	 * If the answer is correct. Play sound, display response, generate new question, increase score, update score and clear value in inputEditText
	 * else If the answer starts with what's currently in inputEditText, do nothing
	 * else the answer is wrong. Play sound and display response.
	 */
	public void checkAnswer()
	{
		//gets the int value for inputEditText and checks if it's right. If it is generate a new question
		if (aal.checkAnswer(Integer.parseInt(inputEditText.getText().toString())))
		{
			// Play the sound effect for the right answer
			soundPool.play(yes_sound_id, 1.0f, 1.0f, 1, 0, 1.0f);
			
			responseTextView.setText(aal.correctResponse()); //update responseTextView with a random correct response
			responseTextView.setTextColor(0xff00ff00); //set color of responseTextView to green
			clearResponseTextView(); //call the method to clear it with a 500ms delay
			
			questionTextView.setText(aal.generateQuestion());//generate a new question and set it to questionTextView
			score+=1;//increase score by 1
			scoreTextView.setText(String.format("Score: %d",score)); //update score on scoreTextView
			clearInputEditText(); //clears text in inputEditText
		}
		
		//if string of the answer starts with the current value in inputEditText do nothing
		else if (aal.getAnswer().startsWith(inputEditText.getText().toString()))
		{
			
		}
		
		//if the answer is wrong this executes
		else
		{
			// Play the sound effect for the wrong answer
			soundPool.play(no_sound_id, 1.0f, 1.0f, 1, 0, 1.0f);
			
			responseTextView.setText(aal.incorrectResponse()); //update responseTextView with a random incorrect response
			responseTextView.setTextColor(0xffff0000); //set color of responseTextView to red
			clearResponseTextView(); //call the method to clear it with a 500ms delay
			
			clearInputEditText(); //clears text in inputEditText
		}
	}
	
	/*
	 * Clears the value in inputEditText with a 500ms delay
	 */
	public void clearInputEditText()
	{
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				inputEditText.setText("");
			}
		}
		, 500);
	}
	
	/*
	 * Clears the value in responseTextView with a 500ms delay
	 */
	public void clearResponseTextView()
	{
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				responseTextView.setText("");
			}
		}
		, 500);
	}
	
	/*
	 * This method resets the instance. This means time, score, question and input are all reset or cleared
	 */
	public void reset()
	{
		//cancel current timer and create a new timer
		timer.cancel();
		createTimer();
		
    	//set score to zero
    	score=0;
    	//update score on scoreTextView
    	scoreTextView.setText(String.format("Score: %d",score));
    	
    	//clear value currently in responseTextView
    	responseTextView.setText("");
    	
    	//generate a new question
    	questionTextView.setTextColor(0xff000000); //reset its color to black
    	questionTextView.setText(aal.generateQuestion()); //set newly generated question
    	
    	//clear value currently in inputEditText
    	inputEditText.setText(""); //clears text in inputEditText
    	
	}

	/*
	 * This method resets the timer
	 */
	public void createTimer()
	{
		//resets the time left to the time interval specified in aal(AndroidAssistedLearning object)
		timeLeft=aal.getTimeInterval();
		
        //Declare the timer
        timer = new Timer();
        
        //create task object
        task=new Task();
        
        //Set the schedule function and rate (task, milliseconds before first execution, milliseconds between subsequent executions)
        timer.scheduleAtFixedRate(task, 0, 1000); 
	}
	
	/*
	 * Anonymous inner class that extends TimerTask
	 * It has a run method that executes everytime the task is run
	 * While there is timeLeft, it will set the timeLeftTextView's text and decrease timeLeft by 1
	 * If there is no timeLeft, it will cancel the timer and get percent and display responses with varying colors.
	 */
	private class Task extends TimerTask
	{
		public void run() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() 
				{
					if (timeLeft<0)
					{
						timer.cancel(); //cancel the current timer
						int percent=aal.getPercent(); //get percent of correct answers
						
						responseTextView.setTextColor(0xff888888); //set responseTextView color to gray
						questionTextView.setText(percent+"%"); //set responseTextView value to the percent of correct answers
						inputEditText.setText(""); //clear the inputEditText field
						
						//different responses and colors for the displayed percent depending on the int percent value
						if (percent<70)
						{
							responseTextView.setText("Please ask your teacher for help. Your percentage of Correct Responses is");
							questionTextView.setTextColor(0xffff0000); //set questionTextView color to red
						}
						else
						{
							responseTextView.setText("You'll go far in life. Congratulations. Your percentage of Correct Responses is");
							questionTextView.setTextColor(0xff00ff00); //set questionTextView color to green
						}
						
					}
					
					else
					{
						timeLeftTextView.setText(String.format("Time Left: %ds",timeLeft)); //update time on timeLeftTextView
						timeLeft -= 1;
					}
				}

			});
		}
	}
	
}