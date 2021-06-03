import java.util.Collection;
import java.util.HashMap;

public class SubGraph implements weighted_graph {
    private HashMap<Integer, node_info> sub_nodes;
    private HashMap<Integer, HashMap<node_info, edge_info>> sub_edges;
    private int MC;
    private int highest_key;
    private int edge_counter;
    private weighted_graph g;

    public SubGraph(weighted_graph g) {
        this.g = g;
        this.sub_edges = new HashMap<>();
        this.sub_nodes = new HashMap<>();
        this.MC = 0;
        this.highest_key = -1;
        this.edge_counter = 0;
    }

    @Override
    public void addNode(int key) {
        node_info to_add = g.getNode(key);
        if (to_add != null) {
            sub_nodes.put(key, to_add);
            MC++;
            if (key > highest_key) {
                highest_key = key;
            }
        }
    }

    @Override
    public node_info getNode(int key) {
        return sub_nodes.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if(sub_edges.containsKey(node1)){
            return sub_edges.get(node1).containsKey(g.getNode(node2));
        }
        return false;
    }

    @Override
    public edge_info getEdge(int node1, int node2) {
        if(sub_edges.containsKey(node1)){

            return sub_edges.get(node1).get(g.getNode(node2));
        }
        return null;
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if (g.hasEdge(node1, node2) && sub_nodes.containsKey(node1) && sub_nodes.containsKey(node2)) {
            if (!sub_edges.containsKey(node1)) {
                sub_edges.put(node1, new HashMap<node_info, edge_info>());
                sub_edges.put(node2, new HashMap<node_info, edge_info>());
            }
            edge_info e = g.getEdge(node1, node2);
            sub_edges.get(node1).put(g.getNode(node2), e);
            sub_edges.get(node2).put(g.getNode(node1), e);
            edge_counter++;
            MC++;
        }

    }

    @Override
    public Collection<node_info> getV() {
        return sub_nodes.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        if (sub_nodes.containsKey(node_id)) {
            return sub_edges.get(node_id).keySet();
        }
        return null;
    }

    @Override
    public node_info removeNode(int key) {
        node_info to_remove = getNode(key);
        if (to_remove != null) {
            HashMap<node_info, edge_info> ni = sub_edges.remove(key);
            edge_counter -= ni.size();
            if (ni != null) {
                for (node_info n : ni.keySet()) {
                    sub_edges.get(n.getKey()).remove(to_remove);
                }
            }
            MC++;
        }
        return sub_nodes.remove(key);
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            edge_counter--;
            MC++;
            sub_edges.get(node1).remove(getNode(node2));
            sub_edges.get(node2).remove(getNode(node1));
        }
    }

    @Override
    public int nodeSize() {
        return sub_nodes.size();
    }

    @Override
    public int edgeSize() {
        return edge_counter;
    }

    @Override
    public int getMC() {
        return MC;
    }

    @Override
    public int getHighest_key() {
        return highest_key;
    }

    @Override
    public void setHighest_key(int highest_key) {
        this.highest_key = highest_key;
    }
}