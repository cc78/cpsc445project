package cpsc445project;

public class SequenceAlignment {
	
	private int startIndex;
	private int endIndex;
	private double score;
	private String patternSegment;
	private String textSegment;
	
	public SequenceAlignment(int startIndex, int endIndex, double score, String patternSegment, String textSegment) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.score = score;
		this.patternSegment = patternSegment;
		this.textSegment = textSegment;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return null;  // TODO
	}

}
