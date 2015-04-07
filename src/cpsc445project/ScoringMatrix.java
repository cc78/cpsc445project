package cpsc445project;

public class ScoringMatrix {
	private String residues = "ACTG";
	private int[][] scores = {
			
            /* A  C  T  G*/
    /* A */ {  3, 0, 0, 0 },
    /* C */ {  0, 3, 0, 0 },
    /* T */ {  0, 0, 3, 0 },
    /* G */ {  0, 0, 0, 3 },

	};
	
	public double getScore(char s1, char s2) {
		s1 = Character.toUpperCase(s1);
		s2 = Character.toUpperCase(s2);
		if (this.residues.indexOf(s1) > -1 && this.residues.indexOf(s2) > -1) {
			return (double) this.scores[this.residues.indexOf(s1)][this.residues.indexOf(s2)];
		} else {
			return Double.NEGATIVE_INFINITY;
		}
	}
}
