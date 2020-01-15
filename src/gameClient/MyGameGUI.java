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

public class MyGameGUI implements Runnable {

    private Graph_Algo ga;
    private int scenario_num;
    private game_service game;
    private RobotCollector RC = new RobotCollector();
    private FruitCollector FC = new FruitCollector();
    double minX,maxX,minY,maxY;

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
    public MyGameGUI() {
        askScenarioNum();
        String g = game.getGraph();
        DGraph dGraph = new DGraph(g);
        this.ga = new Graph_Algo(dGraph);
        init();
    }

    public MyGameGUI(Graph_Algo ga) {
        askScenarioNum();
        this.ga = ga;
        init();
    }

    public MyGameGUI(DGraph g) {
        askScenarioNum();
        Graph_Algo ga = new Graph_Algo();
        ga.init(g);
        this.ga = ga;
        init();
    }

    private void askScenarioNum() {
        try {
            String num = (String)JOptionPane.showInputDialog(null,
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
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        explainGame(); //a window with the things that the user should do
        run();
    }

    private void explainGame() {
        JOptionPane.showMessageDialog(null,"When the game starts, click the next vertex you want to reach. \n" +
                "The goal: Collect as much fruit as you can to earn the most points. GOOD LUCK");
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
        showTime(maxX, minY,minX,maxY);
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
