import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;


public class Admonds_Algo_Util_test {
	
	
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
	
	
	AAU.decompress((Admonds_Algo_Util.SuperNode)g.getNode(6));
	
	
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
 * 
 *     4
 *     |
 * 0---1
 * | \ |
 * 3---2
 * |
 * 5
 * 
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

    
}
