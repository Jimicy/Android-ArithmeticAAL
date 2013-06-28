package com.example.arithmeticaal;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class contains the methods for the android assisted learning program.
 * It primarily responsible for generating numbers, answer, question and checking if user input matches answer.
 * It is also responsible for holding static variables aka "state variables" related to the game.
 * 
 * It also contains variable for number1, number2, answer, correct and wrong.
 * It contains static variables for seed, grade, timeInterval, activeQuestionTypes and arithmetic constants.
 * 
 * They are static so all instances of the class will have the same instance variable values. This was intended for 2 player which follows.
 * the same logic as 1 player. But due to time constraint. It was not implemented.
 * 
 * It has methods to generate numbers, return string representations of the question and check answers
 * It has accessor and mutator methods to set grade, timeInterval and activeQuestionTypes
 * It has methods for correct response, wrong response and generating percent of correct answers
 * 
 *
 * @author Jimmy Wang
 * @version May 28 2013
 */
public class AndroidAssistedLearning{

	//instance variables for number1,number2 and answer
	private int number1;
	private int number2;
	private int answer;

	private int correct; //number of correct responses
	private int wrong; //number of wrong responses

	//int variable seed used to generate numbers
	//static so all instances of the class have the same seed value
	private static int seed;
	
	//int variable for grade
	//static so all instances of the class have the same grade
	private static int grade; //1 single digits, 2 or 3 two digits, 4 or 5 three digits and 6 four digits
	
	//int variable for time interval in seconds
	//static so all instances of the class have the same time interval
	private static int timeInterval;
	
	//int array list for which questionTypes are active 1 addition, 2 subtraction, 3 multiplication, 4 division
	//static so all instances of the class have the same activeQuestionTypes
	private static ArrayList<Integer> activeQuestionTypes = new ArrayList<Integer>(); 

	//Magic numbers for question types
	private final static int ADDITION=1;
	private final static int SUBTRACTION=2;
	private final static int MULTIPLICATION=3;
	private final static int DIVISION=4;
	
	//instantiate Random object
	private Random randomNumbers = new Random();
	
	/*
	 * This constructor sets grade to 1, adds ADDITION as an activeQuestionType, sets timeInterval to 120 and calls setSeed while passing in grade
	 */
	public AndroidAssistedLearning()
	{   
		grade=1; //grade level is set to 1 by default
		activeQuestionTypes.add(ADDITION); //addition is an activeQuestionType by default
		timeInterval=120; //120s by default
		setSeed(grade);
	}
	
	/*
	 * This overloaded constructor takes values to initialize grade, activeQuestionTypes and timeInterval
	 * It also calls setSeed while passing in grade
	 */
	public AndroidAssistedLearning(int grade, ArrayList<Integer> activeQuestionTypes, int timeInterval)
	{   
		AndroidAssistedLearning.grade=grade;
		AndroidAssistedLearning.activeQuestionTypes=activeQuestionTypes;
		AndroidAssistedLearning.timeInterval=timeInterval;
		setSeed(grade);
	}

	/*
	 * This is a private method. It takes an int parameter type representing the question type.
	 * It generates numbers using seed 
	 * If the type passed in is 2 which means subtraction and number1 is less than number2, randomly generate until number1 is greater.
	 */ 
	private void generateNumbers(int type)
	{
		number1=randomNumbers.nextInt(seed)+1;
		number2=randomNumbers.nextInt(seed)+1;

		//while type is 2 and number 1 is less than number 2, random number 1 again until it is greater prevents negatives
		while (type==2 && number1<number2){
			number1=randomNumbers.nextInt(seed);
		}

	}

	/*
	 * This method randomly picks a question type from those that are active
	 * switch statement to generate for the answer and string representation for the question type
	 * 
	 */
	public String generateQuestion()
	{
		int type=activeQuestionTypes.get(randomNumbers.nextInt(activeQuestionTypes.size())); //randomly pick a type from activeQuestionTypes
		generateNumbers(type); //generate number1 and number2 based on the type
		
		switch(type)
		{
			case ADDITION:
				answer=number1+number2;
				return String.format("%d+%d=",number1,number2);
			case SUBTRACTION:
				answer=number1-number2;
				return String.format("%d-%d=",number1,number2);
			case MULTIPLICATION:
				answer=number1*number2;
				return String.format("%d*%d=",number1,number2);
			case DIVISION:
				answer=number1/number2;
				return String.format("%d÷%d=",number1,number2);
		}
		
		return "";//return statement to make method valid, but this statement will never occur
	}

	/*
	 * This method takes an int input parameter. 
	 * 
	 * It compares it to the answer. If it's correct, correctResponse is called,
	 * correct is increased by 1. 
	 * return true
	 * 
	 * If it is not correct, incorrectResponse is called. 
	 * wrong is increased by 1
	 * return false
	 * 
	 */
	public boolean checkAnswer(int input)
	{
		if (input==answer){
			correct+=1;
			return true;
		}

		else{
			wrong+=1;
			return false;
		}
	}
	

	/*
	 * This method takes no parameters. It is called when the user's answer equals the answer
	 * It randomly picks and returns 1 out of 4 statements
	 */
	public String correctResponse()
	{
		String responses[]={"Very good!","Excellent!","Nice work!","Keep up the good work!"};
		return (responses[randomNumbers.nextInt(4)]);
	}

	/*
	 * This method takes no parameters. It is called when the user's answer does not equal the answer
	 * It randomly picks and returns 1 out of 4 statements
	 */    
	public String incorrectResponse()
	{
		String responses[]={"No. Please try again","Wrong. Try once more","Don't give up!","No. Keep trying"};
		return (responses[randomNumbers.nextInt(4)]);
	}  
	
	/*
	 * This method is private and is only used within this class. It sets the seed based on the grade
	 */
	private void setSeed(int grade)
	{
		//Sets seed based on grade. This is basically setting the difficulty since the generated numbers use the seed
		switch (grade)
		{
			case 1:
				seed=5;
				break;
			case 2:
				seed=10;
				break;
			case 3:
				seed=15;
				break;
			case 4:
				seed=20;
				break;
			case 5:
				seed=25;
				break;
			case 6:
				seed=30;
				break;
		}
	}
	
	/*
	 * This method sets the grade from the one passed in and calls setSeed() while passing in grade to generate a new seed
	 */
	public void setGrade(int grade)
	{
		AndroidAssistedLearning.grade=grade;
		setSeed(grade);
	}
	
	/*
	 * This method sets the time interval
	 */
	public void setTimeInterval(int timeInterval)
	{
		AndroidAssistedLearning.timeInterval=timeInterval;
	}
	
	/*
	 * This method takes no parameters
	 * It resets the values for correct and wrong
	 * It returns the percentage of correct answers
	 */  
	public int getPercent()
	{
		
		Double percent=(correct/(correct+wrong*1.0))*100;
		correct=0;
		wrong=0;
		
		return percent.intValue();
	} 
	
	/*
	 * This method returns the answer in string format
	 */
	public String getAnswer()
	{
		return (""+answer);
	}
	
	/*
	 * Accesssor method for grade
	 */
	public int getGrade()
	{
		return grade;
	}
	
	/*
	 * Accesssor method for timeInterval
	 */
	public int getTimeInterval()
	{
		return timeInterval;
	}
	
	/*
	 * This method checks if questionType exists in ArrayList ActiveQuestionTypes. If not add it
	 */
	public void addActiveQuestionTypes(int type)
	{
		if (!activeQuestionTypes.contains(type))
			activeQuestionTypes.add(type);
	}
	
	/*
	 * This method checks if questionType exists in ArrayList ActiveQuestionTypes. If so remove it
	 */
	public void removeActiveQuestionTypes(int type)
	{
		if (activeQuestionTypes.contains(type))
			activeQuestionTypes.remove(type);
	}


}//end class