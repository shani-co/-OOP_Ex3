package gameClient;

import Server.*;
import algorithms.Graph_Algo;
import dataStructure.*;
import gameObjects.Fruit;
import gameObjects.FruitCollector;
import gameObjects.Robot;
import gameObjects.RobotCollector;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarException;

public class AutoDrive implements Runnable {

    private Graph_Algo ga;
    private game_service game;
    private int scenario_num;
    private RobotCollector RC = new RobotCollector();
    private FruitCollector FC = new FruitCollector();
    double minX,maxX,minY,maxY;

    public AutoDrive() {
        askScenarioNum();
        String g = game.getGraph();
        DGraph dGraph = new DGraph(g);
        this.ga = new Graph_Algo(dGraph);
        init();
     }

    public AutoDrive(Graph_Algo ga) {
        askScenarioNum();
        this.ga = ga;
        init();
    }

    public AutoDrive(DGraph graph) {
        askScenarioNum();
        Graph_Algo ga = new Graph_Algo();
        ga.init(graph);
        this.ga = ga;
        init();
    }

    private void askScenarioNum() {
        try {
            String num = (String) JOptionPane.showInputDialog(null,
                    "Please choose the scenario num of the game\n"+
                            "enter a number from 0 to 23");
            checkScenarioNum(Integer.parseInt(num));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wrong input",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkScenarioNum(int scenario_num) {
        if(scenario_num < 0 || scenario_num > 23)
            throw new RuntimeException("The number of game you chose is not exist!");
        else {
            this.scenario_num = scenario_num;
            game_service game = Game_Server.getServer(scenario_num);
            this.game = game;
        }
    }

    private void findFirstLocationToRobot() {
        int fruitSize = initFruits();
        int robotSize = initRobots();
        int min = Math.min(fruitSize, robotSize);
        for (int i = 0; i < min; i++) {
            Fruit f = FC.getFruit(i);
            f.findEdge(this.ga.getG());
            game.addRobot(f.getSRC().getKey());
        }
        if (min < robotSize) { //there are more robots to locate
            for (int i = 0; i < robotSize - min; i++) {
                game.addRobot(i);
            }
        }
        List<String> robots = game.getRobots();
        for(int i = 0; i < robots.size(); i++) {
            Robot f = new Robot(robots.get(i));
            RC.addRobot(f);
        }
        game.startGame();
    }

    private int initRobots() {
        int robotsSize = 0;
        try {
            JSONObject line = new JSONObject(game.toString());
            robotsSize = line.getJSONObject("GameServer").getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return robotsSize;
    }

    private int initFruits() {
        List<String> fruits = game.getFruits();
        for(int i = 0; i < fruits.size(); i++) {
            Fruit f = new Fruit(fruits.get(i));
            FC.addFruit(f);
            f.setID(i);
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
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
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
        showTime(maxX, minY,minX,maxY);
        StdDraw.show();
    }

    private void showTime(double maxX, double minY, double minX, double maxY) {
        long time = game.timeToEnd();
        StdDraw.setPenColor();
        StdDraw.setFont(new Font("Ariel", Font.PLAIN, 15));
        StdDraw.text((maxX+minX)*0.5, (0.1*minY+maxY*0.9), "Time Left: "+time/1000+"."+time%1000);
        if(time%1000 == -1) {
            StdDraw.picture((maxX+minX)*0.5, (maxY+minY)*0.5, "data\\gameOver.jpg");
            String gameServer = game.toString();
            try {
                JSONObject line = new JSONObject(gameServer);
                double score = line.getJSONObject("GameServer").getDouble("grade");
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text((maxX+minX)*0.5, 0.3*maxY+minY*0.7, "YOUR SCORE: "+score+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while(game.isRunning()) {
            moveRobots();
            paint();
        }
    }

    private void moveRobots() {
        List<String> log = game.move();
        if (log != null) {
            long t = game.timeToEnd();
            for (int i = 0; i < log.size(); i++) {
                String robot_json = log.get(i);
                System.out.println(robot_json);
                Robot r = RC.getRobot(i);
                r.build(robot_json);
                if (r.getSrc() == r.getDest()) r.setDest(-1);
                if ((r.getDest() == -1) && (r.getMyPath().isEmpty())) {
                    List<node_data> path = nextStep(r);
                    r.setMyPath((ArrayList<node_data>) path);
                }
                if((r.getDest() == -1) && !(r.getMyPath().isEmpty())) {
                    int key_next;
                    key_next = r.getMyPath().get(0).getKey();
                    r.setDest(key_next);
                    game.chooseNextEdge(i, key_next);
                    System.out.println("Turn to node: " + r.getDest() + "  time to end:" + (t / 1000));
                    r.getMyPath().remove(0);
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
                    if(shortPathRes == 0) break; //to not do unnecessary iterations (0 is the minimum for sure)
                }
            }
        }
        if(chosen != null) chosen.setIsVisit(true);
        return res;
    }

    /**
     * a very simple random walk implementation!
     * @param g
     * @param src
     * @return
     */
    private static int nextNode(graph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }

}
