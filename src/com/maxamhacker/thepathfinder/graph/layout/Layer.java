package com.maxamhacker.thepathfinder.graph.layout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import com.maxamhacker.thepathfinder.graph.nodes.MethodNode;
import com.maxamhacker.thepathfinder.graph.nodes.Node;
import com.maxamhacker.thepathfinder.graph.nodes.Visitor;

public class Layer {
	
	public final int id;
	public final Layer parent;
	public final TreeMap<Integer, Node> nodes = new TreeMap<>();
	
	public Layer(int id, Layer parent) {
		this.id = id;
		this.parent = parent;
	}
	
	public void addNode(Node node) {
		nodes.put(node.id, node);
	}
	
	public void makeLastLayout() {
		nodes.forEach(new BiConsumer<Integer, Node>() {
			private int baseY = 0;
			public void accept(Integer id, Node node) {
				node.x = 0;
				node.y = baseY;
				baseY -= node.height + LayeredLayout.heightBetweenMethodsInCluster;
			}
		});
	}
	
	public void makeLayout() {
		centerParentsByChildren();
		checkParentsAndShiftSubTrees();
	}
	
	public void centerParentsByChildren() {
		nodes.forEach(new BiConsumer<Integer, Node>() {
			
			public void accept(Integer id, Node node) {
				Node first = node.getFistChild();
				Node last = node.getLastChild();
				if (first != null && last != null) {
					int top = first.y;
					int bottom = last.y - last.height;
					node.x = 0;
					node.y = (top + bottom) / 2 + node.height / 2;
				} else {
					node.x = 0;
					node.y = 0;
				}
			}
			
		});
	}
	
	public void checkParentsAndShiftSubTrees() {
		nodes.forEach(new BiConsumer<Integer, Node>() {
			
			TreeMap<Integer, Node> visitedToShift = new TreeMap<>();
			Node lastVisited = null;
			
			public void accept(Integer id, Node node) {
				if (lastVisited != null) {
					int deltaY = lastVisited.y - lastVisited.height - node.y;
					if (deltaY < LayeredLayout.heightBetweenMethodsInCluster) {
						visitedToShift.forEach((visitedId, visitedNode) -> {
							visitedNode.accept(new Visitor() {
								
								public void visit(MethodNode method) {
									method.y += LayeredLayout.heightBetweenMethodsInCluster - deltaY;
								}
								
							});
						});
					}
				}
				lastVisited = node;
				visitedToShift.put(id, node);
			}
			
		});
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("[ layer: ").append( String.valueOf(id)).append(" ]\n");
		nodes.forEach((id, node) -> {
			info.append("\t[ method: ").append(node.getName()).append(" :: ");
			info.append("parent: ").append(node.parent.getName()).append(" :: ");
			info.append("x: ").append(node.x).append(" :: ");
			info.append("y: ").append(node.y).append(" :: ");
			info.append("width: ").append(node.width).append(" :: ");
			info.append("height: ").append(node.height).append(" ]\n");
			//info.append(node);
		});
		return info.toString();
	}
}
