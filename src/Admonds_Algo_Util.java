
import java.util.*;

public class Admonds_Algo_Util {

    HashSet<Integer> free;
    HashSet<edge_info> match;
    weighted_graph g;
    weighted_graph tree;

    

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
                } else {
                    this.match.remove(e);
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

    void update_match() { // the algorithm!

        System.out.println("Starting bfs!"); // just for indication the button pressed...

        while (!this.free.isEmpty()) {
            int root = free.iterator().next();
            free.remove(root);
            bfs(root);
        }
    }

 
    public void bfs(int root) {
        Queue<Integer> q = new LinkedList<Integer>();
        HashSet<Integer> visited = new HashSet<>();
        weighted_graph tree = new WGraph_DS();
        SuperNode sn = new SuperNode(g, tree);
        tree.addNode(root);
        q.add(root);
        while (!q.isEmpty()) {
            int cur = q.poll();
            tree.addNode(cur);
            visited.add(cur);
            for (node_info ni : g.getV(cur)) {
                int kni = ni.getKey();
                edge_info e = g.getEdge(cur, kni);
                int ni_mate = getMate(kni);
                // ni not in tree and ni in match
                if (!visited.contains(kni) && ni_mate != -1) {
                    q.add(ni_mate);
                    tree.addNode(kni);
                    tree.connect(e);
                    tree.addNode(ni_mate);
                    tree.connect(g.getEdge(kni, ni_mate));
                    visited.add(kni);
                    visited.add(ni_mate);

                }

                // ni in tree and free
                else if (visited.contains(kni) && ni_mate == -1) {
                    var cycle = bfs(cur, kni, tree);
                    if (cycle.size() % 2 != 0) {
                        // close the cycle!

                        sn.compress(cycle);
                        q.add(cur);
                        break;
                    }
                }
                // ni not in tree and free, aug path found!
                else if (ni_mate == -1) {
                    tree.addNode(kni);
                    tree.connect(g.getEdge(cur, kni));
                    sn.decompressAll();
                    var path = getPath(backTracking(kni, root, tree));
                    augment(path);
                    free.remove(kni);
                    q.clear();
                    break;
                }
            }
        }
        sn.decompressAll();
    }

    public List<Integer> backTracking(int src, int dest, weighted_graph tree) {
        int curr = src;
        var path = new LinkedList<Integer>();
        int prev = -1;

        path.add(curr);
        while (curr != dest) {
            var nei = tree.getV(curr);
            if (nei.size() == 1) {
                curr = nei.iterator().next().getKey();
            } else if (nei.size() == 2) {
                for (node_info n : nei) {
                    if (n.getKey() != prev) {
                        curr = n.getKey();
                        break;
                    }
                }
            }
            // a junction in the tree
            else {
                curr = getMate(curr);
            }
            prev = path.getLast();
            path.add(curr);
        }
        return path;

    }

    public int getMate(int key) {
        for (edge_info e : match) {
            var nodes = e.getNodes();
            if (nodes.getFirst() == key) {
                return nodes.getSecond();
            }
            if (nodes.getSecond() == key) {
                return nodes.getFirst();
            }

        }
        return -1;
    }

    public List<edge_info> getPath(List<Integer> p) {
        var ans = new LinkedList<edge_info>();
        Iterator<Integer> nodes = p.iterator();
        int n = nodes.next();
        while (nodes.hasNext()) {
            int nei = nodes.next();
            ans.add(g.getEdge(n, nei));
            n = nei;
        }
        return ans;
    }

    /**
     * only method that changes the match
     * 
     * @param path
     */
    void augment(List<edge_info> path) { // achiya

        path.forEach((e) -> {
            e.setInMatch(!e.isInMatch());
            if (e.isInMatch()) {
                this.match.add(e);
            } else {
                this.match.remove(e);
            }
        });
    }

    /**
     * @param get src the run time of this function is o(v+e) sign in the tag of
     *            every node the distance from the src and if havn't path to which
     *            node her tag will be with -1 the path kept in the hash map and
     *            convert to linked list
     */
    private LinkedList<Integer> bfs(int src, int dest, weighted_graph graph) {
        if (graph.getV().isEmpty()) // check if it is empty graph
            return new LinkedList<Integer>();

        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(); // keep the nodes in the path -if path exist
        map.put(src, 0); // insert the src node
        for (node_info keys : graph.getV()) { // initial the tag's nodes- O(V)
            keys.setTag(-1);
        }

        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(graph.getNode(src).getKey()); // keep the key in the queue
        graph.getNode(src).setTag(0); // the first node init with 0

        while (!queue.isEmpty()) {
            Integer loc = queue.poll();
            for (node_info nei : graph.getV(loc)) {
                if (nei.getTag() == -1) {
                    queue.add(nei.getKey());
                    nei.setTag(graph.getNode(loc).getTag() + 1); // if have neighbor update the the tag according to his
                                                                 // parent
                    map.put(nei.getKey(), loc);
                }
            }
        }

        // extract the path from the map
        LinkedList<Integer> list = new LinkedList<Integer>();
        if (graph.getNode(dest).getTag() != -1) { // check if path between the nodes exist
            int curr = dest;
            list.add(curr); // insert the first
            while (map.get(curr) != src) {
                list.add(map.get(curr));
                curr = map.get(curr); // return the neighbord of the current node
            }
            list.add(src);
            Collections.reverse(list);
        }
        return list;
    }

    /**
     * @param tree
     * @param src
     * @param dest
     * @return the simple cycle formed in tree including src & dest
     */
    List<Integer> identify_cyc(weighted_graph tree, int src, int dest) { // evyatar
        if (src == dest) // havn't path
            return new LinkedList<Integer>(); // return empty linked list
        LinkedList<Integer> path = bfs(src, dest, tree);
        return path;
    }
}
