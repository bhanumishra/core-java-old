class Stack {
	
	private int[] list;
	private int top = -1;
		
	public Stack() {
		list = new int[10];
	}
	
	public void push(int value) {
		list[++top] = value;
	}
	
	public int pop() {
		return list[top--];
	}
	
	/*public static void main(String [] args) {
		
		Stack stack = new Stack();
		
		stack.push(10);
		System.out.print("Popped from stack: " + stack.pop());
	}*/
}