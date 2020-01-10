package Testing;
import static org.junit.jupiter.api.Assertions.*;

import dataStructure.DGraph;
import org.junit.jupiter.api.Test;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class DGraphTest {

	private DGraph getDGraph1() {
		DGraph g = new DGraph();
		DGraph g2 = g;
		Node n4=new Node(0,new Point3D(-50,-50));
		Node n5=new Node(1,new Point3D(0,75));
		Node n6=new Node(2,new Point3D(50,50));
		Node n7=new Node(3,new Point3D(50,-50));
		Node n8=new Node(4,new Point3D(0,-75));
		Node n9=new Node(5,new Point3D(-50,50));
		g.addNode(n4);
		g.addNode(n5);
		g.addNode(n6);
		g.addNode(n7);
		g.addNode(n8);
		g.addNode(n9);
		for(int i=0;i<5;i++) {
			g.connect(i, i+1, 2.5);
		}
		return g;
	}

	@Test
	void testGetEdge() {
		DGraph g3= getDGraph1();

		for(int j=0;j<5;j++) {
			assertEquals(g3.getEdge(j, j+1).getDest(),j+1);
			assertEquals(g3.getEdge(j, j+1).getSrc(),j);
		}
	}

	@Test
	void testAddNode() {
		DGraph g = getDGraph1();
		List<Integer> l_prev = new ArrayList<Integer>();
		for(node_data node : g.getV()) l_prev.add(node.getKey());
		node_data node = new Node(10, new Point3D(-9,8));
		g.addNode(node);
		List<Integer> l_curr = new ArrayList<Integer>();
		for(node_data node1 : g.getV()) l_curr.add(node1.getKey());
		l_prev.add(node.getKey());
		assertTrue(l_prev.equals(l_curr));
	}

	@Test
	void testConnect() {
		DGraph g = getDGraph1();
		assertFalse(((Node) g.getNode(1)).getNeighbors().containsKey(0));
		g.connect(1,0,3);
		assertTrue(((Node) g.getNode(1)).getNeighbors().containsKey(0));
	}

	@Test
	void getVgetEtest() {
		DGraph g6=getDGraph1();
		Collection<node_data> nod=g6.getV();
		for(node_data a:nod) {
			Collection<edge_data> edg=g6.getE(a.getKey());
			for(edge_data e:edg) {
				assertEquals(e.getSrc(),a.getKey());
				assertEquals(e.getDest(),a.getKey()+1);
			}
		}
		assertEquals(g6.nodeSize(),6);
		assertEquals(g6.edgeSize(),5);
	}

	@Test
	void testRemoveNode() {
		DGraph g = getDGraph1();
		assertTrue(((Node) g.getNode(1)).getNeighbors().containsKey(2));
		Node n = (Node)g.getNode(2);
		g.removeNode(2);
		assertFalse(g.getV().contains(n));
		assertFalse(((Node) g.getNode(1)).getNeighbors().containsKey(2));
	}

	@Test
	void testRemoveEdge() {
		DGraph g = getDGraph1();
		assertTrue(((Node) g.getNode(3)).getNeighbors().containsKey(4));
		g.removeEdge(3,4);
		assertFalse(((Node) g.getNode(3)).getNeighbors().containsKey(4));
	}

	@Test
	void testNodeSize() {
		DGraph g = getDGraph1();
		assertEquals(g.nodeSize(), 6);
	}

	@Test
	void testEdgeSize() {
		DGraph g = getDGraph1();
		assertEquals(g.edgeSize(), 5);
	}

	@Test
	void testGetMC() {
		DGraph g = getDGraph1();
		assertEquals(g.getMC(),11); //6 nodes + 5 edges = 11 changes
		g.removeNode(0);
		assertEquals(g.getMC(),12); //remove node = 1 change
		g.removeEdge(2, 3);
		assertEquals(g.getMC(),13); //another change

		for(int y=1;y<5;y++) {
			g.connect(y+1, y, y*2.3);
		}
		assertEquals(g.getMC(),17);
	}
}