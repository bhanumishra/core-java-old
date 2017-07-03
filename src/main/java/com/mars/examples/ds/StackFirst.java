package com.mars.examples.ds;

public class StackFirst {

	private int[] list;
	private int top = -1;

	public StackFirst() {
		list = new int[10];
	}

	public int pop() {
		return list[top--];
	}

	public void push(int value) {
		list[++top] = value;
	}

	/*
	 * public static void main(String [] args) {
	 *
	 * StackFirst stack = new StackFirst();
	 *
	 * stack.push(10); System.out.print("Popped from stack: " + stack.pop()); }
	 */
}