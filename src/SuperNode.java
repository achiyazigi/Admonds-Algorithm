
import java.util.*;

public class SuperNode {
	private Stack<Integer> reverse_order;
	private HashMap<Integer, HashSet<edge_info>> super_nodes; // for every represent key keep all the edges that connect
																// her
	HashMap<Integer, HashMap<Integer, node_info>> preserved_nodes; // keep the nodes that compressed
	HashMap<Integer, HashSet<edge_info>> tree_edges; // keep originaly tree edges
	weighted_graph g; // the graph that operate the method on him
	weighted_graph tree; // to de/compress with g

	public SuperNode(weighted_graph h, weighted_graph tree) { // constructor
		this.g = h;
		this.tree = tree;
		this.super_nodes = new HashMap<Integer, HashSet<edge_info>>();
		this.preserved_nodes = new HashMap<Integer, HashMap<Integer, node_info>>();
		this.tree_edges = new HashMap<>();
		this.reverse_order = new Stack<>();
	}

	/**
	 * @param to_compress- list of integer - key nodes the goal of the function is
	 *                     to compressed all the nodes to the single node who call
	 *                     "contract node" the function take care to keep all the
	 *                     details that contract
	 **/
	void compress(List<Integer> to_compress) {
		// copy all the edges to the map
		int id_list = to_compress.get(0); // the repersent node - stay after the contract
		HashSet<edge_info> compressed_edges = new HashSet<edge_info>(); // all the edges that remove
		this.tree_edges.put(id_list, new HashSet<>());
		this.reverse_order.push(id_list);
		this.preserved_nodes.put(id_list, new HashMap<>());
		// move on all the edges and insert them
		for (Integer node : to_compress) {
			for (node_info nei : this.g.getV(node)) { // move on all the edges
				int nei_key = nei.getKey();
				edge_info e = this.g.getEdge(node, nei_key);
				if (tree.hasEdge(node, nei_key)) {
					tree_edges.get(id_list).add(e);
				}
				compressed_edges.add(e); // add the edge to the list

				if (!to_compress.contains(nei_key)) { // check if it not "intrinsic" edge
					this.g.connect(id_list, nei_key, 0); // connect the edge to the represent node
					edge_info added_edge = this.g.getEdge(id_list, nei_key);
					added_edge.setInMatch(e.isInMatch());
					added_edge.setValue(e.getValue());
					this.tree.connect(e);
				}
			}

			if (node != id_list) { // check if it's not the first node
				this.preserved_nodes.get(id_list).put(node, this.g.removeNode(node));
				this.tree.removeNode(node);
			} else { // if it the represent node you not remove him from the map
				preserved_nodes.get(id_list).put(id_list, this.g.getNode(id_list));
			}
		}
		this.super_nodes.put(id_list, compressed_edges); // put the edges with the represent key

	}

	/**
	 * 
	 * @param decompress - get an the represent integer the function restore all the
	 *                   data that compressed in the super node
	 */
	int decompress(int decompress) {
		if (!this.super_nodes.containsKey(decompress)) { // check if it's super node
			return -1;
		}
		this.g.removeNode(decompress); // remove the super node
		this.tree.removeNode(decompress);

		for (node_info n : this.preserved_nodes.get(decompress).values()) {
			this.g.addNode(n);
			this.tree.addNode(n);
		}

		// add all the edges and nodes to the graph
		for (edge_info edge : this.super_nodes.get(decompress)) {
			// restore removed nodes
			node_info src = this.g.getNode(edge.getNodes().getFirst());
			node_info dest = this.g.getNode(edge.getNodes().getSecond());

			// connect the reserved edge
			this.g.connect(edge);

			// to connect the tree we need to check carefully 3 cases:
			if ((this.preserved_nodes.get(decompress).containsKey(src.getKey())
					&& this.preserved_nodes.get(decompress).containsKey(dest.getKey())) // if edge is a cycle edge or
					|| (src.getKey() == decompress || dest.getKey() == decompress)// one of them is the representative
																					// or
					|| tree_edges.get(decompress).contains(edge)) { // the edge is originaly from the tree

				this.tree.connect(edge);
			}

		}
		// this.preserved_nodes.remove(decompress);
		this.super_nodes.remove(decompress);
		return decompress;
	}

	public void decompressAll() {
		for (var key : reverse_order) {
			if (this.super_nodes.containsKey(key)) {

				decompress(key);
			}
		}
	}

}
