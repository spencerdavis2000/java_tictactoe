/*
 Spencer Davis
 Unbeatable TicTacToe program redo 10/12/2012
 Get rid of duplications
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class practiceMain {
	
	private char [] field;
	private static final int DIM = 3;
	private static final int SIZE = (DIM * DIM)+1;
	private static final int GROUPSIZE = 8;
	private char space = ' ';
	private char Xmove = 'X';
	private char Omove = 'O';
	private boolean gameOver = false;
	private boolean win = false;
	private boolean draw = false;
	private int currentPlayer = 1;
	//objects
	private Count count;
	Random random = new Random();
	
	//used to match the group number with the group elements shown in the init()
	HashMap<Integer, Integer []> hashmap = new HashMap<Integer, Integer []>();
	
	//constructor
	public practiceMain(){
		field = new char[SIZE];
		count = new Count(field, DIM, space, Omove, Xmove);
		this.init();
	}
	//initialize everything
	private void init() {
		//set the field to all empty spaces
		for(int i = 1; i<SIZE; i++){
			field[i] = space;
		}
		//wire up the group numbers to the members in each group
		hashmap.put(1, new Integer[]{1,2,3});
		hashmap.put(2, new Integer[]{4,5,6});
		hashmap.put(3, new Integer[]{7,8,9});
		hashmap.put(4, new Integer[]{1,4,7});
		hashmap.put(5, new Integer[]{2,5,8});
		hashmap.put(6, new Integer[]{3,6,9});
		hashmap.put(7, new Integer[]{1,5,9});
		hashmap.put(8, new Integer[]{3,5,7});
	}
	//start the game here
	public void playGame(){
		while(!gameOver){
			if(currentPlayer == 1){
				playerOne();
				if(win || draw){
					gameOver = true;
				}
				else{
					++currentPlayer;
				}
			}
			else if(currentPlayer == 2){
				playerTwo();
				if(win || draw){
					gameOver = true;
				}
				else{
					--currentPlayer;
				}
				print();
			}
		}
		System.out.println("Game Over. Thanks for playing:-)");
	}
	//playerOne turn
	public void playerOne(){
		Scanner scan = new Scanner(System.in);
		boolean madeMove = false;
		try{
			while(!madeMove){
				System.out.println("Player One:  Please enter (1-9): ");
				int move = scan.nextInt();
				if((move > 0)&& (move <=9)){
					if(field[move]==space){
						field[move] = Omove;
						madeMove = true;
					}
					else{
						System.out.println("Field already filled.  Enter another field.");
					}
				}
				else{
					System.out.println("Incorrent Value");
				}
			}
		}catch(Exception e){
			System.out.println("Problem with the entry. " + e.toString());
		}
	}
	//playerTwo turn
	public void playerTwo(){
		this.analyzeP2MakeMove();
		this.checkForWin();
		this.checkForDraw();
	}
	//check to see if there is a tie.  check each set of groups and when they all are true, end game
	public void checkForDraw(){
		int counter = 0;
		for(int i = 1; i<=GROUPSIZE; i++){
			if(((getGroup(i)==4)||(getGroup(i)==5)||(getGroup(i)==8))){
				++counter;
			}
		}
		if(counter == 8){
			draw = true;
			//gameOver = true;
			System.out.println("Tied, no winner!");
		}
	}
	//Check for a winner
	public void checkForWin(){
		for(int i = 1; i<=GROUPSIZE; i++){
			if(getGroup(i)==3){
				win = true;
				//gameOver = true;
				System.out.println("Player 1 wins!");
			}
			else if(getGroup(i)==6){
				win = true;
				//gameOver = true;
				System.out.println("Player 2 wins!");
			}
		}
	}
	public void analyzeP2MakeMove(){

//=========Everything here happens in proper order================================		
		//1.  check for middle first
		if(field[5]==space){
			field[5]=Xmove;
		}
		//2.  if O is in middle, make next X move on corners
		else if(((field[5])==Omove)&&((field[1])==space)&&((field[3])==space)&&((field[7])==space)&&((field[9])==space)){
			ArrayList<Integer> value = new ArrayList<Integer>();
			value.add(1);
			value.add(3);
			value.add(7);
			value.add(9);
			int number = value.get(random.nextInt(value.size()));
			field[number]= Xmove;
			value.clear();
		}
		/*
		One situation that can make it beatable is if the human picks 2
		the machine picks 5, the human picks 9 and the machine picks 6 instead of 4.
		This is true starting at 2, and also 4, 6, and 8
		Basically, you don't want this:
		_|O|_
		_|X|X
		_|_|O
		You want the sequence of moves to be:
		if it starts at 2:  
		_|O|_
		X|X|_
		_|_|O
		*/
		//3.  next situation
		else if(((getGroup(4))==15)&&((getGroup(5))==8)&&((getGroup(6))==12)){
			field[4]=Xmove;
		}
		else if(((getGroup(4))==12)&&((getGroup(5))==8)&&((getGroup(6))==15)){
			field[6]=Xmove;
		}
		else if(((getGroup(1))==15)&&((getGroup(2))==8)&&((getGroup(3))==12)){
			field[2]=Xmove;
		}
		else if(((getGroup(1))==12)&&((getGroup(2))==8)&&((getGroup(3))==15)){
			field[8]=Xmove;
		}
		//4.  this creates the entry point if at least one group == 7
		else if(((getGroup(1))==7)||((getGroup(2))==7)||((getGroup(3))==7)||
				((getGroup(4))==7)||((getGroup(5))==7)||((getGroup(6))==7)||
				((getGroup(7))==7)||((getGroup(8))==7)){
			ArrayList<Integer> seven = new ArrayList<Integer>();
			//once we are in, find the first group equal to seven
			//find the empty place and mark an X
			for(int i = 1; i<=GROUPSIZE; i++){
				if(this.ifEqualsToSeven(i)){
					seven.add(i);
				}
			}
			this.makeMoveSeven(seven.get(0));//mark an X
			seven.clear(); //clear the list
		}
		//5.  entry point to if at least one group == 9
		else if(((getGroup(1))==9)||((getGroup(2))==9)||((getGroup(3))==9)||
				((getGroup(4))==9)||((getGroup(5))==9)||((getGroup(6))==9)||
				((getGroup(7))==9)||((getGroup(8))==9)){
			ArrayList<Integer> nine = new ArrayList<Integer>();
			for(int i = 1; i<=GROUPSIZE; i++){
				if(this.ifEqualsToNine(i)){
					nine.add(i);
				}
			}
			this.makeMoveNine(nine.get(0));//mark an X
			nine.clear(); //clear list
		}
		//6.  entry point to if at least one group == 11
		else if(((getGroup(1))==11)||((getGroup(2))==11)||((getGroup(3))==11)||
				((getGroup(4))==11)||((getGroup(5))==11)||((getGroup(6))==11)||
				((getGroup(7))==11)||((getGroup(8))==11)){
			ArrayList<Integer> eleven = new ArrayList<Integer>();
			for(int i = 1; i<=GROUPSIZE; i++){
				if(this.ifEqualsToEleven(i)){
					eleven.add(i);
				}
			}
			this.makeMoveEleven(eleven.get(0));
			eleven.clear();
		}
		//7.  by this time, if any empty spaces are there and nothing above fits, just randomly pick next move
		else if((field[1]==space)||(field[2]==space)||(field[3]==space)||
				(field[4]==space)||(field[5]==space)||(field[6]==space)||
				(field[7]==space)||(field[8]==space)||(field[9]==space)){
			ArrayList<Integer> anythingElse = new ArrayList<Integer>();
			//find empty spaces
			for(int i = 1; i<SIZE; i++){
				if(field[i]==space){
					anythingElse.add(i);//add to the list
				}
			}
			//pick a random empty space and mark an X
			//this is for any situation other than the above situations
			//we don't want X not making a move because nothing above fits
			int number = anythingElse.get(random.nextInt(anythingElse.size()));
			field[number] = Xmove;
		}
	}
	//Print out to the screen
	private void print() {
		System.out.println(field[1]+ "|"+field[2]+ "|"+field[3]+ "\n"+
				field[4]+ "|"+field[5]+ "|"+field[6]+ "\n"+
				field[7]+ "|"+field[8]+ "|"+field[9]);
	}
	//test if equal to 7
	public boolean ifEqualsToSeven(int groupNumber){
		if(this.getGroup(groupNumber)==7){
			return true;
		}
		else{
			return false;
		}
	}
	//test if equal to 9
	public boolean ifEqualsToNine(int groupNumber){
		if(this.getGroup(groupNumber)==9){
			return true;
		}
		else{
			return false;
		}
	}
	//test if equal to 11
	public boolean ifEqualsToEleven(int groupNumber){
		if(this.getGroup(groupNumber)==11){
			return true;
		}
		else{
			return false;
		}
	}
	//use this to determine which value will be marked by an X in other methods
	public int pickValue(int groupNumber, int groupTotal){
		ArrayList<Integer> value = new ArrayList<Integer>();
		int n1, n2, n3;
		int number = 0;
		//use the hashmap above to get the proper group set for group number
		n1 = hashmap.get(groupNumber)[0];
		n2 = hashmap.get(groupNumber)[1];
		n3 = hashmap.get(groupNumber)[2];
		if(field[n1]==space){
			value.add(n1);
		}
		if(field[n2]==space){
			value.add(n2);
		}
		if(field[n3]==space){
			value.add(n3);
		}
		if((groupTotal == 7)||groupTotal == 9){
			number = value.get(0);
		}
		else if((groupTotal == 11) && (field[5]==Xmove)){
			number = value.get(random.nextInt(value.size()));
		}
		else if((groupTotal == 11)&&(field[5]==Omove)){
			if(value.contains(1)){
				number = value.get(0);
			}
			else if(value.contains(3)){
				number = value.get(0);
			}
			else if(value.contains(7)){
				number = value.get(0);
			}
			else if(value.contains(9)){
				number =value.get(0);
			}
		}
		value.clear();//clear the ArrayList
		return number;
	}
	//Make next move if group is equal to 11
	public void makeMoveEleven(int groupNumber){
		int number = this.pickValue(groupNumber, 11);
		field[number] = Xmove;
	}
	//Make the next move if group is equal to 7 or 9
	public void makeMoveSeven(int groupNumber){
		int number = this.pickValue(groupNumber, 7);
		field[number] = Xmove;
	}
	//Make the next move if group is equal to 7 or 9
	public void makeMoveNine(int groupNumber){
		int number = this.pickValue(groupNumber, 9);
		field[number] = Xmove;
	}
	//Main
	public static void main(String args []){
		practiceMain go = new practiceMain();
		go.playGame();
	}
	//get the group totals
	public int getGroup(int groupNumber){
		return count.groupCount(hashmap.get(groupNumber)[0], hashmap.get(groupNumber)[1], hashmap.get(groupNumber)[2]);
	}
}
//The class that is used to get the group totals
class Count{
	private char [] field;
	private int DIM;
	private char space, Omove, Xmove;
	
	public Count(char [] field, int DIM, char space, char Omove, char Xmove){
		this.field = field;
		this.DIM = DIM;
		this.space = space;
		this.Omove = Omove;
		this.Xmove = Xmove;
	}
	public int groupCount(int n1, int n2, int n3){
		char [] c = new char[DIM];
		c[0] = field[n1];
		c[1] = field[n2];
		c[2] = field[n3];
		int total = 0;
		for(int i = 0; i<DIM; i++){
			if(c[i] == space){
				total += 5;
			}
			else if(c[i] == Omove){
				total += 2;
			}
			else if(c[i] == Xmove){
				total += 1;
			}
		}
		return total;
	}
}
