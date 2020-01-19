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
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;
import utils.Point3D;
import utils.StdDraw;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AutoDrive implements Runnable {

    private KML_Logger kml;
    private Thread t;
    private Graph_Algo ga;
    private game_service game;
    private int scenario_num;
    private RobotCollector RC = new RobotCollector();
    private FruitCollector FC = new FruitCollector();
    double minX,maxX,minY,maxY;

    //getters & setters

    public Graph_Algo getGa() {
        return ga;
    }

    public void setGa(Graph_Algo ga) {
        this.ga = ga;
    }

    public game_service getGame() {
        return game;
    }

    public void setGame(game_service game) {
        this.game = game;
    }

    public int getScenario_num() {
        return scenario_num;
    }

    public void setScenario_num(int scenario_num) {
        this.scenario_num = scenario_num;
    }

    public RobotCollector getRC() {
        return RC;
    }

    public void setRC(RobotCollector RC) {
        this.RC = RC;
    }

    public FruitCollector getFC() {
        return FC;
    }

    public void setFC(FruitCollector FC) {
        this.FC = FC;
    }

    //constructor
    public AutoDrive() {
        askScenarioNum();
        String g = game.getGraph();
        DGraph dGraph = new DGraph(g);
        this.ga = new Graph_Algo(dGraph);
        init();
     }

    //constructor
    public AutoDrive(Graph_Algo ga) {
        askScenarioNum();
        this.ga = ga;
        init();
    }

    //constructor
    public AutoDrive(DGraph graph) {
        askScenarioNum();
        Graph_Algo ga = new Graph_Algo();
        ga.init(graph);
        this.ga = ga;
        init();
    }

    /**
     * ask from the player in which map he want to play
     */
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

    /**
     * check that the player chose an existing map
     * @param scenario_num the map number
     */
    private void checkScenarioNum(int scenario_num) {
        if(scenario_num < 0 || scenario_num > 23)
            throw new RuntimeException("The number of game you chose is not exist!");
        else {
            this.scenario_num = scenario_num;
            game_service game = Game_Server.getServer(scenario_num);
            this.game = game;
        }
    }

    /**
     * this function locate the best start node for the robots.
     * she locate the src node of each fruit, and put the robot on that src.
     */
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
    }

    /**
     * method that makes a JSONObject and checks the robot's size
     * @return robot's size
     */
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

    /**
     * add all the fruits to the FC
     * @return fruit's size
     */
    private int initFruits() {
        List<String> fruits = game.getFruits();
        for(int i = 0; i < fruits.size(); i++) {
            Fruit f = new Fruit(fruits.get(i));
            FC.addFruit(f);
            f.setID(i);
        }
        return fruits.size();
    }

    /**
     * build the show window.
     * draw on the window: the graph, all the robots, fruits.
     */
    private void init() {
        kml = new KML_Logger(this);
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

        backgroundImg();
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

        t = new Thread(this);
        t.start();
    }

    /**
     * add background image to the game window
     */
    private void backgroundImg() {
        StdDraw.clear();
        //StdDraw.picture((maxX+minX)/2, (maxY+minY)/2, "data\\cityBackground.jpg", 0.05,
               // 0.02);
        StdDraw.picture((maxX+minX)*0.5, (maxY+minY)*0.5, "data\\cityBackground.jpg");
    }

    /**
     * draw all the edges of the graph in the show window.
     */
    private void drawEdges() {
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
     * draw all the vertices of the graph in the show window.
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
     * draw all the fruits of the graph in the show window.
     * we took a picture of a banana and apple, and locate them in the coordinates that we get.
     */
    private void drawFruits() {
        try {

            List<String> log = game.getFruits();
            Iterator<String> f_iter = log.iterator();
            while (f_iter.hasNext()) {
                JSONObject line = new JSONObject(f_iter.next());
                String pos = line.getJSONObject("Fruit").getString("pos");
                int type = line.getJSONObject("Fruit").getInt("type");
                String[] spl = pos.split(",");
                if(type==-1) {
                    StdDraw.picture(Double.parseDouble(spl[0]), Double.parseDouble(spl[1]), "data\\banana.png");
                }else {
                    StdDraw.picture(Double.parseDouble(spl[0]), Double.parseDouble(spl[1]), "data\\apple.png");
                }
            }
        }catch (Exception e){


        }
//        System.out.println(FC.getSize());
//        for(Fruit f : FC.getFC()) {
//            System.out.println(f.getID());
//            StdDraw.picture(f.getX(), f.getY(), f.getFileName());
//        }
    }

    /**
     * draw all the robots of the graph in the show window.
     * we took a picture of marvel characters, and locate them in the coordinates that we get.
     */
    private void drawRobots() {
        for(Robot r : RC.getRC()) {
            StdDraw.picture(r.getX(), r.getY(), r.getFileName());
        }
    }


    /**
     * paint all in the show window
     */
    private void paint(){
        backgroundImg();
        drawEdges();
        drawVertices();
        System.out.println("fruit draw");
        drawFruits();
        drawRobots();
        showTime();
        StdDraw.show();
    }

    /**
     * show how much time left to play.
     */
    private void showTime() {
        long time = game.timeToEnd();
        StdDraw.setPenColor();
        StdDraw.setFont(new Font("Ariel", Font.PLAIN, 15));
        StdDraw.text((maxX+minX)*0.5, (0.1*minY+maxY*0.9), "Time Left: "+time/1000+"."+time%1000);
        if(time%1000 == -1) gameOver();
    }

    /**
     * this function start the game.
     * while the game is running, the robots will move to the next fruits
     * and the show window draw himself all over again.
     * when the game is end, all the data of the robots saved in KML file.
     */
    @Override
    public void run() {
        game.startGame();
        music();
        while(game.isRunning()) {
            moveRobots();
            paint();
        }
        gameOver();
        askKML();
        String gameServer = game.toString();
        try {
            JSONObject line = new JSONObject(gameServer);
            double score = line.getJSONObject("GameServer").getDouble("grade");
            System.out.println("SCORE: "+score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * ask if we want to keep the data in KML file.
     * if yes- activate another function.
     */
    private void askKML() {
        Object[] options = {"YES", "NO"};
        int n = JOptionPane.showOptionDialog(null, "Do you want to save your game as KML file?",
                "CHOOSE",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);
        if (n == 0) { //the answer is YES
            kml.toKML_file();
        }
    }

    /**
     * draw message -game over, in the end of the game.
     */
    private void gameOver() {
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

    /**
     * this function always Renews the fruits and bots from the  Json file,
     * and send the data to KML file.
     * its also check if know what the next step that they need do do (To get to the fruits).
     * if they dont know (the list that contains the way is empty), we go to
     * another function that find the next dest.
     */
    private void moveRobots() {
        List<String> log = game.move();
        for(Fruit f : FC.getFC()) {
            for(String s : game.getFruits()) {
                System.out.println(s);
                f.build(s);
                if(f.getType() == 1) { //apple
                    kml.placemark(f.getX(), f.getY(), 1);
                }
                else{ //banana
                    kml.placemark(f.getX(), f.getY(), 2);
                }
            }
        }
        if (log != null) {
            long t = game.timeToEnd();
            for (int i = 0; i < log.size(); i++) {
                String robot_json = log.get(i);
                Robot r = RC.getRobot(i);
                System.out.println(robot_json);
                r.build(robot_json);
                kml.placemark(r.getX(), r.getY(), 3);
                if ((r.getDest() == -1) && (r.getMyPath().isEmpty())) {
                    List<node_data> path = nextStep(r);
                    r.setMyPath((ArrayList<node_data>) path);
                }
                else if((r.getDest() == -1) && !(r.getMyPath().isEmpty())) {
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

    /**
     * we build a list of node_data that contains the way from the robot src to his dest.
     * we using the function shortestPath-> to find the shortest path from one vertex to onenter vertex on a graph.
     * @param SRC =src of the robot
     * @return the way that the robot need to do.
     */
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
                    res.clear();
                    minPath = shortPathRes;
                    res.addAll(ga.shortestPath(SRC.getSrc(), f.getSRC().getKey()));
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
     * start play the background music when the game begging.
     */
    public static void music()
    {
        AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;
        AudioData MD;

        ContinuousAudioDataStream loop = null;

        try
        {
            InputStream test = new FileInputStream("data\\song.wav");
            BGM = new AudioStream(test);
            AudioPlayer.player.start(BGM);
            MD = BGM.getData();
            loop = new ContinuousAudioDataStream(MD);

        }
        catch(FileNotFoundException e){
            System.out.print(e.toString());
        }
        catch(IOException error)
        {
            System.out.print(error.toString());
        }
        MGP.start(loop);
    }

}
