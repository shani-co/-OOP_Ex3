package gameClient;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import org.json.JSONException;
import org.json.JSONObject;
import algorithms.Graph_Algo;
import java.util.Iterator;
import java.util.List;

public class AutoDrive {

    private DGraph graph;
    private game_service game;

    public AutoDrive() {

    }

    public AutoDrive(DGraph graph, game_service game) {
        this.graph = graph;
        this.game = game;
    }

    public int findFirstLocation() {
        List<String> fruits = game.getFruits();
        try {
            for (int i = 0; i < fruits.size(); i++) {
                JSONObject line = new JSONObject(fruits.get(i));
                String pos = line.getJSONObject("Fruit").getString("pos");
                String[] spl = pos.split(",");
                double xFruit = Double.parseDouble(spl[0]);
                double yFruit = Double.parseDouble(spl[1]);
               /* String fruitFile = "";
                if (type == 1) fruitFile = "data\\apple.png"; //apple
                if (type == -1) fruitFile = "data\\banana.png"; //banana
*/
                Iterator<node_data> itr = this.graph.getV().iterator();
                while (itr.hasNext()) {
                    node_data src = itr.next();
                    Iterator<Integer> itrDest = ((Node) src).getNeighbors().keySet().iterator();
                    while (itrDest.hasNext()) {
                        node_data dest = itr.next();
                        double Incline = ((src.getLocation().y()-dest.getLocation().y())/(src.getLocation().x()-dest.getLocation().x()));
                        if((yFruit-src.getLocation().y())==(Incline*(xFruit-src.getLocation().x()))){
                            game.addRobot(src.getKey());
                        }

                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

}