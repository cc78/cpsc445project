package cpsc445project;

public class StackItem {
	public int sa_left;
	public int sa_right;
	public char z;
	public int depth;
	
	public StackItem(int sal, int sar, char z, int depth) {
		this.sa_left = sal;
		this.sa_right = sar;
		this.z = z;
		this.depth = depth;
	}	
}
