package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.*;
import gameObjects.Fruit;
import gameObjects.FruitCollector;
import gameObjects.RobotCollector;
import gameObjects.Robot;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.lang.String;

public class myGameGUI implements Runnable {

    private Graph_Algo ga;
    private int scenario_num;
    private game_service game;
    private RobotCollector RC = new RobotCollector();
    private FruitCollector FC = new FruitCollector();

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
        drawEdges();
        drawVertices();
        drawFruits();
        StdDraw.enableDoubleBuffering();
        initRobots();
        drawRobots();
        StdDraw.show();
        //explainGame(); //a window with the things that the user should do
        //showTime() ?
        run();
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
     */
    private void drawEdges() {
        StdDraw.clear();
        for (node_data n : ga.getG().getV()) {
            for (int dest : ((Node) n).getNeighbors().keySet()) {
                Point3D p_src = n.getLocation();
                Point3D p_dest = ga.getG().getNode(dest).getLocation();
                StdDraw.setPenColor(Color.GRAY);
                StdDraw.setPenRadius(0.003);
                StdDraw.line(p_src.x(), p_src.y(), p_dest.x(), p_dest.y());
                //calculate the space to take from dest, to put the arrow
                double x_space = p_src.x() * 0.1 + p_dest.x() * 0.9;
                double y_space = p_src.y() * 0.1 + p_dest.y() * 0.9;
                //add a triangle that represents the head of the arrow
                StdDraw.picture(x_space, y_space, "data\\play-arrow.png");
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
            StdDraw.setPenColor(Color.pink);
            StdDraw.setPenRadius(0.03);
            Point3D p = n.getLocation();
            StdDraw.point(p.x(), p.y());
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setFont(new Font("Ariel", Font.PLAIN, 10));
            StdDraw.text(p.x(), p.y(), n.getKey() + "");
        }
    }

    /**
     * add fruits (banana & apple) to the GUI, on it's places
     */
    private void drawFruits() {
        List<String> fruits = game.getFruits();
        for(int i = 0; i < fruits.size(); i++) {
            Fruit f = new Fruit(fruits.get(i));
            FC.addFruit(f);
            StdDraw.picture(f.getX(), f.getY(), f.getFileName());
        }
    }

    private void initRobots() {
        try {
            JSONObject line = new JSONObject(game.toString());
            int robotsSize = line.getJSONObject("GameServer").getInt("robots");
            for (int i = 0; i < robotsSize; i++) {
                posRobot_manual(i);
            }
            List<String> robots = game.getRobots();
            for(int i = 0; i < robots.size(); i++) {
                Robot r = new Robot(robots.get(i));
                RC.addRobot(r);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void drawRobots() {
        for(int i = 0; i < RC.getSize(); i++) {
            Robot r = RC.getRobot(i);
            StdDraw.picture(r.getX(), r.getY(), r.getFileName());
        }
    }

    private void posRobot_manual(int count) {
        String vertex = (String)JOptionPane.showInputDialog(null,
                "Please choose the key of the vertex you want to put the " + count + "th robot into");
        try {
            int key = Integer.parseInt(vertex);
            game.addRobot(key);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please write an integer that represents a vertex's key on the graph",
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("try again");
        }
    }

    private void moveRobots() {
        List<String> log = game.move();
        if (log != null) {
            long t = game.timeToEnd();
            for (int j = 0; j < log.size(); j++) {
                String robot_json = log.get(j);
                System.out.println(robot_json);
                Robot robot = RC.getRobot(j);
                robot.build(robot_json);
                if (robot.getDest() == -1) {
                    int key_next = nextNode();
                    if(key_next != -1)
                        robot.setDest(key_next);
                    game.chooseNextEdge(j, robot.getDest());
                    System.out.println("Turn to node: " + robot.getDest() + "  time to end:" + (t / 1000));
                }
            }
        }
    }

    private int nextNode() {
        if(StdDraw.isMousePressed()) {
            Node n = findNearestNode(StdDraw.mouseX(), StdDraw.mouseY());
            if(n != null) return n.getKey();
        }
        return -1;
    }

    /**
     * find the nearest vertex to the click location (with epsilon) and move there the robot
     * @param
     */
    private Node findNearestNode(double x, double y) {
        double eps = 0.0005;
        for(node_data n : ga.getG().getV()) {
            boolean x_fine = (n.getLocation().x() >= x - eps) && (n.getLocation().x() <= x + eps);
            if(!x_fine) continue;
            boolean y_fine = (n.getLocation().y() >= y - eps) && (n.getLocation().y() <= y + eps);
            if(y_fine) return (Node)n; //both x and y are fine
        }
        return null;
    }

    private void paint(){
        drawEdges();
        drawVertices();
        drawFruits();
        drawRobots();
        StdDraw.show();
    }

    @Override
    public void run() {
        game.startGame();
        while(game.isRunning()) {
            moveRobots();
            paint();
        }
    }

}
