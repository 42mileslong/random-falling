import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	/* Grid variables */
	public static int gameHeight = 22; //contains the game height - 22
	public static int gameWidth = input("What is the width of the gameboard?"); //gets the game width, which is variable, from the user, calling input()
	public static Square[][] grid = new Square[gameWidth][gameHeight]; //makes a grid with the width and height of above variables

	/* Calculation variables */
	private static int currentRuns = 0; //stores the current amount of runs
	private static int totalRuns = 5; //stores how many runs the user wants
	public static boolean finishedRun = false; //stores whether the run has finished, and is set by the method Square.spawnNew() when it tries to spawn a square in a square
	private static int droppedSquares = 0; //stores the amount of squares dropped (is increased whenever a new block is spawned)
	private static ArrayList<Integer> droppedSquaresList = new ArrayList<Integer>(); //stores the amount of squares dropped for all of the runs

	public static void main(String args[]) {
		initialize(); //initializes the grid
		System.out.println("Run counts are:"); //tells the user that the below numbers are the number of blocks dropped for each run
		
		while (currentRuns < totalRuns) { //this loops until there have been the same number of runs as the variable totalRuns
			while (!finishedRun) { //this loops until Square.spawnNew() stops it when it spawns a square in a square
				Square.spawnNew(); //spawns a new square
				Square.dropAll(); //checks to drop all squares, but the only square effected is the new square
				droppedSquares++; //adds a new square to the list of dropped squares
				checkClearLine(); //checks to see whether a line needs to be cleared
//				printGrid(); //printGrid prints the current grid, which is useful for testing but not needed now
			}
			//once the run finishes...
			System.out.println(droppedSquares); //prints the number of dropped squares
			droppedSquaresList.add(droppedSquares); //adds the number of dropped squares to the array droppedSquaresList
			currentRuns++; //increases the number of runs
			finishedRun = false; //resets finishedRun
			droppedSquares = 0; //resets the number of dropped squares
			Square.clearGrid(); //clears the grid, getting ready for a new run
		}
		//after the runs are finished...
		System.out.println("Average dropped squares are " + findAverage()); //prints the average squares dropped with findAverage()
	}

	//initializes by putting squares in the grid
	private static void initialize() {
		for (int x = 0; x < gameWidth; x++) { //loops for width
			for (int y = 0; y < Main.gameHeight; y++) { //loops for height
				grid[x][y] = new Square(x, y); //makes a new square in the current xy
				if (y > 0) { //if the y is above the bottom
					grid[x][y].below = grid[x][y-1]; //sets the square below to be the `below` square
				} else { //if the y is on the bottom
					grid[x][y].below = null; //it sets the `below` to be null (there is no below)
				}
			}
		}
	}

	//gets user input and asks a certain question
	private static int input(String question) {
		System.out.println(question); //prints a question to the user
		Scanner sc = new Scanner(System.in); //makes a new scanner
		String text = sc.nextLine(); //retrieves the text before the new line
		sc.close(); //closes the scanner
		try { //try catch for testing whether the string is a valid integer
			return Integer.parseInt(text); //tries to parse the string to turn it into an integer
		} catch (NumberFormatException e) { //if the string is not a integer...
			System.err.println("Enter a valid integer"); //tells the user to enter a valid integer
			return input(question); //repeats the cycle
		}
	}
	
	@SuppressWarnings("unused")
	//prints the grid for testing purposes
	private static void printGrid() {
		for (int y = gameHeight -1; y >= 0; y--) { //inverted gameHeight loop, so lines printed will be in order
			for (int x = 0; x < Main.gameWidth; x++) { //loops of gameWidth
				if (grid[x][y].current) System.out.print("1 "); //if there is a square, it prints `1`
				if (!grid[x][y].current) System.out.print("0 "); //if there is not a square, it prints `0`
			}
			System.out.println(); //makes a new line (so each line will be on a new line)
		}
		System.out.println(); //makes a space after the printed grid
	}
	
	//checks to see whether the bottom line is ready to be cleared, and because only one square is dropping at a time it doesn't have to check all of the lines
	private static void checkClearLine() {
		boolean allAlive = true; //a variable that assumes all the squares in the line are alive
		for (int x = 0; x < gameWidth; x++) { //loops for gameWidth
			if (!grid[x][0].current) { //if there is not a square in the x
				allAlive = false; //sets all alive to false
			}
		}
		if (allAlive) { //if allAlive was never set to false
			clearLine(0); //it calls clearLine() for the line of 0 (the bottom)
		}
	}
	
	//removes square from a current line and drops the rest
	private static void clearLine(int y) {
		for (int x = 0; x < gameWidth; x++) { //loops for gameWidth
			grid[x][y].current = false; //sets the grid in the x position to false, using the argument y for the y variable
		}
		Square.dropAll(); //drops all of the squares
	}
	
	//finds the average number of dropped squares
	private static double findAverage() {
		double total = 0; //a variable to hold the total of all dropped squares
		for (int i = 0; i < droppedSquaresList.size(); i++) { //loops for the size of droppedSquaresList
			total += droppedSquaresList.get(i); //adds the dropped square to total
		}
		return (total/droppedSquaresList.size()); //returns total divided by the number of dropped squares
	}
}