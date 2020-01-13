package gameClient;

import Server.*;
import algorithms.Graph_Algo;
import dataStructure.Node;
import dataStructure.node_data;
import gameObjects.Fruit;
import gameObjects.FruitCollector;
import gameObjects.Robot;
import gameObjects.RobotCollector;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarException;

public class AutoDrive implements Runnable {

    private Graph_Algo ga;
    private game_service game;
    private int scenario_num;
    private RobotCollector RC = new RobotCollector();
    private FruitCollector FC = new FruitCollector();

    public AutoDrive() {
        this.scenario_num = 0;
        init();
    }

    public AutoDrive(Graph_Algo graph, game_service game) {
        this.scenario_num=0;
        this.ga = graph;
        this.game = game;
        init();
    }

    public AutoDrive(Graph_Algo graph, game_service game, int scenario_num) {
        this.scenario_num = scenario_num;
        this.ga = graph;
        this.game = game;
        init();
    }

    private void findFirstLocationToRobot() {
        int fruitSize = initFruits();
        int robotSize = initRobots();
        int min = Math.min(fruitSize, robotSize);

        System.out.println("f: "+ fruitSize);
        System.out.println("r: "+ robotSize);

        for (int i = 0; i < min; i++) {
            Fruit f = FC.getFruit(i);
            f.findEdge(this.ga.getG());

            System.out.println(f.getSRC().getKey());

            game.addRobot(f.getSRC().getKey());
            //            RC.addRobot(r); ??
        }
        if (min < robotSize) { //there are more robots to locate
            for (int i = 0; i < robotSize - min; i++) {
                game.addRobot(i);
            }
        }
    }

    private int initRobots() {
        int robotsSize = 0;
        try {
            JSONObject line = new JSONObject(game.toString());
            robotsSize = line.getJSONObject("GameServer").getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("initRobot " + robotsSize);

        /*for(int i = 0; i < robotsSize; i++) {
            Robot r = new Robot(i);
            RC.addRobot(r);
        }*/
        return robotsSize;
    }

    private int initFruits() {
        List<String> fruits = game.getFruits();
        for(int i = 0; i < fruits.size(); i++) {
            Fruit f = new Fruit(fruits.get(i));
            FC.addFruit(f);
        }
        return fruits.size();
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
        findFirstLocationToRobot();
        drawEdges();
        drawVertices();
        drawFruits();
        StdDraw.enableDoubleBuffering();
        drawRobots();
        StdDraw.show();
        //showTime() ?
        run();
    }

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

    private void drawFruits() {
        for(Fruit f : FC.getFC()) {
            StdDraw.picture(f.getX(), f.getY(), f.getFileName());
        }
    }

    private void drawRobots() {
        for(Robot r : RC.getRC()) {
            StdDraw.picture(r.getX(), r.getY(), r.getFileName());
        }
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
                    List<node_data> path = nextStep(robot);
                    int key_next;
                    if(!path.isEmpty()) {
                        for(int i = 0; i < path.size(); i++) {
                            key_next = path.get(i).getKey();
                            robot.setDest(key_next);
                            game.chooseNextEdge(j, robot.getDest());
                            System.out.println("Turn to node: " + robot.getDest() + "  time to end:" + (t / 1000));
                        }
                    }
                }
            }
        }
    }

    private List<node_data> nextStep(Robot SRC) {
        double minPath = Double.POSITIVE_INFINITY;
        List<node_data> res = new ArrayList<node_data>();
        Iterator<Fruit> itrFruit = FC.getFC().iterator();
        Fruit chosen = null;
        while (itrFruit.hasNext()) {
            Fruit f = itrFruit.next();
            if(!f.getIsVisit()) {
                f.findEdge(ga.getG()); //update it's src and dest
                double shortPathRes = ga.shortestPathDist(SRC.getSrc(), f.getSRC().getKey());
                if(shortPathRes < minPath) {
                    minPath = shortPathRes;
                    res = ga.shortestPath(SRC.getSrc(), f.getSRC().getKey());
                    res.add(f.getDEST());
                    chosen = f;
                }
            }
        }
        if(chosen != null) chosen.setIsVisit(true);
        return res;
    }

}
