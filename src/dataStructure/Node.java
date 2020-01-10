package dataStructure;

import utils.Point3D;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * This class implements node_data interface, and representing a Node:
 * Node is a vertex, in a graph for example, and has key (ID: unique to every node).
 * there are some more attributes to the node, that will help us to implement the algorithms in the rest of our project.
 */
public class Node implements node_data, Comparable<Object>, Serializable {

    private int key;
    private double weight; //a variable that will save the distances at shortestPathDist method;
    private Point3D location;
    private String info;
    private int tag;
    private HashMap<Integer, edge_data> neighbors = new HashMap<Integer, edge_data>();
    private boolean isVisit;

    public Node() {
        key = 0;
        weight = 0;
        location = null;
        info = "";
        tag = 0;
        isVisit = false;
    }

    public Node(int key, Point3D location) {
        this.key = key;
        this.location = location;
    }

    public Node(int key) {
        this.key = key;
        double x = Math.random() * 10;
        double y = Math.random() * 10;
        Point3D p = new Point3D(x,y);
    }

    public Node(Node copy) {
        this.key = copy.key;
        this.weight = copy.weight;
        this.location = copy.location;
        this.info = copy.info;
        this.tag = copy.tag;
        this.isVisit = copy.isVisit;
        this.neighbors = cloneNeighbors(copy.neighbors);
    }

    private HashMap<Integer, edge_data> cloneNeighbors(HashMap<Integer, edge_data> edges) {
        HashMap<Integer, edge_data> copyHash = new HashMap<Integer, edge_data>();
        Iterator<Integer> itr = edges.keySet().iterator();
        edge e;
        int next;
        while (itr.hasNext()) {
            next = itr.next();
            e = new edge((edge) edges.get(next)); //sent to the copy constructor of edge.
            copyHash.put(next, e); //put in the new HashMap
        }
        return copyHash;
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public Point3D getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Point3D p) {
        this.location = p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public HashMap<Integer, edge_data> getNeighbors(){
        return this.neighbors;
    }

    public void setNeighbors(int dest, edge e) {
        this.neighbors.put(dest, e);
    }

    public boolean getIsVisit() {
        return this.isVisit;
    }

    public void setIsVisit(boolean b) {
        this.isVisit = b;
    }

    /** (implements compareTo method in Comparable interface).
     * This method compare between two objects (it will be nodes).
     * The two objects are: this Node & another one we get like object.
     * It used for the priorityQueue in shortestPath method (Graph_Algo class).
     * The comparison is made by weight parameter of the Nodes (bigger weight --> bigger Node).
     * @param obj = the Node to compare with
     * @return: 1 - if this Node is bigger
     *          0 - if the given Node is bigger
     */
    @Override
    public int compareTo(Object obj) {
        if(!(obj instanceof Node))
            throw new RuntimeException("CAN NOT COMPARE: not instanceof Node!");
        if(this.weight > ((Node)obj).weight)
            return 1;
        return 0;
    }

}

