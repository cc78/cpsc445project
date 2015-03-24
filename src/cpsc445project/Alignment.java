package cpsc445project;

import cpsc445project.ListMatrix;

public class Alignment {

	public static void main(String[] args) {
		BWTIndexBuilder builder = new SimpleBWTIndexBuilder();
		BWTIndex bwt = builder.build("actg");
		computeAlignment(bwt, "at");
	}
	
	final static double negInf = Double.NEGATIVE_INFINITY;
	
	final static double d = 5; //g
	final static double e = 5; //s
	final static ScoringMatrix scores = new ScoringMatrix();
		
	public static void computeAlignment(BWTIndex bwt, String pattern) {

		int n = bwt.size();
		int m = pattern.length();
		ListMatrix N1 = new ListMatrix();
		ListMatrix N2 = new ListMatrix();
		ListMatrix N3 = new ListMatrix();
		ListMatrix N = new ListMatrix();

		//Using 0 indexing, where 0 = first character in string
		for (int j=-1; j<=m; j++) {
			N.set(0,j, 0);
		}
		
		for (int i=0; i<=n; i++) {
			N1.set(i,0,-(d + i * e));
		}		
				
		double n1;
		double n2;
		double n3;
		
		for (int i=1; i<=n; i++) {
			for (int j=1; j<=m; j++) {
				
			    //N1
			    if (N.get(i-1, j-1) > 0) {
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
