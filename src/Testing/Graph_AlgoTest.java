
package Testing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.*;

class Graph_AlgoTest {

	private Graph_Algo getGA(){
		Graph_Algo GA = new Graph_Algo();

		Node N1 = new Node(1, new Point3D(3,4));
		Node N2 = new Node(2, new Point3D(3,4));
		Node N3 = new Node(3, new Point3D(3,4));
		Node N4 = new Node(4, new Point3D(3,4));
		Node N5 = new Node(5, new Point3D(3,4));

		GA.getGraph().addNode(N1);
		GA.getGraph().addNode(N2);
		GA.getGraph().addNode(N3);
		GA.getGraph().addNode(N4);
		GA.getGraph().addNode(N5);

		GA.getGraph().connect(1,5,3);
		GA.getGraph().connect(5,2,2);
		GA.getGraph().connect(2,3,5);
		GA.getGraph().connect(3,4,6);
		GA.getGraph().connect(4,3,7);
		GA.getGraph().connect(3,1,4);
		GA.getGraph().connect(3,5,1);

		return GA;
	}

	@Test
	void testInit() {
		Graph_Algo ga = getGA();
		DGraph dg = new DGraph();
		ga.init(dg);
		boolean ans = ga.getG() == dg;
		assertEquals(true, ans);
	}

	@Test
	void testSaveLoad() {
		Graph_Algo ga = getGA();
		ga.save("test");
		Graph_Algo load = new Graph_Algo();
		load.init("test");
	}

	@Test
	void testIsConnected() {
		Graph_Algo ga = getGA();
		assertTrue(ga.isConnected());
		ga.getG().removeEdge(3,4);
		assertFalse(ga.isConnected());
	}

	@Test
	void testShortestPathDist() {
		Graph_Algo ga = getGA();
		assertEquals(ga.shortestPathDist(3, 5),1);
		assertEquals(ga.shortestPathDist(3, 2),3);
		assertEquals(ga.shortestPathDist(4, 2),10);
	}

	@Test
	void testShortestPath() {
		Graph_Algo ga = getGA();
		List<node_data> path = ga.shortestPath(4,2);
		List<Integer> actual = new ArrayList<Integer>();
        Iterator<node_data> itr = path.iterator();
        while(itr.hasNext()) {
            actual.add(itr.next().getKey());
        }
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(4);
        expected.add(3);
        expected.add(5);
        expected.add(2);
        assertTrue(actual.equals(expected));
	}

	@Test
	void testTSP() {
		Graph_Algo ga = getGA();
		List<Integer> targets = new ArrayList<>();
		targets.add(1);
		targets.add(4);
		List<node_data> path = ga.TSP(targets);
		Iterator<node_data> itr = path.iterator();
		node_data n1 = ga.getGraph().getNode(1);
        node_data n4 = ga.getGraph().getNode(4);
        assertTrue(path.contains(n1));
        assertTrue(path.contains(n4));
	}

	@Test
	void testCopy() {
		Graph_Algo ga = getGA();
		graph g = ga.getG();
		graph g_copy = ga.copy();
        assertNotEquals(g.getNode(1), g_copy.getNode(1));
        assertNotEquals(g.getE(3), g_copy.getE(3));
        Graph_Algo ga_copy = new Graph_Algo();
        ga_copy.init(g_copy);
        assertEquals(ga.shortestPathDist(4,2), ga_copy.shortestPathDist(4,2));
	}
}
