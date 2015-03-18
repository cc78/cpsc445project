package cpsc445project;

public class Alignment {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	final static double negInf = Double.NEGATIVE_INFINITY;
	
	final double d = 5;
	final double e = 5;
	int[][] score;
		
	public void ModifiedAlignment(String seq1, String seq2) {

		int n = seq1.length();
		int m = seq2.length();
		double[][] N1 = new double[n+1][m+1];
		double[][] N2 = new double[n+1][m+1];
		double[][] N3 = new double[n+1][m+1];
		double[][] N = new double[n+1][m+1];
		
		for (int i=1; i<=n; i++) {
			N[i][0] = -d * i;
		}
		for (int j=1; j<=m; j++) {
			N[0][j] = -d * j;
		}
		for (int i=1; i<=n; i++) {
			for (int j=1; j<=m; j++) {
			    int s = score[seq1.charAt(i-1)][seq2.charAt(j-1)];
			    double val = max(N[i-1][j-1]+s, N[i-1][j]-d, N[i][j-1]-d);
			    N[i][j] = val;
			    if (val == N[i-1][j-1]+s) {
			    				    	
			    }			    	
			    else if (val == N[i-1][j]-d) {
			    	
			    }
			    else if (val == N[i][j-1]-d) {
			    	
			    }
			    else {
			    	throw new Error("NW 1");
			    }
			}
		}
	}
	
	private double max(double... vals) {
		double max = Double.NEGATIVE_INFINITY;

		for (double d : vals) {
			if (d > max)
				max = d;
		}
		return max;
	}
}
