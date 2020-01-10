package dataStructure;

import java.util.Collection;
import java.io.Serializable;
import java.util.HashMap;

/**
 * This class implements graph interface.
 * It represents a directional weighted graph.
 */
public class DGraph implements graph, Serializable {

    //fields
    HashMap<Integer, node_data> graph = new HashMap<Integer, node_data>();
    private int nodeSize = 0;
    private int edgeSize = 0;
    private int MC =0;

    //getters

    /**
     * return the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if(!graph.containsKey(key)) throw new RuntimeException("Error: the node is not in the graph!");
        return graph.get(key);
    }

    /**
     * return the data of the edge (src,dest), null if none.
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(!graph.containsKey(src)) throw new RuntimeException("Error: the source node is not in the graph!");
        if(!((Node) graph.get(src)).getNeighbors().containsKey(dest)) throw new RuntimeException("Error: the destination node is not in the graph!");
        return ((Node) graph.get(src)).getNeighbors().get(dest);
    }

    /**
     * add a new node to the graph with the given node_data.
     * @param n = a parameter of node_data type to add to the DGraph.
     */
    @Override
    public void addNode(node_data n) {
        if(this.graph.containsKey(n.getKey())) throw new RuntimeException("There is already a Node with this ID, please chose another key.");
        this.graph.put(n.getKey(), (Node)n);
        this.nodeSize++;
        MC++;
    }

    /**
     * Connect an edge with weight w between node src to node dest.
     * @param src = the source node.
     * @param dest = the destination node.
     * @param w = the weight of the new edge.
     */
    public void connect(int src, int dest, double w) {
        edge e = new edge(src, dest, w);
        ((Node) this.graph.get(src)).setNeighbors(dest, e);
        edgeSize++;
        MC++;
    }

    /**
     * This method return a pointer (shallow copy) for the collection representing all the nodes in the graph.
     * @return Collection<node_data> of all the nodes in the graph.
     */
    @Override
    public Collection<node_data> getV() {
        return this.graph.values();
    }

    /**
     * This method return a pointer (shallow copy) for the collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * @param node_id = a key of some node that we want the neighbors of it.
     * @return Collection<edge_data> of all the edges that get out from the given node.
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        return ((Node)(this.graph.get(node_id))).getNeighbors().values();
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * @return the data of the removed node (null if none).
     * @param key = the node's key.
     */
    @Override
    public node_data removeNode(int key) {
        Node copy=new Node((Node)getNode(key));
        this.graph.remove(key);
        nodeSize--;
        MC++;
        for (int i = 0; i < nodeSize; i++) {
            if (null!=this.graph.get(i)) {
                if(null!=((Node)this.graph.get(i)).getNeighbors().get(key)){
                    ((Node)this.graph.get(i)).getNeighbors().remove(key);
                    edgeSize--;
                    MC++;
                }
            }
        }
        return copy;
    }

    /**
     * Delete the edge from the graph.
     * @param src = the source node.
     * @param dest = the destination node.
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        edge_data copy = new edge((edge)((Node)this.graph.get(src)).getNeighbors().get(dest));
        ((Node)this.graph.get(src)).getNeighbors().remove(dest);
        edgeSize--;
        MC++;
        return copy;
    }

    /** Methos that returns the number of vertices (nodes) in the graph.
     * @return nodeSize value.
     */
    @Override
    public int nodeSize() {
        return this.nodeSize;
    }

    /**
     * Return the number of edges (assume directional graph).
     * @return edgeSize value.
     */
    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

    /**
     * Return the Mode Count - for testing changes in the graph.
     * @return the numbers of changes that happened in the graph.
     */
    @Override
    public int getMC() {
        return MC;
    }
}
