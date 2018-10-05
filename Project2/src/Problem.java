import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Problem
{
	private Node initial;
	private Node solution;
	private int n;
	private int cost;
	private boolean solved;
	private int distance;
	private long time;
	
	public Problem(int n)
	{
		this.n = n;
		initial = new Node(n);
		solution = new Node(n);
		cost = 0;
		solved = false;
		for(int x=0; x<n; x++)
		{
			initial.row()[x] = x;
			initial.col()[x] = x;
			solution.row()[x] = x;
			solution.col()[x] = x;
		}
	}
	
	public void genetic()
	{
		int pop = n;
		double mutationChance = 0.2;
		double sum = 0;
		long startTime = System.currentTimeMillis();
		
		Node[] population = new Node[pop];
		int[] h = new int[pop];
		//Initial population
		population[0] = new Node(solution.row(), solution.col());
		for(int x=1; x<pop; x++)
		{
			population[x] = new Node(solution.row());
			h[x] = population[x].valueState();
			sum = sum + h[x];
		}
		
		double count = 100;
		
		while(true)
		{
			Node[] newPop = new Node[pop];
			int[] newH = new int[pop];
			float newSum = 0;
			for(int c=0; c<pop; c++)
			{
				//selection
				Node x = fitness(population, sum, h);
				Node y = fitness(population, sum, h);
				//propagation
				Node child = breed(x, y);
				//mutation
				double chance = Math.random();
				if(chance < mutationChance)
					child.mutate(); // mutate
				//add to new population
				newPop[c] = child;
				newH[c] = child.valueState();
				if(newH[c] == 0 || count <=0)
				{
					solution = child;
					if(newH[c] ==0)
						solved = true;
					distance = newH[c];
					time = System.currentTimeMillis() - startTime;
					return;
				}
				newSum = newSum + newH[c];
			}
			//new population to current
			population = newPop;
			h = newH;
			sum = newSum;
			cost++;
			count = count - 0.0001;
		}
	}
	
	private Node fitness(Node[] population, double sum, int[] h)
	{
		double chance = Math.random();
		double fit = 0;
		
		for(int x=0; x<population.length; x++)
		{
			fit = (1.0 - h[x]/sum) + fit;
			if(chance <= fit)
			{
				return population[x];
			}
		}
		//should never reach here
		return null;
	}
	
	private Node breed(Node n1, Node n2)
	{
		Node child = new Node(n);
		int cross = ThreadLocalRandom.current().nextInt(0, n);
		
		
		for(int x=0; x<n; x++)
		{
			if(x <= cross)
			{
				child.col()[x] = n1.col()[x];
			}
			else
			{
				child.col()[x] = n2.col()[x];
			}
			child.row()[x] = x;
		}
		
		return child;
	}
	
	
	public void SimulatedAnnealing()
	{
		Node curr = new Node(solution.row(), solution.col());
		Node next = new Node(solution.row(), solution.col());
		double temp = 20;
		int deltaE =0;
		int h;
		long startTime = System.currentTimeMillis();
		
		while(true)
		{
			cost++;
			h = curr.valueState();
			temp = temp - 0.0001;
			if(temp <= 0 || h ==0)
			{
				if(h ==0)
					solved = true;
				solution = curr;
				time = System.currentTimeMillis() - startTime;
				distance = h;
				return;
			}
			
			next = new Node(curr.row(), curr.col());
			next.random();
			deltaE = next.valueState() - curr.valueState();
			
			if(deltaE < 0)
			{
				curr = next;
			}
			else
			{
				if(probablity(deltaE, temp))
				{
					curr = next;
				}
			}
		}
		
	}
	
	public long getTime()
	{
		return time;
	}
	
	public int distance()
	{
		return distance;
	}

	private boolean probablity(int deltaE, double temp)
	{
		double e = -deltaE;
		double t = temp;
		double prob = Math.exp(e/t);
		double r = Math.random();
		
		if(prob > r)
		{
			return true;
		}
		else
			return false;
	}

	public String toString(boolean sol)
	{
		if(!sol)
		return initial.getState();
		return solution.getState();
	}
	public boolean solved()
	{
		return solved;
	}
	public int cost()
	{
		return cost;
	}
}

class Node
{
	private int[] row;
	private int[] col;
	private int[] pDag;
	private int[] nDag;
	
	public Node(int n)
	{
		row = new int[n];
		col = new int[n];
		pDag = new int[n];
		nDag = new int[n];
	}
	
	public void random()
	{
		int n = row.length;
		int q;
		int q2;
		do{
			q = ThreadLocalRandom.current().nextInt(0, n);
			q2 = ThreadLocalRandom.current().nextInt(0, n);
		}while(q == q2);
		
		n = col[q];
		col[q] = col[q2];
		col[q2] = n;
		
		fillDags();
	}
	
	public void mutate()
	{
		int n = row.length;
		int q;
		int q2;
		do{
			q = ThreadLocalRandom.current().nextInt(0, n);
			q2 = ThreadLocalRandom.current().nextInt(0, n);
		}while(q == q2);
		
		col[q] = q2;
		
		fillDags();
	}

	public Node(int[] row, int[] col)
	{
		int n = row.length;
		this.row = new int[n];
		this.col = new int[n];
		pDag = new int[n];
		nDag = new int[n];
		
		for(int x =0; x<row.length; x++)
		{
			this.row[x] = row[x];
			this.col[x] = col[x];
			pDag[x] = row[x] + col[x];
			nDag[x] = row[x] - col[x];
		}
	}
	
	public Node(int[] row)
	{
		int n = row.length;
		this.row = new int[n];
		this.col = new int[n];
		pDag = new int[n];
		nDag = new int[n];
		
		for(int x =0; x<row.length; x++)
		{
			this.row[x] = row[x];
			this.col[x] = ThreadLocalRandom.current().nextInt(0, n);
			pDag[x] = row[x] + this.col[x];
			nDag[x] = row[x] - this.col[x];
		}
	}
	
	public int[] row()
	{
		return row;
	}
	public int[] col()
	{
		return col;
	}
	public int[] pDag()
	{
		return pDag;
	}
	public int[] nDag()
	{
		return nDag;
	}
	
	public void fillDags()
	{
		for(int x=0; x<row.length; x++)
		{
			pDag[x] = row[x] + col[x];
			nDag[x] = row[x] - col[x];
		}
	}
	
	public String getState()
	{
		String[][] store = new String[row.length][row.length];
		for(int x =0; x<row.length; x++)
		{
			store[row[x]][col[x]] = "Q";
		}
		String out = Arrays.deepToString(store).replace("], ", "]\n");
		out = out.replace("[[", "[");
		out = out.replace("null", " ");
		out = out.replace(",", "|");
		out = out.replace("]]", "]");
		return out;
	}
	public int valueState()
	{
		int value = 0;
		
		for(int x=0; x<row.length-1; x++)
		{
			for(int y=0; y<row.length; y++)
			{
				if(x==y)
					continue;
				if(row[x] == row[y])
					value++;
				if(col[x] == col[y])
					value++;
				if(pDag[x] == pDag[y])
					value++;
				if(nDag[x] == nDag[y])
					value++;
			}
		}
		return value;
	}
}
