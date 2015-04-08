package cpsc445project;

public class SequenceAlignment {
	
	//private int startIndex;
	//private int endIndex;
	private double score;
	private String patternSegment;
	private String textSegment;
	
	public SequenceAlignment(double score, String patternSegment, String textSegment) {
		//this.startIndex = startIndex;
		//this.endIndex = endIndex;
		this.score = score;
		this.patternSegment = patternSegment;
		this.textSegment = textSegment;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int startIndex = 0;
		int lineLength = 80;
		
		while (startIndex < patternSegment.length()) {
			int endIndex = Math.min(startIndex + lineLength, patternSegment.length());
			builder.append(patternSegment.substring(startIndex, endIndex) + "\n" +
					textSegment.substring(startIndex, endIndex) +"\n\n");
			startIndex = endIndex;
		}
		
		return builder.toString();
	}

}
