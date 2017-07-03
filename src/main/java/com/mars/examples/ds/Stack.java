package com.mars.examples.ds;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Stack {
	Logger logger = Logger.getLogger("Stack");

	private int top = 0;
	private int size = 0;
	private int[] stack;

	public Stack() {
		this(10);
	}

	public Stack(final int size) {
		this.size = size;
		stack = new int[this.size];
		logger.log(Level.INFO, "Stack with size " + stack.length + " created.");
	}

	public void display() {
		if (top > 0) {
			System.out.println("Values in stack are:");

			for (int i = top - 1; i >= 0; i--) {
				System.out.printf("%d\n", stack[i]);
			}
			System.out.println();
		} else {
			logger.warning("stack empty. nothing to display.");
		}
	}

	/* pop: pop and return top value from stack */
	public int pop() {
		if (top > 0) {
			return stack[--top];
		} else {
			logger.warning("error: stack empty. nothing to pop.\n");
			return -1;
		}
	}

	/* push: push f onto value stack */
	public void push(final int val) {
		if (top < size) {
			stack[top++] = val;
		} else {
			logger.warning(String.format("error: stack full. can't push %d.\n", val));
		}
	}
}