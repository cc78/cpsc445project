package cpsc445project;

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

}
