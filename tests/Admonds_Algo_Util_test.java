import org.junit.jupiter.api.Test;

import kotlin.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Admonds_Algo_Util_test {

    /**
     * 
     * @param seed
     * @param nodes
     * @param edges
     * @return random graph (seed uniformed connections)
     */
    static private weighted_graph graph_generator(int seed, int nodes, int edges) {
        weighted_graph res = new WGraph_DS();
        List<Pair<Integer, Integer>> free_edges = new LinkedList<>();
        Random r = new Random(seed);
        for (int i = 0; i < nodes - 1; i++) {
            res.addNode(i);
            for (int j = i + 1; j < nodes; j++) {
                free_edges.add(new Pair<Integer, Integer>(i, j));
            }
        }
        res.addNode(nodes - 1);

        assert (free_edges.size() == nodes * (nodes - 1) / 2);
        Collections.shuffle(free_edges, r);
        for (int i = 0; i < edges; i++) {
            var e = free_edges.remove(0);
            res.connect(e.getFirst(), e.getSecond(), r.nextDouble());
        }
        return res;
    }

    /**
     * 
     * @param seed
     * @param g
     * @return a random edge in g, null if g has no edges
     */
    static private edge_info get_random_edge(int seed, weighted_graph g) {
        if (g.nodeSize() == 0) {
            return null;
        }
        int nodes = g.nodeSize();
        Random r = new Random(seed);
        edge_info e = null;

        while (e == null) {
            e = g.getEdge(r.nextInt(nodes), r.nextInt(nodes));
        }
        return e;
    }

    @Test
    void test_super_node() {

        weighted_graph g = build_simple_graph_1();

        List<Integer> list_of_nodes = new LinkedList<Integer>();
        list_of_nodes.add(0);
        list_of_nodes.add(1);
        list_of_nodes.add(2);
        list_of_nodes.add(3);

        Admonds_Algo_Util AAU = new Admonds_Algo_Util(g);
        AAU.compress(list_of_nodes);

        assertEquals(3, g.nodeSize());
        assertTrue(g.hasEdge(4, 6));
        assertTrue(g.hasEdge(5, 6));
        assertFalse(g.hasEdge(4, 5));

        AAU.decompress((Admonds_Algo_Util.SuperNode) g.getNode(6));

        assertEquals(6, g.nodeSize());
        assertTrue(g.hasEdge(0, 1));
        assertTrue(g.hasEdge(1, 2));
        assertTrue(g.hasEdge(2, 3));
        assertTrue(g.hasEdge(3, 0));
        assertTrue(g.hasEdge(3, 5));
        assertTrue(g.hasEdge(4, 1));
        assertTrue(g.hasEdge(0, 2));

    }

    /**
     * build a graph in the form:
     * _________________________________________________________________________ 4
     * _________________________________________________________________________ |
     * _____________________________________________________________________ 0---1
     * _____________________________________________________________________ | \ |
     * _____________________________________________________________________ 3---2
     * _____________________________________________________________________ |
     * _____________________________________________________________________ 5
     * 
     * @return
     */
    private weighted_graph build_simple_graph_1() {

        weighted_graph g = new WGraph_DS();

        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.addNode(5);

        g.connect(0, 1, 0);
        g.connect(1, 2, 0);
        g.connect(2, 3, 0);
        g.connect(3, 0, 0);

        g.connect(1, 4, 0);
        g.connect(3, 5, 0);
        g.connect(0, 2, 0);

        return g;
    }

    @Test
    public void identify_cyc() {
        weighted_graph graph = new WGraph_DS();
        graph.addNode(0);
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.addNode(5);

        // graph.connect(0,1, 0);
        // graph.connect(1,4, 0);
        // graph.connect(4,3, 0);
        // graph.connect(0,2, 0);
        // graph.connect(2,5, 0);
        Admonds_Algo_Util adm = new Admonds_Algo_Util(graph);
        LinkedList<Integer> path = (LinkedList<Integer>) adm.identify_cyc(graph, 1, 1);
        System.out.println(path);
    }

    @Test
    public void test_augment() {
        weighted_graph g = build_simple_graph_1();
        g.getEdge(1, 0).setInMatch(true);
        Admonds_Algo_Util aa = new Admonds_Algo_Util(g);
        LinkedList<edge_info> path = new LinkedList<>();
        path.add(g.getEdge(4, 1));
        path.add(g.getEdge(1, 0));
        path.add(g.getEdge(0, 3));
        // 4 > false > 1 > true > 0 > false > 3
        aa.augment(path);
        // 4 > true > 1 > false > 0 > true > 3
        assertTrue(g.getEdge(4, 1).isInMatch());
        assertFalse(g.getEdge(1, 0).isInMatch());
        assertTrue(g.getEdge(0, 3).isInMatch());

        path.clear();

        path.add(g.getEdge(1, 0));
        path.add(g.getEdge(0, 3));
        path.add(g.getEdge(3, 5));
        // 1 > false > 0 > true > 3 > false > 5
        aa.augment(path);
        // 1 > true > 0 > false > 3 > true > 5

        assertTrue(g.getEdge(1, 0).isInMatch());
        assertFalse(g.getEdge(0, 3).isInMatch());
        assertTrue(g.getEdge(3, 5).isInMatch());

        path.clear();

        path.add(g.getEdge(2, 1));
        path.add(g.getEdge(1, 0));
        path.add(g.getEdge(0, 2));

        g.getEdge(2, 3).setInMatch(true);
        path.add(g.getEdge(2, 3));
        path.add(g.getEdge(3, 0));

        // 2 > false > 1 > true > 0 > false > 2 > true > 3 > false > 0

        aa.augment(path);
        // 2 > true > 1 > false > 0 > true > 2 > false > 3 > true > 0

        assertTrue(g.getEdge(2, 1).isInMatch());
        assertFalse(g.getEdge(1, 0).isInMatch());
        assertTrue(g.getEdge(0, 2).isInMatch());
        assertFalse(g.getEdge(2, 3).isInMatch());
        assertTrue(g.getEdge(3, 0).isInMatch());
    }

    @Test
    public void test_init() {
        int nodes = 100;
        int edges = 4217 /* nodes * (nodes - 1) / 2 */;
        int seed = 0;
        Random r = new Random(seed);
        weighted_graph g = graph_generator(seed, nodes, edges);
        assertTrue(g.nodeSize() == nodes);
        assertTrue(g.edgeSize() == edges);

        Admonds_Algo_Util aa = new Admonds_Algo_Util(g);
        for (int i = 0; i < edges; i++) {
            edge_info e = get_random_edge(seed, g);
            boolean b = r.nextBoolean();
            e.setInMatch(b);
            aa.init(g);
            Set<edge_info> match = aa.get_match();
            assertEquals(match.contains(e), b);
            assertEquals(aa.free.contains(e.getNodes().getFirst()), !b);
            assertEquals(aa.free.contains(e.getNodes().getSecond()), !b);
        }

    }
}
