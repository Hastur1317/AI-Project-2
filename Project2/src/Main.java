
public class Main {

	public static void main(String[] args)
	{
		//largeSample();
		threeTests();
	}
	
	@SuppressWarnings("unused")
	private static void largeSample()
	{
		System.out.println("Beginning Simulated Annealing tests\nplease wait");
		int solved =0;
		int cost = 0;
		long time = 0;
		double s = 0;
		double c = 0;
		
		for(int x=0; x<150; x++)
		{
			Problem p = new Problem(21);
			p.SimulatedAnnealing();
			System.out.println("Test " + (x+1) + "\tSolved? = " + p.solved() + "\tCost = " + p.cost() + "\tTime = " + p.getTime() + "ms");
			if(p.solved())
				solved++;
			cost = cost + p.cost();
			time = time + p.getTime();
		}
		s = solved/150.0 *100.0;
		c = cost/150.0;
		time = time/150;
		System.out.println("Average Solved = " + s + "%\tAverage Cost = " + c + "\tAverage Time = " + time + "ms");
		
		System.out.println("Beginning Genetic tests\nplease wait");
		solved =0;
		cost = 0;
		time = 0;
		s = 0;
		c = 0;
		for(int x=0; x<150; x++)
		{
			Problem p = new Problem(21);
			p.genetic();;
			System.out.println("Test " + (x+1) + "\tSolved? = " + p.solved() + "\tCost = " + p.cost() + "\tTime = " + p.getTime() + "ms");
			if(p.solved())
				solved++;
			cost = cost + p.cost();
			time = time + p.getTime();
		}
		s = solved/150.0 *100.0;
		c = cost/150.0;
		time = time/150;
		System.out.println("Average Solved = " + s + "%\tAverage Cost = " + c + "\tAverage Time = " + time + "ms");
		System.out.println("End of Tests");
	}
	
	private static void threeTests()
	{
		System.out.println("Beginning Simulated Annealing tests\nplease wait");
		for(int x=0; x<3; x++)
		{
			Problem p = new Problem(21);
			p.SimulatedAnnealing();
			System.out.println("Test " + x);
			System.out.println("\n" + p.toString(true));
			System.out.println("Solved? = " + p.solved() + "\tCost = " + p.cost() + "\tTime = " + p.getTime() + "ms");
		}
		
		System.out.println("Beginning Genetic tests\nplease wait");
		for(int x=0; x<3; x++)
		{
			Problem p = new Problem(21);
			p.genetic();
			System.out.println("Test " + (x+1));
			System.out.println("\n" + p.toString(true));
			System.out.println("Solved? = " + p.solved() + "\tCost = " + p.cost() + "\tTime = " + p.getTime() + "ms");
		}
		System.out.println("End of Tests");
	}

}
