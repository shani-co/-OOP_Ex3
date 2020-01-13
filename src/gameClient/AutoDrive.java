package gameClient;

import Server.*;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.node_data;
import gameObjects.Fruit;
import gameObjects.Robot;
import oop_utils.OOP_Point3D;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import java.util.Iterator;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.concurrent.ThreadLocalRandom;

public class AutoDrive {

    private DGraph graph;
    private game_service game;
    private int scenario_num;


    public AutoDrive() {
        this.scenario_num=0;
        this.game=null;
        this.graph=null;
    }

    public AutoDrive(DGraph graph, game_service game) {
        this.scenario_num=0;
        this.graph = graph;
        this.game = game;
    }
    public AutoDrive(DGraph graph, game_service game, int scenario_num) {
        this.scenario_num = scenario_num;
        this.graph = graph;
        this.game = game;
    }


    private void findFirstLocation() {
        List<String> fruits = game.getFruits();
        try {
            for (int i = 0; i < fruits.size(); i++) {
                JSONObject line = new JSONObject(fruits.get(i));
                String pos = line.getJSONObject("Fruit").getString("pos");
                String[] spl = pos.split(",");
                double xFruit = Double.parseDouble(spl[0]);
                double yFruit = Double.parseDouble(spl[1]);
                Iterator<node_data> itr = this.graph.getV().iterator();
                while (itr.hasNext()) {
                    node_data src = itr.next();
                    Iterator<Integer> itrDest = ((Node) src).getNeighbors().keySet().iterator();
                    while (itrDest.hasNext()) {
                       int dest1 = itrDest.next();
                        node_data dest = graph.getNode(dest1);
                       double distance = Point2D.distance(src.getLocation().x(),src.getLocation().y(),dest.getLocation().x(),dest.getLocation().y());
                        double distance_ToFruit = Point2D.distance(src.getLocation().x(),src.getLocation().y(),xFruit,yFruit);
                        double distance_FromFruit = Point2D.distance(xFruit,yFruit,dest.getLocation().x(),dest.getLocation().y());
                        if(Math.abs(distance-(distance_ToFruit+distance_FromFruit))<=0.00004){
                             game.addRobot(src.getKey());
                        }

                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //List<node_data> shortestPath(int src, int dest)
    private int nearestFruit(Robot id){



        return Fruit.getID:
    }

}
