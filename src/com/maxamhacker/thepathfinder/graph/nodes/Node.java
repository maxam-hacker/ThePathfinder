package com.maxamhacker.thepathfinder.graph.nodes;

import java.util.Arrays;
import java.util.TreeMap;

public class Node {
	
	public int x = 0;
	public int y = 0;
	public int width = 0;
	public int height = 0;
	public int layer = 0;
	
	public final int id;
	private String name;
	private Node[] children;
	private TreeMap<Integer, Node> childrenMap = new TreeMap<>();
	public Node parent;
	private int number;
	private int capacity;
	
	public Node(int id) {
		this.id = id;
		this.number = 0;
		this.capacity = 10;
		this.children = new Node[capacity];
	}
	
	public Node(int id, String name) {
		this(id);
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addChild(Node child) {
		if (this.number == this.capacity) {
			this.capacity += 3;
			this.children = Arrays.copyOf(this.children, this.children.length);
		}
		this.children[number++] = child;
		childrenMap.put(child.id, child);
		child.parent = this;
	}
	
	public Node[] getChildren() {
		return this.children;
	}
	
	public Node getFistChild() {
		if (number == 0) return null;
		return childrenMap.firstEntry().getValue();
	}
	
	public Node getLastChild() {
		if (number == 0) return null;
		return childrenMap.lastEntry().getValue();
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public void calculateWidthAndHeight() {
		return;
	}
	
	public void accept(Visitor visitor) {
		visitor.visit(this);
		for (int idx = 0; idx < number; idx ++)
			this.children[idx].accept(visitor);
	}
	
	public String toString() {
		return  "id: " + String.valueOf(id) + " " +
				"name: " + name + " " +
				"x: " + String.valueOf(x) + " " +
				"y: " + String.valueOf(y) + " " +
				"width: " + String.valueOf(width) + " " +
				"height: " + String.valueOf(height);
	}
	
}
