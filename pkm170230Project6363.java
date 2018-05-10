
package pkm170230;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class pkm170230Project6363 {	
    static int VERBOSE = 0;
    static int profits[][];
    static class Jewel {
	public int weight, profit, min, max, fine, cap;
	Jewel(int w, int p, int n, int x, int f, int c) {
	    weight = w;  profit = p;  min = n;  max = x;  fine = f;  cap = c;
	}
	
	public String toString() { return weight + " " + profit + " " + min + " " + max + " " + fine + " " + cap; }
    }
    

    static class Pair {
	public int p, n;
	Pair(int p, int n) {
	    this.p = p;  this.n = n;
	}

	public String toString() { return p + " " + n; }
    }
    
    public static void enumerateAnswers(int [] sols, int i, int g, Jewel[] items)
	{
		if(i==0)
		{
			
			for(int j=1;j<sols.length;j++)
			{
				if(j == sols.length - 1) {
					System.out.print(sols[j]);
				}else {
					System.out.print(sols[j]+" ");
				}
			}
			System.out.println();
		}
		else
		{
			for(int j=0; Math.min(g- j*items[i].weight, items[i].max - j)>=0 ;j++)
			{
				int prevFine=0;
				if(j<items[i].min)
				{
					prevFine = Math.min(items[i].fine*(items[i].min-j),items[i].cap);
					//System.out.println("entered here");
				}		
				
				if(profits[i][g]== profits[i-1][g-j*items[i].weight] + j*items[i].profit - prevFine)
				{
					sols[i]=j;
					enumerateAnswers(sols,i-1,g-j*items[i].weight, items);
				}
			}
		}
	}
    

    public static Pair process(int G, Jewel[] items, int n) {
	//	
    	
		
		//r[0]=0;
		//int temp;
		profits= new int[n+1][G+1];
		int [][] NoOfSols = new int [n+1][G+1];
		for(int i = 0; i<= G; i++) {
			 NoOfSols[0][i] = 1;
		 }
		for(int i=1;i<n+1;i++)
		{
			profits[i][0] = profits[i-1][0] - Math.min(items[i].fine*items[i].min,items[i].cap);
			NoOfSols[i][0] = 1;
		}
		
		for(int i = 1 ;i <= n;i++)
		{
			for(int j = 1; j<=G; j++)
			{
				profits[i][j]=Integer.MIN_VALUE;			
				for(int quantity = 0; quantity <= items[i].max ; quantity++ )//quantity
				{

					int profit= 0;//=Integer.MIN_VALUE;
					if(j-(quantity*items[i].weight)<0)
					{
						//System.out.println("breaking");
						break;
					}
					if(quantity<items[i].min)
					{
						profit = (items[i].profit*quantity) + profits[i-1][j-(quantity*items[i].weight)]- 
								Math.min(items[i].fine*(items[i].min-quantity),items[i].cap);
						//System.out.println("entered here");
					}
					else
					{
						
//						System.out.println("fine total "+prevFine);						
						profit = (items[i].profit*quantity) + profits[i-1][j-(quantity*items[i].weight)];
					}
					if(profit >= profits[i][j]) {
						
						if(profit > profits[i][j])
						{
							NoOfSols[i][j] =  NoOfSols[i-1][j-(quantity*items[i].weight)];
						}
						else
						{
							NoOfSols[i][j] = NoOfSols[i][j] + NoOfSols[i-1][j-(quantity*items[i].weight)];
							//System.out.println("NoOfSols[i][j] = NoOfSols[i][j] + NoOfSols[i-1][j-(quantity*items[i].weight)];"+NoOfSols[i][j]);
						}
						profits[i][j]=profit;
					}
					//System.out.println("Profit at quantity :"+ quantity +" item i: "+ i +" gram of gold j is "+j+" now profit is:" + profit);
					//System.out.println(profit);
				}

			}
		}	
//		System.out.println("Profit Matrix");
//		for(int i = 1 ;i <= n;i++)
//		{
//			for(int j = 0; j<=G; j++)
//			{
//				System.out.print(profits[i][j]+" ");
//			}
//			System.out.println();
//		}
//
//		for(int i = 1 ;i <= n;i++)
//		{
//			for(int j = 0; j<=G; j++)
//			{
//				System.out.print(NoOfSols[i][j]+" ");
//			}
//			System.out.println();
//		}		
		
	return new Pair(profits[n][G], NoOfSols[n][G]);
    }
    
    public static void main(String[] args) throws FileNotFoundException {
	Scanner in;
	if(args.length == 0 || args[0].equals("-")) {
	    in = new Scanner(System.in);
	} else {		
	    File inputFile = new File(args[0]);
	    in = new Scanner(inputFile);
	}
	if (args.length > 1) {
	    VERBOSE = Integer.parseInt(args[1]);
	}
	int G = in.nextInt();
	int n = in.nextInt();
	Jewel[] items = new Jewel[n+1];
	for(int i=0; i<n; i++) {
	    int index = in.nextInt();
	    int weight = in.nextInt();
	    int profit = in.nextInt();
	    int min = in.nextInt();
	    int max = in.nextInt();
	    int fine = in.nextInt();
	    int cap = in.nextInt();
	    items[index] = new Jewel(weight, profit, min, max, fine, cap);
	    if(VERBOSE > 0) { System.out.println(index + " " + items[index]); }
	}
	long startTime = System.nanoTime();
	Pair answer = process(G, items, n);
	long endTime   = System.nanoTime();
	long totalTime = endTime - startTime;
	System.out.println("# Output:");
	System.out.println(answer);
	System.out.println("totalTime: "+totalTime+" nanoseconds");
	if(VERBOSE > 0) {
	int [] answers = new int[n+1];	
	System.out.println("# List of optimal solutions:");
		enumerateAnswers(answers, n, G, items);	
	}
    }
}