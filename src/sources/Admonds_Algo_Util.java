import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kotlin.Pair;


public class Admonds_Algo_Util {

    HashSet<Integer> free;
    HashSet<edge_info> match;
    weighted_graph g;

    /**
     * itay, nir this class compresses an odd cycle into one node having all the out
     * edges from the original cycle, as out edges of the super node if multiple out
     * edges have the same dest, only one edge will be considered. NOTE! this class
     * temporary changes the graph presented in the gui so its important to reset to
     * original state when its done... e.g. after update_match() and before
     * get_match() called
     */
    class SuperNode implements node_info {
    	
        private int key;
        private String info;
        private double tag;
        private int x;
        private int y;
        private int color = 0;

        private HashSet<edge_info> restored_neighbors = new HashSet<edge_info>();
        private HashSet<node_info> original_nodes = new HashSet<node_info>();
        private HashSet<Integer> neighbors = new  HashSet<Integer>();
        
        SuperNode(List<Integer> keys) {
        	
        	// set the key
        	this.key = g.getHighest_key() + 1;
        	
        
        	// save the data of the nodes
        	for(int node_id : keys) {
        		
        		for(node_info nei : g.getV(node_id)) {	// neighbors
        			this.restored_neighbors.add(g.getEdge(node_id, nei.getKey()));
        			if(!keys.contains(nei.getKey())) {this.neighbors.add(nei.getKey());}
        		}
        		
	        	this.original_nodes.add(g.getNode(node_id));	// save the node
	        }
        }

        @Override
        public int getKey() {return this.key;}
        @Override
        public String getInfo() {return this.info;}
        @Override
        public void setInfo(String s) {this.info = s;}
        @Override
        public double getTag() {return this.tag;}
        @Override
        public void setTag(double t) {this.tag = t;}
        @Override
        public void setX(int x) {this.x = x;}
        @Override
        public void setY(int y) {this.key = y;}
        @Override
        public int X() {return this.x;}
        @Override
        public int Y() {return this.y;}
        @Override
        public int getColor() {return this.color;}
        @Override
        public void setColor(int color) {this.color = color;}
    }

    Admonds_Algo_Util(weighted_graph g) {
        this.free = new HashSet<>();
        this.match = new HashSet<>();
        this.init(g);
    }

    Admonds_Algo_Util() {
        this.free = new HashSet<>();
        this.match = new HashSet<>();
    }

    void init(weighted_graph g) {
        this.g = g;
        this.set_match();
        this.set_free();
    }

    /**
     * setting the free set to correct state after initialization for example, when
     * init called and edges added after last match update
     */
    private void set_free() {
        this.g.getV().forEach((e) -> {
            this.free.add(e.getKey());
        });
        this.match.forEach((e) -> {
            this.free.remove(e.getNodes().getFirst());
            this.free.remove(e.getNodes().getSecond());
        });

    }

    /**
     * setting the match set to correct state after initialization for example, when
     * init called and edges added after last match update
     */
    private void set_match() {
        this.g.getV().forEach((u) -> {
            int ukey = u.getKey();
            this.g.getV(ukey).forEach((v) -> {
                edge_info e = this.g.getEdge(ukey, v.getKey());
                if (e.isInMatch()) {
                    this.match.add(e);
                }
            });
        });
    }

    /**
     * return last match calculated in g
     * 
     * @return
     */
    HashSet<edge_info> get_match() {
        return this.match;
    }
    
    /**
     * compress the nodes given to a super-node
     * @param keys
     */
    void compress(List<Integer> keys) {
    	SuperNode sn = new SuperNode(keys);
 
    	g.addNode(sn);	// add to the graph
    	
    	for(int n : keys) {g.removeNode(n);}	// remove the nodes
    	for(int nei : sn.neighbors) {g.connect(sn.getKey(), nei, 0);}	//connect the neighbors
    }
    
    /**
     * de-compress a super-node to the original nodes
     * @param sn
     */
    public void decompress(SuperNode sn) {

    	for(node_info node : sn.original_nodes) {g.addNode(node);}	// restore the nodes
    	
    	for(edge_info e : sn.restored_neighbors) {	// restore all edges
    		g.connect(e.getNodes().getFirst(), e.getNodes().getSecond(), e.getValue());
    		if(e.isInMatch()) {g.getEdge(e.getNodes().getFirst(), e.getNodes().getSecond()).setInMatch(true);}
    	}
    
    	g.removeNode(sn.getKey());	// remove the super-node
    }
    

    void update_match() { // the algorithm!

        System.out.println("Starting bfs!"); // just for indication the button pressed...

        while (!this.free.isEmpty()) {
            int root = free.iterator().next();
            free.remove(root);
            bfs(root);
        }
    }

    /**
     * finding augmenting path from key and calling augment
     * 
     * @param key
     */
    void bfs(Integer key) { // amichai

    }

    /**
     * only method that changes the match
     * 
     * @param path
     */
    void augment(List<edge_info> path) { // achiya
        
        path.forEach((e)->{
            e.setInMatch(!e.isInMatch());
            if(e.isInMatch()){
                this.match.add(e);
            }
        });
    }
	/**
	 * @param get src
	 * the run time of this function is o(v+e)
	 * sign in the tag of every node the distance from the src 
	 * and if havn't path to which node her tag will be with -1
	 * the path kept in the hash map and convert to linked list
	 */
	private LinkedList<Integer> bfs(int src,int dest, weighted_graph graph) {
		if(graph.getV().isEmpty())								//check if it is empty graph
			return new LinkedList<Integer>();
	
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();	//keep the nodes in the path -if path exist 
		map.put(src, 0);	// insert the src node
		for(node_info keys : graph.getV()) {						//initial the tag's nodes- O(V)
			keys.setTag(-1);
		}
		
		Queue<Integer> queue=new LinkedList<Integer>();
		queue.add(graph.getNode(src).getKey());				//keep the key in the queue
		graph.getNode(src).setTag(0);				//the first node init with 0 										
		
		while(!queue.isEmpty()) {
			Integer loc=queue.poll();
					for(node_info nei : graph.getV(loc)) {
						if(nei.getTag() == -1) {
							queue.add(nei.getKey());
							nei.setTag(graph.getNode(loc).getTag()+1);			//if have neighbor update the the tag according to his parent
							map.put(nei.getKey(), loc);
						}
					}
				}
		
		//extract the path from the map 
		LinkedList<Integer> list =new LinkedList<Integer>();
		if(graph.getNode(dest).getTag() != -1) {	// check if path between the nodes exist
			int curr = dest;
			list.add(curr);	//insert the first 
				while(map.get(curr) != src) {
					list.add(map.get(curr));
					curr = map.get(curr); 	//return the neighbord of the current node
			}
				list.add(src);
				Collections.reverse(list);
				}		
		return list;
			}
    /**
     * 
     * @param tree
     * @param src
     * @param dest
     * @return the simple cycle formed in tree including src & dest
     */
    List<Integer> identify_cyc(weighted_graph tree, int src, int dest){ // evyatar
    	if(src == dest)	//havn't path
    		return new LinkedList<Integer>();	//return empty linked list 
    LinkedList<Integer> path = bfs(src, dest,tree);
    	return path;
    }
}



