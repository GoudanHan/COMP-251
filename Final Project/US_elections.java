import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
 
public class US_elections {
 
	public static int solution(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided){
		int need;
		int delegates_sum = 0;
		int Trump=0;
		
		// since we don't know exactly how many states are still undecided,
		// thus we need to use arraylist to dynamically change the size
		// vote stores the minimum undecided votes biden need to win this state
		// delegate stores the delegates of this state
		ArrayList<Integer> vote = new ArrayList<Integer>();
		ArrayList<Integer> delegate = new ArrayList<Integer>();
		
		for (int i = 0; i < num_states; i++) {
			
			// biden needs this many votes to win this state
			need=(votes_Biden[i]+votes_Trump[i]+votes_Undecided[i])/2+1; 
			
			// even if biden wins all the undecided votes, he still cannot beat trump
			// in this case, trump wins all the delegates in this state
			if (votes_Biden[i]+votes_Undecided[i]<need) {
				Trump += delegates[i];
			}
			
			// if biden still has a chance to win this state
			// update the variables vote and delegate
			else {
				if (Math.max(0, need-votes_Biden[i])>0) {
					vote.add(need-votes_Biden[i]);
					delegate.add(delegates[i]);
				}
			}
			
			// all the delegates in all states
			delegates_sum += delegates[i];
		}
		
		// since now we know the size, we can change the arraylists into arrays
		// to make it more convenient for the upcoming dynamic programming part
		int[] value = new int[vote.size()]; 
		int[] weight = new int[delegate.size()]; 
		int votes_max = 0;
		for(int i = 0; i<vote.size();i++) {
			value[i]= vote.get(i);
			weight[i]=delegate.get(i);
			votes_max += value[i];
		}
 
		// maximum delegates biden can lose to win this election
		int limit;
		if (delegates_sum%2==0) {
			limit = delegates_sum/2 - Trump - 1;
		}
		else {
			limit = delegates_sum/2 - Trump;
		}
		
		// if the limit<0, it means biden doesn't have a chance to win
		if (limit<0) {
			return -1;
		}
		
		// the dynamic programming part
		// source: knapsack problem in course slides
		int bag[][] = new int [value.length + 1][limit + 1];
		for (int i = 0; i <= value.length; i++) {
			for (int w = 0; w <= limit; w++) {
				if (i==0 || w==0) {
					bag[i][w]=0;}
				else if(weight [i-1] <= w) {
					bag[i][w]= Math.max(bag[i - 1][w], value[i-1]+bag[i - 1][w - weight[i - 1]]);
				}else {
					bag[i][w]=bag[i-1][w];
				}
			}
		}
	
		return votes_max - bag[value.length][limit];
	}
	
	
	public static void main(String[] args) {
		 try {
				String path = args[0];
	      File myFile = new File(path);
	      Scanner sc = new Scanner(myFile);
	      int num_states = sc.nextInt();
	      int[] delegates = new int[num_states];
	      int[] votes_Biden = new int[num_states];
	      int[] votes_Trump = new int[num_states];
	 			int[] votes_Undecided = new int[num_states];	
	      for (int state = 0; state<num_states; state++){
				  delegates[state] =sc.nextInt();
					votes_Biden[state] = sc.nextInt();
					votes_Trump[state] = sc.nextInt();
					votes_Undecided[state] = sc.nextInt();
	      }
	      sc.close();
	      int answer = solution(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided);
	      	System.out.println(answer);
	    	} catch (FileNotFoundException e) {
	      	System.out.println("An error occurred.");
	      	e.printStackTrace();
	    	}
	  	}
 
}