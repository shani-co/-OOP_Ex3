package gameClient;

import Server.*;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.node_data;
import gameObjects.Fruit;
import gameObjects.FruitCollector;
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


    private void findFirstLocationToRobot() {
        try {
            new FruitCollector().fruitID_SRC(this.game, this.graph);
            Iterator <String> itrRobot = game.getRobots().iterator();
            Iterator <String> itrFruit = game.getFruits().iterator();
            while(itrRobot.hasNext()) {
                String locateRobot =itrRobot.next();
                String fruit=itrFruit.next();
                Fruit newFruit =  new Fruit(fruit);
                newFruit.setSrc(graph);
                game.addRobot(newFruit.getSRC().getKey());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //List<node_data> shortestPath(int src, int dest)
   /* private int nearestFruit(Robot id){



        return Fruit.getID:
    }
*/
}
