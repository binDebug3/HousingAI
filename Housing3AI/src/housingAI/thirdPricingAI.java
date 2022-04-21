package housingAI;

import java.util.Random;
import java.util.Scanner;

public class thirdPricingAI {

	static int n = 6;						//number of features
	static int m = 100;						//number of input vectors
	static double[] y = new double[m];		//price vector
	static double[] h = new double[n];		//output function
	static double[][] x = new double[m][n];	//input matrix [set of inputs][type of input]
	static double[] alpha = new double[n];	//BUT what does alpha do?
	
	static double[] theta = new double[n];	//linear regression vector
	static double tSum = 0;					//temporary variable
	static double tDif = 0;					//temporary variable
	static double dJ0 = 0;					//temporary variable
	static double dJ1 = 0;					//temporary variable
	
	static int count = 0;
	static int countReruns = 0;
	
	
	public static void main(String[] args) {
		while (count == 0 || count > 100) {
			count = 0;
			defined();
			run();
			countReruns++;
		}
		printTrainingData();

		System.out.println("\n\nStep "+count+". y = "+displayVar(theta[0])+"X0 + "+displayVar(theta[1])+"X1 + "+displayVar(theta[2])+"X2 + "+displayVar(theta[3])+"X3 + "+displayVar(theta[4])+"X4 + "+displayVar(theta[5])+"X5. Attempts: "+countReruns+". Fitness: "+displayVar(J()));
		Scanner reader = new Scanner(System.in);
		boolean continu = true;
		while (continu) {
			newHouse();
			System.out.println("Enter '1' to continue: ");
			double chooseCont = reader.nextDouble();
			if (chooseCont == 1)
				continu = true;
			else
				continu = false;
		}
	}
	public static void defined() {
		setTrainingData();
		defAlpha();
	}
	public static void run() {
		double fun = 5;
		while (J() > 10 && count < 100) {
			gradientDescent();
			if (Math.round(fun) == Math.round(J()))
				break;
			if (Math.round(fun) == Math.round(J()))
				break;
			if (Math.round(J()) < 10)
				break;
			count++;
			fun = J();
		}
	}
	
	//AI algorithm
	public static void gradientDescent() {
		tSum = 0;							//reset variables
		tDif = 0;							//I forgot to do this at first lol
		dJ1 = 0;
		for (int j = 0; j < n; j++) {		//for each set of inputs
			for (int i = 0; i < m; i++) {
				tDif = (sumOf(thetaX(x[i], theta)) - y[i]) * x[i][j];//equation to find step
				//System.out.println(tDif);
				tSum += tDif;				//length of variable two
			}
			dJ1 = tSum / n;					//take the average
			theta[j] -= alpha[j] * dJ1;		//update linear regression vector
			count++;
			//theta[0] = 1;
			//System.out.println("Step "+count+". y = "+displayVar(theta[0])+"X0 + "+displayVar(theta[1])+"X1 + "+displayVar(theta[2])+"X2 + "+displayVar(theta[3])+"X3 + "+displayVar(theta[4])+"X4 + "+displayVar(theta[5])+"X5. Fitness: "+J());
		}
		
	}
	//multiply two vector matrices
	public static double[] thetaX(double[] firstM, double[] secondM) {
		double[] result = new double[firstM.length];
		for (int i = 0; i < firstM.length; i++) {
			result[i] += firstM[i] * secondM[i];
		}
		return result;
	}

	//cost function
	public static double J() {
		double tSum = 0;				//used to sum all of the differences
		double tDif = 0;				//used to temporarily hold the value of the differences so it can be squared
		for (int j = 0; j < n; j++) {
			tDif = h(x[j]) - y[j];		//equation to find fitness of the equation
			tSum += Math.pow(tDif, 2);	//summation of expression
		}
		return tSum / 2 / n;			//arbitrary fitness expression
	}
	//hypothesis function
	public static double h(double[] xi) {
		double fitness = 0;
		for (int i = 0; i < n; i++) {
			fitness += theta[i] * xi[i];
		}
		return fitness;
	}

	
	
	public static void setTrainingData() {
		Random rand = new Random();
		for (int i = 0; i < m; i++) {
			x[i][0] = 1;
			x[i][1] = 60 * (i+5) + Math.random() * 100;				//size in square feet
			x[i][2] = rand.nextInt(3)+2 + Math.round(i / 25);		//number of bedrooms
			x[i][3] = rand.nextInt(2)+1 + Math.round(i/50);			//number of bathrooms
			x[i][4] = rand.nextInt(3)+3 - Math.round(2 - i / 50);	//number of floors
			x[i][5] = rand.nextInt(30) + rand.nextInt(30);			//age of house
			y[i] = Math.random()*10 + x[i][1]/2 + x[i][2]*20 + x[i][3]*25 + x[i][4]*30 - x[i][5]*5;
			if (y[1] < 100)
				y[i] = 80 + Math.random()*50;
		}
		theta[0] = 1;
		theta[1] = Math.random();
		theta[2] = Math.random() * 10 + Math.random() * 10 + 10;
		theta[3] = Math.random() * 10 + Math.random() * 10 + 15;
		theta[4] = Math.random() * 10 + Math.random() * 10 + 20;
		theta[5] = Math.random() * 5  + Math.random() * 5  - 10;
	}
	public static void printTrainingData() {
		System.out.print("Number:\t\t");
		for (int i = 0; i < m; i++) {
			System.out.print((i+1)+"  \t\t");
		}
		System.out.print("\nSize:\t\t");
		for (int i = 0; i < m; i++) {
			System.out.print(displayVar(x[i][1])+"   \t");
		}
		System.out.print("\nBedrooms:\t");
		for (int i = 0; i < m; i++) {
			System.out.print(x[i][2]+"     \t");
		}
		System.out.print("\nBathrooms:\t");
		for (int i = 0; i < m; i++) {
			System.out.print(x[i][3]+"     \t");
		}
		System.out.print("\nFloors:\t\t");
		for (int i = 0; i < m; i++) {
			System.out.print(x[i][4]+"     \t");
		}
		System.out.print("\nAge:\t\t");
		for (int i = 0; i < m; i++) {
			System.out.print(x[i][5]+"     \t");
		}
		System.out.print("\nPrice:\t\t");
		for (int i = 0; i < m; i++) {
			System.out.print(displayVar(y[i])+"   \t");
		}
	}
	
	
	//reduce to sig figs
	public static double displayVar(double toMod) {
		double displayM = 0;
		displayM = toMod;
		displayM = Math.round(displayM * 1000);
		return displayM / 1000;
	}
	//add two vector matrices
	public static double sumOf(double[] vector) {
		double total = 0;
		for (int i = 0; i < vector.length; i++) {
			total += vector[i];
		}
		return total;
	}
	//set modifier variables
	public static void defAlpha() {
		for (int i = 0; i < n; i++)
			alpha[i] = 0.000000008;
	}
	
	
	public static void newHouse() {
		Scanner reader = new Scanner(System.in);
		/*System.out.println("\nStep "+count+". y = "+displayVar(theta[0])+"X0 + "
							+displayVar(theta[1])+"X1 + "+displayVar(theta[2])+"X2 + "
							+displayVar(theta[3])+"X3 + "+displayVar(theta[4])+"X4 + "
							+displayVar(theta[5])+"X5. Fitness: "+displayVar(J()));
*/
		System.out.println("\n\nEnter a new house:\nSize: ");
		double size = reader.nextDouble();
		System.out.println("Number of bedrooms: ");
		double beds = reader.nextDouble();
		System.out.println("Number of bathrooms: ");
		double baths = reader.nextDouble();
		System.out.println("Number of floors: ");
		double floors = reader.nextDouble();
		System.out.println("Age of the house: ");
		double age = reader.nextDouble();
		
		double[] newHouseInfo = new double[n];
		newHouseInfo[0] = 1;
		newHouseInfo[1] = size;
		newHouseInfo[2] = beds;
		newHouseInfo[3] = baths;
		newHouseInfo[4] = floors;
		newHouseInfo[5] = age;
		double price = sumOf(thetaX(theta, newHouseInfo));
		System.out.println("The price of this house should be "+displayVar(price)+" thousand dollars.");
		}
	}

