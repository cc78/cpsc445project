package cpsc445project;

import java.util.Comparator;

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
	
	public double getScore() {
		return this.score;
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
	
	public static class SequenceAlignmentComparator implements Comparator<SequenceAlignment> {
		
		@Override
		public int compare(SequenceAlignment a1, SequenceAlignment a2) {
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
