package cpsc445project;

import java.util.Comparator;

public class AlignmentResult {
	
	private int textIndex;
	private int patternIndex;
	private double score;
	
	public int getTextIndex() {
		return textIndex;
	}
	
	public void setTextIndex(int i) {
		this.textIndex = i;
	}
	
	public int getPatternIndex() {
		return patternIndex;
	}
	
	public void setPatternIndex(int j) {
		this.patternIndex = j;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public static class AlignmentResultComparator implements Comparator<AlignmentResult> {
		
		@Override
		public int compare(AlignmentResult a1, AlignmentResult a2) {
			if (a1.getScore() < a2.getScore()) {
				return -1;
			}
			
			if (a1.getScore() > a2.getScore()) {
				return 1;
			}
			
			return 0;
		}
	}

}
