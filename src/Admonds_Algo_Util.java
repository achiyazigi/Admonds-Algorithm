import java.util.HashSet;
import java.util.List;

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
     * 
     * @param tree
     * @param src
     * @param dest
     * @return the simple cycle formed in tree including src & dest
     */
    List<Integer> identify_cyc(weighted_graph tree, int src, int dest){ // evyatar
        return null;
    }
}