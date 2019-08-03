package com.maxamhacker.thepathfinder.graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.maxamhacker.thepathfinder.graph.layout.Layout;
import com.maxamhacker.thepathfinder.graph.nodes.CallNode;
import com.maxamhacker.thepathfinder.graph.nodes.MethodNode;
import com.maxamhacker.thepathfinder.graph.nodes.Node;

public class PTHGraph implements Graph {
	
	private Node root;

	private PTHGraph(Node root) {
		this.root = root;
	}
	
	public static PTHGraph create(String path) {
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject(content);
		JSONArray nodes = (JSONArray)json.get("nodes");
		JSONArray links = (JSONArray)json.get("links");
		
		HashMap<String, Node> graphNodes = new HashMap<>();
		nodes.forEach(node -> {			
			int id = (int)((JSONObject)node).get("id");
			String name = (String)((JSONObject)node).get("name");
			String type = (String)((JSONObject)node).get("type");		
			switch (type) {
				case "call":
					CallNode call = new CallNode(id, name);
					graphNodes.put(String.valueOf(id), call);
					break;
				case "method":
					MethodNode method = new MethodNode(id, name);
					graphNodes.put(String.valueOf(id), method);
					break;
			}
			
		});
		links.forEach(link -> {
			int source = (int)((JSONObject)link).get("source");
			int target = (int)((JSONObject)link).get("target");
			Node src = graphNodes.get(String.valueOf(source));
			Node tgt = graphNodes.get(String.valueOf(target));
			if (src instanceof MethodNode || tgt instanceof CallNode)
				((MethodNode)src).addCall((CallNode)tgt);
			else if (src instanceof CallNode || tgt instanceof MethodNode)
				((CallNode)src).parent.addChild(tgt);
		});
		
		Node root = graphNodes.get("0");
		if (root == null) return null;
		while (root.parent != null) root = root.parent;
		root.parent = new MethodNode(-1, "~ Root Node ~");
		return new PTHGraph(root);
	}
	
	public Node getRoot() {
		return this.root;
	}
	
	public void setRoot(Node root) {
		this.root = root;
	}
	
	public Graph layoutCalculate(Layout layout) {
		layout.apply(this);
		return this;
	}
}
