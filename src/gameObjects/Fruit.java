package gameObjects;

import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import org.json.JSONException;
import org.json.JSONObject;
import utils.StdDraw;

import java.awt.geom.Point2D;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

public class Fruit {

    private double value;
    private int type;
    private double loc_x;
    private double loc_y;
    private String fileName;
    private int ID;
    private node_data src;
    private node_data dest;
    private boolean isVisit = false;

    /**
     * getters and setters
     * @return
     */

    public boolean getIsVisit() {
        return this.isVisit;
    }

    public void setIsVisit(boolean b) {
        this.isVisit = b;
    }

    public node_data getSRC(){return src;}

    public node_data getDEST(){return dest;}

    public void setSrc(DGraph graph) {
        findEdge(graph);
    }

    public int getID(){return ID;}

    public void setID(int id){this.ID=id;}

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public double getX() {
        return loc_x;
    }

    public void setX(double loc_x) {
        this.loc_x = loc_x;
    }

    public double getY() {
        return loc_y;
    }

    public void setY(double loc_y) {
        this.loc_y = loc_y;
    }

    public String getFileName() {
        return fileName;
    }

    //constructor
    public Fruit(String s) {
       build(s);
    }

    /**
     * take data from json file ,and build a new fruit.
     * @param s
     */
    public void build(String s) {
        System.out.println(s);
        try {
            JSONObject line = new JSONObject(s);
            this.value = line.getJSONObject("Fruit").getDouble("value");
            this.type = line.getJSONObject("Fruit").getInt("type");
            String pos = line.getJSONObject("Fruit").getString("pos");
            String[] spl = pos.split(",");
            this.loc_x = Double.parseDouble(spl[0]);
            this.loc_y = Double.parseDouble(spl[1]);
            if(type == 1) this.fileName = "data\\apple.png"; //apple
            if(type == -1) this.fileName = "data\\banana.png"; //banana
            this.isVisit = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * find the edge that the fruit is stand on.
     * update what the src and dest node of the fruit.
     * @param graph- the game
     */
    public void findEdge(DGraph graph) {
        Iterator<node_data> itr = graph.getV().iterator();
        while (itr.hasNext()) {
            node_data src = itr.next();
            Iterator<Integer> itrDestOfEdge = ((Node) src).getNeighbors().keySet().iterator();
            while (itrDestOfEdge.hasNext()) {
                int destOfEdge = itrDestOfEdge.next();
                node_data dest = graph.getNode(destOfEdge);
                double distanceEdge = Point2D.distance(src.getLocation().x(), src.getLocation().y(), dest.getLocation().x(), dest.getLocation().y());
                double distance_ToFruitEdge = Point2D.distance(src.getLocation().x(), src.getLocation().y(), loc_x,loc_y);
                double distance_FromFruitEdge = Point2D.distance(loc_x,loc_y, dest.getLocation().x(), dest.getLocation().y());
                if (Math.abs(distanceEdge - (distance_ToFruitEdge + distance_FromFruitEdge)) <= 0.00004) {
                    if(type == 1 && src.getKey()<dest.getKey()) {
                        this.src = src;
                        this.dest = dest;
                    }
                    if(type == -1 && src.getKey()<dest.getKey()) {
                        this.src = dest;
                        this.dest = src;
                    }
                    break;
                }
            }
        }
    }


}
