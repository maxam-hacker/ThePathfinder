package com.maxamhacker.thepathfinder.graph.layout;

import java.util.LinkedHashMap;
import java.util.Iterator;

import com.maxamhacker.thepathfinder.graph.Graph;
import com.maxamhacker.thepathfinder.graph.nodes.CallNode;
import com.maxamhacker.thepathfinder.graph.nodes.MethodNode;
import com.maxamhacker.thepathfinder.graph.nodes.Visitor;

public class LayeredLayout implements Layout {
	
	public static int weightBetweenLayers = 120;
	
	public static int heightBetweenClusters = 80;
	
	public static int heightBetweenMethodsInCluster = 60;
	
	public static int heightBetweenCalls = 40;
	public static int heightBetweenTopBorderAndFirstCall = 40;
	public static int heightBetweenBottomBorderAndLastCall = 20;
	public static int widthBetweenLeftBorderAndCall = 40;
	public static int widthBetweenRightBorderAndCall = 0;
	
	private static LinkedHashMap<Integer, Layer> layers = new LinkedHashMap<>();
	
	public class WidthAndHeightBuilder extends Visitor {
		
		public void visit(MethodNode node) {	
			Iterator<CallNode> callsWalker = node.calls.iterator();
			while (callsWalker.hasNext()) {
				CallNode call = callsWalker.next();
				call.calculateWidthAndHeight();
				node.height += call.height;
				if (callsWalker.hasNext())
					node.height += heightBetweenCalls;
				if (node.width < call.width)
					node.width = call.width;
			}
			node.height += heightBetweenTopBorderAndFirstCall + heightBetweenBottomBorderAndLastCall;
			node.width += widthBetweenLeftBorderAndCall + widthBetweenRightBorderAndCall;
		}
		
	}
	
	public class LayersBuilder extends Visitor {
		
		public void visit(MethodNode node) {
			node.layer = (node.parent == null) ? 1 : node.parent.layer + 1;
			if (layers.get(node.layer) == null)
				layers.put(node.layer, new Layer(node.layer, layers.get(node.layer - 1)));
			layers.get(node.layer).addNode(node);
		}
		
	}
	
	public void apply(Graph graph) {
		graph.getRoot().accept(new WidthAndHeightBuilder());
		layers.clear();
		graph.getRoot().accept(new LayersBuilder());
		layers.get(layers.size()).makeLastLayout();
		for (int idx = layers.size() - 1; idx >= 1; idx --)
			layers.get(idx).makeLayout();
		layers.forEach((id, layer) -> {
			System.out.println(layer.toString());
		});
	}
	
	
}
