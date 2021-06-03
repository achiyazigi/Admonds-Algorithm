import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import kotlin.Pair;


public class SuperNode {
	
	private HashMap<Integer,HashSet<edge_info>> super_nodes;	//for every represent key keep all the edges that connect her 
	HashMap<Integer, node_info> preserved_nodes;	//keep the nodes that compressed
	weighted_graph g;	//the graph that operate the method on him 
	
	public SuperNode(weighted_graph h) {	//constructor
		this.g = h;
		this.super_nodes = new HashMap<Integer, HashSet<edge_info>>();
		this.preserved_nodes = new HashMap<Integer, node_info>();
	}
	
	/**
	 * @param to_compress- list of integer - key nodes 
	 * the goal of the function is to compressed all the nodes
	 * to the single node who call "contract node"
	 * the function take care to keep all the details that contract 
	 **/
	void compress(List<Integer> to_compress) {
		//copy all the edges to the map
		int id_list = to_compress.get(0);	//the repersent node - stay after the contract
		HashSet<edge_info> compressed_edges = new HashSet<edge_info>();	//all the edges that remove
		
		//move on all the edges and insert them
		for(Integer node: to_compress) {
			for(node_info nei: this.g.getV(node)) {		//move on all the edges 
				int nei_key = nei.getKey();
				edge_info e = this.g.getEdge(node, nei_key);
				compressed_edges.add(e);		//add the edge to the list
				
				if(!to_compress.contains(nei_key)){		//check if it not "intrinsic" edge
					this.g.connect(id_list, nei_key, 0);	//connect the edge to the represent node
					edge_info added_edge = this.g.getEdge(id_list, nei_key);
					added_edge.setInMatch(e.isInMatch());
					added_edge.setValue(e.getValue());
				}
			}
		
			if(node != id_list) {	//check if it's not the first node
				this.preserved_nodes.put(node, this.g.removeNode(node));
			}
			else {	//if it the represent node you not remove him from the map
				preserved_nodes.put(id_list, this.g.getNode(id_list));
			}
		}
		this.super_nodes.put(id_list, compressed_edges);	//put the edges with the represent key
		
	}
	/**
	 * 
	 * @param decompress - get an the represent integer 
	 * the function restore all the data that compressed in the super node
	 */
	int decompress(int decompress) {
		if(!this.super_nodes.containsKey(decompress)) {		//check if it's super node
			return -1;
		}
		this.g.removeNode(decompress);		//remove the super node
		
		//add all the edges and nodes to the graph
		for(edge_info edge: this.super_nodes.get(decompress)) {	
			int src = edge.getNodes().getFirst();
			int dest = edge.getNodes().getSecond();
			this.g.addNode(this.preserved_nodes.get(src));
			this.g.addNode(this.preserved_nodes.get(dest));
			this.g.connect(src,dest, 0);
			edge_info curr_edge = this.g.getEdge(src, dest);
			if(curr_edge != null) {
				//update all the info on the edges as before 
				curr_edge.setInMatch(edge.isInMatch());
				curr_edge.setValue(edge.getValue());
			}
		
		}
		return decompress; 
	}
	

}
