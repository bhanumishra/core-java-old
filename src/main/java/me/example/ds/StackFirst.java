class StackFirst {
	
	private int[] list;
	private int top = -1;
		
	public StackFirst() {
		list = new int[10];
	}
	
	public void push(int value) {
		list[++top] = value;
	}
	
	public int pop() {
		return list[top--];
	}
	
	/*public static void main(String [] args) {
		
		StackFirst stack = new StackFirst();
		
		stack.push(10);
		System.out.print("Popped from stack: " + stack.pop());
	}*/
}