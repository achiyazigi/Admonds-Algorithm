import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Admonds_Algo_Util {

    HashSet<Integer> free;
    HashSet<edge_info> match;
    weighted_graph g;

    /**
     * itay, nir
     * this class compresses an odd cycle into one node
     * having all the out edges from the original cycle, as out edges of the super node
     * if multiple out edges have the same dest, only one edge will be considered.
     * NOTE! this class temporary changes the graph presented in the gui so its important to reset to original state
     * when its done... e.g. after update_match() and before get_match() called
     */
    private class SuperNode implements node_info {

        SuperNode(List<Integer> keys) {

        }

        @Override
        public int getKey() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public String getInfo() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setInfo(String s) {
            // TODO Auto-generated method stub

        }

        @Override
        public double getTag() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setTag(double t) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setX(int x) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setY(int y) {
            // TODO Auto-generated method stub

        }

        @Override
        public int X() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int Y() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getColor() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setColor(int color) {
            // TODO Auto-generated method stub

        }

        public void decompress() {

        }

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
     * setting the free set to correct state after initialization
     * for example, when init called and edges added after last match update
     */
    private void set_free() {
    }

    /**
     * setting the match set to correct state after initialization
     * for example, when init called and edges added after last match update
     */
    private void set_match() {
    }

    /**
     * return last match calculated in g
     * @return
     */
    HashSet<edge_info> get_match() {
        return this.match;
    }

    void update_match() { // the algorithm!

        System.out.println("Starting bfs!"); // just fot indication the button pressed...

        while(!this.free.isEmpty()){
            int root = free.iterator().next();
            free.remove(root);
            bfs(root);
        }
    }

    /**
     * finding augmenting path from key and calling augment
     * @param key
     */
    void bfs(Integer key) { // amichai

    }

    /**
     * only method that changes the match
     * @param path
     */
    void augment(List<edge_info> path){ // achiya

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

public static void main(String args []) {
	
	weighted_graph graph = new WGraph_DS();
	graph.addNode(0);
	graph.addNode(1);
	graph.addNode(2);
	graph.addNode(3);
	graph.addNode(4);
	graph.addNode(5);

//	graph.connect(0,1, 0);
//	graph.connect(1,4, 0);
//	graph.connect(4,3, 0);
//	graph.connect(0,2, 0);
//	graph.connect(2,5, 0);
	Admonds_Algo_Util adm= new Admonds_Algo_Util(graph);
	LinkedList<Integer> path = (LinkedList<Integer>) adm.identify_cyc(graph, 1, 1);
	System.out.println(path);
}


}



