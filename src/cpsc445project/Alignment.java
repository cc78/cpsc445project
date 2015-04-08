package cpsc445project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cpsc445project.ListMatrix;

public class Alignment {

	public static void main(String[] args) {
		
		String textFileName;
		String patternFileName;
		
		if (args.length < 2) {
			System.err.println("Usage: file1 file2 (text, pattern)");
			System.exit(1);
		}
		
		textFileName = args[0];
		patternFileName = args[1];

		String text = readFASTA(textFileName);
		if (text == null) {
			System.exit(1);
		}
		
		String pattern = readFASTA(patternFileName);
		if (pattern == null) {
			System.exit(1);
		}
		
		BWTIndexBuilder builder = new SimpleBWTIndexBuilder();
		List<Character> alphabet = new ArrayList<Character>();
		/*alphabet.add('\0');
		alphabet.add('a');
		alphabet.add('c');
		alphabet.add('t');
		alphabet.add('g');*/
		alphabet.add('\0');
		alphabet.add('A');
		alphabet.add('C');
		alphabet.add('T');
		alphabet.add('G');
		
		//String reversedString = new StringBuilder("gacaca").reverse().toString();
		String reversedString = new StringBuilder(text).reverse().toString();
		//Build the BWT for the reverse of the text instead of the text
		BWTIndex rbwt = builder.build(reversedString, alphabet);
		//pattern = "caca";
		
		Alignment a = new Alignment(rbwt, pattern);
		double result = a.computeAlignment();
		System.out.println(result);  // DEBUG
	}

	
	final static double negInf = Double.NEGATIVE_INFINITY;
	
	final static double d = 1; //g
	final static double e = 1; //s
	final static ScoringMatrix scores = new ScoringMatrix();
	
	BWTIndex rbwt;
	String pattern;
	ListMatrix N;
	ListMatrix N1;
	ListMatrix N2;
	ListMatrix N3;
	
	public Alignment(BWTIndex bwt, String pattern) {
		this.rbwt = bwt;
		this.pattern = pattern;
		this.N1 = new ListMatrix();
		this.N2 = new ListMatrix();
		this.N3 = new ListMatrix();
		this.N = new ListMatrix();
	}	
		
	public double computeAlignment() {

		int n = rbwt.size();
		int depth = 0;
		double bestScore = 0;
		
		//Using 0 indexing, where 0 = first character in string
						
		Stack<Character> curString = new Stack<Character>();
		
		N.set(0, 0, 0);	
		N1.set(0, 0, 0);
		
		for (int i=1; i<=n; i++) {
			N2.set(i,0, -(d + (i-1)*e));
			N.set(i, 0, N2.get(i, 0));
		}
		
		//Don't penalize for gap at the beginning of the pattern
		for (int j=1; j<=pattern.length(); j++) {
//			N3.set(0, j, -(d + (j-1)*e));
			N3.set(0, j, 0);
			N.set(0, j, N3.get(0, j));
		}		
		
		Stack<StackItem> stack = new Stack<StackItem>();
		stack.push(new StackItem(0, n-1, ' ', 0));
		
		AlignmentResult substringAlignment;
		SequenceAlignment bestAlignment;
		while (!stack.empty()) {
			StackItem item = stack.pop();
			depth = item.depth;
			while (curString.size() > depth ) {
				curString.pop();
			}
			
			curString.push(item.z);
			//System.out.println(curString);
			//Don't bother if this is the end of the string or if deeper than 2*pattern-length bound
			if (item.z != '\0' && !(item.depth > pattern.length()*2)) { 
				//align pattern with current prefix
				if (depth > 0) {
					substringAlignment = localAlignment(depth, item.z);
					if (substringAlignment.getScore() > bestScore) {
						bestScore = substringAlignment.getScore();
						System.out.println(bestScore);
						/* traceback */
						String text = stackToString(curString);
						bestAlignment = traceback(text, pattern, substringAlignment.getTextIndex(),
								substringAlignment.getPatternIndex(), d, e, N);
						bestAlignment.setScore(bestScore);
						System.out.println(bestAlignment);
					} else if (substringAlignment.getScore() <= 0) {
						continue;
					}
				}
				
				for (Character c : rbwt.getAlphabet()) {
					//given the SA range of the current node, push on the min SA of its children
					//do edge check
					
					int[] newRange = rbwt.getSuffixRange(item.sa_left, item.sa_right, c);
					if (newRange[0] <= newRange[1]) {
						stack.push(new StackItem(newRange[0], newRange[1], c, depth+1));
					}
				}
			} 
		}
		return bestScore;
	}
	
	private AlignmentResult localAlignment(Integer i, char c){
		double n1;
		double n2;
		double n3;
		AlignmentResult res = new AlignmentResult();
		
		for (int j=1; j<=pattern.length(); j++) {
		    //N1
			n1 = N.get(i-1, j-1) + scores.getScore(c,pattern.charAt(j-1));
		    N1.set(i, j, n1);
		    
		    //N2
		    n2 = max( N2.get(i-1, j)-e, N1.get(i-1, j)- d);
		    N2.set(i, j, n2);
   
		    
		    //N3
		    n3 = max( N3.get(i, j-1)-e, N1.get(i, j-1)-d);
		    N3.set(i, j, n3);		    

		    double bestval = max(N1.get(i,j), N2.get(i,j), N3.get(i,j));
		    if (bestval > res.getScore()) {
		    	res.setScore(bestval);
		    	res.setTextIndex(i);
		    	res.setPatternIndex(j);
		    }
		    N.set(i, j, bestval);
		}
		return res;
	}
	
	private SequenceAlignment traceback(String text, String pattern, int i, int j, double d, double e, ListMatrix N) {
		int length = text.length();
		Stack<Character> alignedPattern = new Stack<Character>();
		Stack<Character> alignedText = new Stack<Character>();
		
		int k = 1;
		while (i > 0 && j > 0) {
			char t = text.charAt(i);
			char p = pattern.charAt(j-1);
			//System.out.println(t + " " + p);
			if (N.get(i, j) == N.get(i - 1, j - 1) + scores.getScore(t, p)) {
				alignedPattern.push(p);
				alignedText.push(t);
				i--;
				j--;
			
			} else if (N.get(i, j) == N.get(i - 1, j) - d || N.get(i, j) == N.get(i - 1, j) - e) {
				alignedPattern.push('-');
				alignedText.push(t);
				i--;
			
			} else if (N.get(i, j) == N.get(i, j - 1) - d || N.get(i, j) == N.get(i, j - 1) - e) {
				alignedPattern.push(p);
				alignedText.push('-');
				j--;
				
			} else {
				System.err.println("Error during traceback");
				System.exit(1);
			}
			k++;
		}
		
		return new SequenceAlignment(0.0, stackToString(alignedPattern), stackToString(alignedText));
	}
	
	private static double max(double... vals) {
		double max = negInf;

		for (double d : vals) {
			if (d > max)
				max = d;
		}
		return max;
	}
	
	private int indexOfMax(double[] vals) {
		int index = 0;
		double max = vals[0];
		
		for (int i = 1; i < vals.length; i++) {
			if (vals[i] > max)
				max = vals[i];
				index = i;
		}
		return index;
	}
	
	// This is pretty awful
	private String stackToString(Stack<Character> stack) {
		int size = stack.size();
		Stack<Character> stackCopy = (Stack<Character>) stack.clone();
		char[] arr = new char[size];
		for (int i = 0; i < size; i++) {
			arr[size - i - 1] = stackCopy.pop();
		}
		return String.valueOf(arr);
	}
	
	/* 
	 * Attempt to read file. Assume FASTA format. Only read first sequence if file contains multiple.
	 */
	private static String readFASTA(String fileName) {
		String text;
		StringBuilder strBuilder = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line = reader.readLine();
			
			while (!line.startsWith(">") && line != null) {
				line = reader.readLine();
			}
			if (line == null) {
				System.err.println("Reached end of file without encountering sequence.");
				System.exit(1);
			}
			/* read the sequence */
			while ((line = reader.readLine()) != null && !line.startsWith(">")) {
				strBuilder.append(line);
			}
			
			text = strBuilder.toString();
			
		} catch (IOException e) {
			System.err.println(e);
			return null;
		}
		
		return text;
	}
	
}
