package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.node_data;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import java.awt.*;
import java.util.List;
import java.lang.String;

public class myGameGUI {

    private Graph_Algo ga;
    private int scenario_num;
    private game_service game;

    //getter
    public Graph_Algo getGA() {
        return this.ga;
    }

    public int getScenario_num() {
        return this.scenario_num;
    }

    public game_service getGameService() {
        return this.game;
    }

    //constructor
    public myGameGUI(int scenario_num, game_service game) {
        checkScenarioNum(scenario_num, game);
        this.ga = new Graph_Algo();
        init();
    }

    public myGameGUI(Graph_Algo ga, int scenario_num, game_service game) {
        checkScenarioNum(scenario_num, game);
        this.ga = ga;
        init();
    }

    public myGameGUI(DGraph g, int scenario_num, game_service game) {
        checkScenarioNum(scenario_num, game);
        Graph_Algo ga = new Graph_Algo();
        ga.init(g);
        this.ga = ga;
        init();
    }

    private void checkScenarioNum(int scenario_num, game_service game) {
        if(scenario_num < 0 || scenario_num > 23)
            throw new RuntimeException("The number of game you chose is not exist!");
        else {
            this.game = game;
            this.scenario_num = scenario_num;
        }
    }

    private void init() {
        StdDraw.setCanvasSize(1000, 650);

        //find the scale size
        double INF = Double.MAX_VALUE, MINF = Double.MIN_VALUE;
        double minX = INF, maxX = MINF, minY = INF, maxY = MINF;
        for (node_data n : ga.getG().getV()) {
            Point3D p = n.getLocation();
            if (p.x() > maxX) maxX = p.x();
            if (p.x() < minX) minX = p.x();
            if (p.y() > maxY) maxY = p.y();
            if (p.y() < minY) minY = p.y();
        }
        //add a number that to the length & width to be space
        double per = 0.000015;
        StdDraw.setXscale(minX - per * minX, maxX + per * maxX);
        StdDraw.setYscale(minY - per * minY, maxY + per * maxY);

        //addBackgroundImg(maxX, maxY, minX, minY, per);

        drawEdges(maxX, maxY, minX, minY, per);

        drawVertices();

        drawFruits(maxX, maxY, minX, minY, per);
    }

    /**
     * add background image of the map to the scale
     * @param maxX
     * @param maxY
     * @param minX
     * @param minY
     */
    private void addBackgroundImg(double maxX, double maxY, double minX, double minY, double per) { //***
        String gameServer = game.toString();
        try {
            JSONObject line = new JSONObject(gameServer);
            String img = line.getJSONObject("GameServer").getString("graph");
            double center_x = (minX-per*minX)+(maxX+per*maxX) / 2;
            double center_y = (minY-per*minY)+(maxY+per*maxY) / 2;
            String[] spl = img.split("/");
            String filename = spl[0]+"\\"+spl[1]+".png";
            StdDraw.picture(center_x, center_y, filename, 32, 32);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * draw all the edges that come out of each vertex
     * @param maxX
     * @param maxY
     * @param minX
     * @param minY
     * @param per
     */
    private void drawEdges(double maxX, double maxY, double minX, double minY, double per) {
        for (node_data n : ga.getG().getV()) {
            for (int dest : ((Node) n).getNeighbors().keySet()) {
                Point3D p_src = n.getLocation();
                Point3D p_dest = ga.getG().getNode(dest).getLocation();
                StdDraw.setPenColor(Color.PINK);
                StdDraw.setPenRadius(0.006);
                StdDraw.line(p_src.x(), p_src.y(), p_dest.x(), p_dest.y());

                //calculate the space to take from dest, to put the arrow
                double x_space = p_src.x() * 0.1 + p_dest.x() * 0.9;
                double y_space = p_src.y() * 0.1 + p_dest.y() * 0.9;
                //add a triangle that represents the head of the arrow
                double width = (maxX - minX +  Math.abs(per*maxX) + Math.abs(per*minX)) / 55;
                double height = (maxY - minY + Math.abs(per*maxY) + Math.abs(per*minY)) / 45;
                StdDraw.picture(x_space, y_space, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQOAV_8t4Cpta3s1rFNJSvA9OyGs9eyKfuV4Zb0sPE8-3mEZj3O&s",
                        width, height);

                //calculate the space to take from dest, to put the edge's weight
                x_space = p_src.x() * 0.22 + p_dest.x() * 0.88;
                y_space = p_src.y() * 0.22 + p_dest.y() * 0.88;
                //draw the edge's weight
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.setPenRadius(0.04);
                String w = Double.toString(ga.getG().getEdge(n.getKey(), dest).getWeight());
                StdDraw.text(x_space, y_space + 0.15, w);
            }
        }
    }

    /**
     * draw each vertex & it's key
     */
    private void drawVertices() {
        for (node_data n : ga.getG().getV()) {
            StdDraw.setPenColor(Color.black);
            StdDraw.setPenRadius(0.03);
            Point3D p = n.getLocation();
            StdDraw.point(p.x(), p.y());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setFont(new Font("Ariel", Font.PLAIN, 10));
            StdDraw.text(p.x(), p.y(), n.getKey() + "");
        }
    }

    /**
     * add fruits (banana & apple) to the GUI, on it's places
     */
    private void drawFruits(double maxX, double maxY, double minX, double minY, double per) {
        List<String> fruits = game.getFruits();
        try {
            for(int i = 0; i < fruits.size(); i++) {
                JSONObject line = new JSONObject(fruits.get(i));
                //value ????
                int type = line.getJSONObject("Fruit").getInt("type");
                String pos = line.getJSONObject("Fruit").getString("pos");
                String[] spl = pos.split(",");
                double x = Double.parseDouble(spl[0]);
                double y = Double.parseDouble(spl[1]);
                String fruitFile = "";
                if(type == 1) fruitFile = "data\\apple.png"; //apple
                if(type == -1) fruitFile = "data\\banana.png"; //banana
                StdDraw.picture(x, y, fruitFile);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
