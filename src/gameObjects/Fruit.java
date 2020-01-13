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

public class Fruit {

    private double value; //?
    private int type;
    private double loc_x;
    private double loc_y;
    private String fileName;
    private int ID;
    private node_data src;
    private node_data dest;


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

    public void setType(int type) {
        this.type = type;
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //constructor
    public Fruit(String s) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*private void findEdge(DGraph graph) {
        Iterator<node_data> itr_nodes = graph.getV().iterator();
        while (itr_nodes.hasNext()) {
            node_data src = itr_nodes.next();
            Iterator<edge_data> itr_edges = graph.getE()
        }
    }*/

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
                    if(type == 1) {
                        this.src = src;
                        this.dest = dest;
                    }
                    if(type == -1) {
                        this.src = dest;
                        this.dest = src;
                    }
                    break;
                }
            }
        }
    }


}
