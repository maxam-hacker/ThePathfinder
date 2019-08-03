package com.maxamhacker.thepathfinder.graph;

import com.maxamhacker.thepathfinder.graph.layout.Layout;
import com.maxamhacker.thepathfinder.graph.nodes.Node;

public interface Graph {
	
	public Node getRoot();
	
	public Graph layoutCalculate(Layout layout);
	
}
