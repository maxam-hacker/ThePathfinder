package com.maxamhacker.thepathfinder.graph.nodes;

public class Visitor {
	
	public void visit(Node node) {
		if (node instanceof CallNode)
			visit((CallNode)node);
		else if (node instanceof MethodNode)
			visit((MethodNode)node);
	}
	
	public void visit(CallNode node) {
		return;
	}
	
	public void visit(MethodNode node) {
		return;
	}

}
