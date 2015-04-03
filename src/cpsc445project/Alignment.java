package cpsc445project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import cpsc445project.ListMatrix;

public class Alignment {

	public static void main(String[] args) {
		BWTIndexBuilder builder = new SimpleBWTIndexBuilder();
		List<Character> alphabet = new ArrayList<Character>();
		alphabet.add('a');
		alphabet.add('c');
		alphabet.add('t');
		alphabet.add('g');
		BWTIndex bwt = builder.build("actg", alphabet);
		BWTIndex rbwt = builder.build("gtca", alphabet);
		
		System.out.println(bwt.getAlphabet());
		Alignment a = new Alignment(bwt, "at");
		a.computeAlignment(rbwt);
	}

	
	final static double negInf = Double.NEGATIVE_INFINITY;
	
	final static double d = 5; //g
	final static double e = 5; //s
	final static ScoringMatrix scores = new ScoringMatrix();
	
	BWTIndex bwt;
	String pattern;
	ListMatrix N;
	ListMatrix N1;
	ListMatrix N2;
	ListMatrix N3;
	List<Character> alphabet;
	
	public Alignment(BWTIndex bwt, String pattern) {
		this.bwt = bwt;
		this.pattern = pattern;
		this.N1 = new ListMatrix();
		this.N2 = new ListMatrix();
		this.N3 = new ListMatrix();
		this.N = new ListMatrix();
	}	
		
	public void computeAlignment(BWTIndex rbwt) {

		int n = bwt.size();
		int sa_left;
		int sa_right;

		//Using 0 indexing, where 0 = first character in string
						
		int depth = 0;
		char[] curString = new char[n];
		curString[0] = '\0';
		
		for (int j=0; j<=pattern.length(); j++) {
				N.set(0,j, 0);
			}
			
//		for (int i=0; i<=n; i++) {
//				N1.set(i,0,-(d + i * e));
//			}
		
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(1);
		
		while (!stack.empty()) {
			int i = stack.pop();
			//align pattern with current prefix
//			localAlignment(i);
			boolean isUp = true;
			for (Character c : bwt.getAlphabet()) {
				//given the SA range of the current node, push on the min SA of its children
				//do edge check
				if (rbwt.isSuffix(i, curString, c)) {
					System.out.println(c);
					stack.push(i);
					isUp = false;
				}
			}
			if (isUp) {
				depth = depth - 1;
			} else {
				depth = depth + 1;
			}
			
		}
	}
	
	private void localAlignment(Integer i){
		double n1;
		double n2;
		double n3;
		for (int j=1; j<=pattern.length(); j++) {
			
		    //N1
		    if ((N.get(i-1, j-1) > 0) || (i == 0)) {
		    	n1 = N.get(i-1, j-1) + scores.getScore(bwt.get(i-1),pattern.charAt(j-1));
		    } else {
		    	n1 = negInf;
		    }
		    if (n1 > 0) {
		    	N1.set(i, j, n1);
		    }
		    
		    
		    //N2
		    if (N2.get(i-1, j) > 0 && N.get(i-1, j) > 0) {
		    	n2 = max( N2.get(i-1, j)-e, N.get(i-1, j)-(d+e) );
		    } else if (N2.get(i-1, j) > 0) {
		    	n2 = N2.get(i-1, j) - e;
		    } else if (N.get(i-1, j) > 0) {
		    	n2 = N.get(i-1, j) - (d+e);
		    } else {
		    	n2 = negInf;
		    }
		    if (n2 > 0) {
		    	N2.set(i, j, n2);
		    }			    
		    
		    //N3
		    if (N3.get(i, j-1) > 0 && N.get(i, j-1) > 0) {
		    	n3 = max( N3.get(i, j-1)-e, N.get(i, j-1)-(d+e) );
		    } else if (N3.get(i, j-1) > 0) {
		    	n3 = N3.get(i, j-1) - e;
		    } else if (N.get(i, j-1) > 0) {
		    	n3 = N.get(i, j-1) - (d+e);
		    } else {
		    	n3 = negInf;
		    }
		    if (n3 > 0) {
		    	N3.set(i, j, n3);
		    }			    

		    double bestval = max(N1.get(i,j), N2.get(i,j), N3.get(i,j));
		    System.out.println(bestval);
		    N.set(i, j, bestval);
		}		
	}
	
	private static double max(double... vals) {
		double max = negInf;

		for (double d : vals) {
			if (d > max)
				max = d;
		}
		return max;
	}
}
