package com.maxamhacker.thepathfinder.graph.nodes;

public class CallNode extends Node {
	
	public CallNode(int id, String name) {
		super(id, name);
	}

	public void calculateWidthAndHeight() {
		width = 120;
		height = 40;
	}
	
}
