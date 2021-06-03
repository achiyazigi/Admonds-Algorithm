import java.util.*;

import kotlin.Pair;


public class SuperNode {
	
	private HashMap<Integer,HashSet<edge_info>> super_nodes;
	HashMap<Integer, node_info> preserved_nodes;
	weighted_graph g;
	public SuperNode(weighted_graph h) {	//constructor
		this.g = h;
		this.super_nodes = new HashMap<Integer, HashSet<edge_info>>();
		this.preserved_nodes = new HashMap<Integer, node_info>();
	}
	
	void compress(List<Integer> to_compress) {
		//copy all the edges to the map
		int id_list = to_compress.get(0);
		HashSet<edge_info> compressed_edges = new HashSet<edge_info>();
		//move on all the edges and insert them
		for(Integer node: to_compress) {
			for(node_info nei: this.g.getV(node)) {
				int nei_key = nei.getKey();
				edge_info e = this.g.getEdge(node, nei_key);
				compressed_edges.add(e);
				if(!to_compress.contains(nei_key)){
					this.g.connect(id_list, nei_key, 0);
					edge_info added_edge = this.g.getEdge(node, nei_key);
					added_edge.setInMatch(e.isInMatch());
					added_edge.setValue(e.getValue());
				}
			}
		
			if(node != id_list) {
				this.preserved_nodes.put(node, this.g.removeNode(node));
			}
			preserved_nodes.put(id_list, this.g.getNode(id_list));
			
		}
		this.super_nodes.put(id_list, compressed_edges);
		
	}
	
	int decompress(int decompress) {
		if(!this.super_nodes.containsKey(decompress)) {
			return -1;
		}
		this.g.removeNode(decompress);
		for(edge_info edge: this.super_nodes.get(decompress)) {
			int src = edge.getNodes().getFirst();
			int dest = edge.getNodes().getSecond();
			this.g.addNode(this.preserved_nodes.get(src));
			this.g.addNode(this.preserved_nodes.get(dest));
			this.g.connect(src,dest, 0);
			edge_info curr_edge = this.g.getEdge(src, dest);
			if(curr_edge != null) {
				curr_edge.setInMatch(edge.isInMatch());
				curr_edge.setValue(edge.getValue());
			}
		
		}
		return decompress; 
	}
	
	public static void main(String[] args) {
			
	}

}
