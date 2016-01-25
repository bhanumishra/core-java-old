package me.example.ds;

public class Stack {

	private int top = 0;
	private int size = 0;
	private int[] stack;

	public Stack() {
		this(10);
	}

	public Stack(final int size) {
		this.size = size;
		stack = new int[this.size];
		System.out.println("Stack with size " + stack.length + " created.");
	}

	public void display() {
		if (top > 0) {
			System.out.println("Values in stack are:");

			for (int i = top - 1; i >= 0; i--) {
				System.out.printf("%d\n", stack[i]);
			}
			System.out.println();
		} else {
			System.out.println("stack empty. nothing to display.\n");
		}
	}

	/* pop: pop and return top value from stack */
	public int pop() {
		if (top > 0) {
			return stack[--top];
		} else {
			System.err.printf("error: stack empty. nothing to pop.\n");
			return -1;
		}
	}

	/* push: push f onto value stack */
	public void push(final int val) {
		if (top < size) {
			stack[top++] = val;
		} else {
			System.err.printf("error: stack full. can't push %d.\n", val);
		}
	}
}