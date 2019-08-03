package com.maxamhacker.thepathfinder.graph.nodes;

import java.util.ArrayList;

public class MethodNode extends Node {

	public ArrayList<CallNode> calls = new ArrayList<CallNode>();
	
	public MethodNode(int id, String name) {
		super(id, name);
	}
	
	public void addCall(CallNode call) {
		calls.add(call);
		call.parent = this;
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		calls.forEach(node -> {
			info.append("\t\t[ call: ").append(node.getName()).append(" :: ");
			info.append("x: ").append(node.x).append(" :: ");
			info.append("y: ").append(node.y).append(" :: ");
			info.append("width: ").append(node.width).append(" :: ");
			info.append("height: ").append(node.height).append(" ]\n");
		});
		return info.toString();
	}
	
}
