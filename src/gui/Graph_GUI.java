package gui;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.node_data;
import utils.Point3D;
import utils.StdDraw;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents graphic interface, and enable to save graph, load it from a saved file, show it.
 * In addition, enable to run algorithms on it (all the algorithms from Graph_Algo class), and represent the answers.
 * Also can add / remove Nodes and edges.
 */
public class Graph_GUI extends JFrame implements ActionListener, Runnable {

    private Graph_Algo ga;
    private int mc;

    //getter
    public Graph_Algo getGA() {
        return this.ga;
    }

    //constructor
    public Graph_GUI() {
        this.ga = new Graph_Algo();
        init();
    }

    public Graph_GUI(Graph_Algo ga) {
        this.ga = ga;
        init();
    }

    public Graph_GUI(DGraph g) {
        Graph_Algo ga = new Graph_Algo();
        ga.init(g);
        this.ga = ga;
        init();
    }

    /**
     * This method using STDraw library (utils) to draw the graph.
     * [look at our wiki to see the explanation of all the parts on the graph].
     *
     * @param algo = a boolean parameter that says if the method was called by an algorithm (in actionPerformed): shortestPath / TSP.
     * @param path = a List that represents a path. will be define only if algo is true, otherwise will be null.
     */
    private void drawGraph(boolean algo, List<node_data> path) {
        StdDraw.setCanvasSize(800, 600);

        //find the scale size
        double INF = Double.MAX_VALUE, MINF = Double.MIN_VALUE;
        double minX = INF, maxX = MINF, minY = INF, maxY = MINF;
        for (node_data n : ga.getGraph().getV()) {
            Point3D p = n.getLocation();
            if (p.x() > maxX) maxX = p.x();
            if (p.x() < minX) minX = p.x();
            if (p.y() > maxY) maxY = p.y();
            if (p.y() < minY) minY = p.y();
        }
        int space = 3; //a number that we'll add to the length & width to be space
        StdDraw.setXscale(minX - space, maxX + space);
        StdDraw.setYscale(minY - space, maxY + space);

        //draw all the edges that come out of each vertex:

        if (algo)  //if the method was triggered by an algorithm (shortestPath/TSP)
            paintPath(path, minX, maxX, maxY);
        for (node_data n : ga.getGraph().getV()) {
            for (int dest : ((Node) n).getNeighbors().keySet()) {
                String e = n.getKey() + "," + dest; //e = string representing an edge that begins in n and ends in dest.
                String e_reverse = dest + "," + n.getKey();
                Point3D p_src = n.getLocation();
                Point3D p_dest = ga.getGraph().getNode(dest).getLocation();
                //checks if e is part of the path (so there is already an orange line there).
                if (!algo || (path == null) || (!likeString(path).contains(e) && !likeString(path).contains(e_reverse))) {
                    StdDraw.setPenColor(Color.PINK);
                    StdDraw.setPenRadius(0.006);
                    StdDraw.line(p_src.x(), p_src.y(), p_dest.x(), p_dest.y());
                }
                //calculate the space to take from dest, to put the arrow
                double x_space = p_src.x() * 0.05 + p_dest.x() * 0.95;
                double y_space = p_src.y() * 0.05 + p_dest.y() * 0.95;
                //add a triangle that represents the head of the arrow
                double width = (maxX - minX + 6) / 55;
                double height = (maxY - minY + 6) / 45;
                StdDraw.picture(x_space, y_space, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQOAV_8t4Cpta3s1rFNJSvA9OyGs9eyKfuV4Zb0sPE8-3mEZj3O&s",
                        width, height);
                //calculate the space to take from dest, to put the edge's weight
                x_space = p_src.x() * 0.22 + p_dest.x() * 0.88;
                y_space = p_src.y() * 0.22 + p_dest.y() * 0.88;
                //draw the edge's weight
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.setPenRadius(0.04);
                String w = Double.toString(ga.getGraph().getEdge(n.getKey(), dest).getWeight());
                StdDraw.text(x_space, y_space + 0.15, w);
            }
        }

        //draw each vertex & it's key
        for (node_data n : ga.getGraph().getV()) {
            StdDraw.setPenColor(Color.black);
            StdDraw.setPenRadius(0.05);
            Point3D p = n.getLocation();
            StdDraw.point(p.x(), p.y());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(p.x(), p.y(), n.getKey() + "");
        }
    }

    private String likeString(List<node_data> path) {
        Iterator<node_data> itr = path.iterator();
        String res = "";
        while (itr.hasNext()) {
            res += itr.next().getKey() + ",";
        }
        return res;
    }

    /**
     * Method that "paint" the path answer on TSP and shortestPathDist algorithms.
     *
     * @param path = the result of the algorithm.
     * @param minX = the minimum x's node value.
     * @param maxX = the maximum x's node value.
     * @param maxY = the maximum y's node value.
     *             ! all the max and min values sent here to determine the location of the path text (n1 --> n2 --> ...)
     */
    private void paintPath(List<node_data> path, double minX, double maxX, double maxY) {
        StdDraw.setPenColor(Color.orange);
        StdDraw.setPenRadius(0.006);
        double midX = (minX + maxX) / 2;
        if (path == null) {
            StdDraw.setPenRadius(1.5);
            StdDraw.text(midX, maxY + 1, "There is no path");
            return;
        }
        Iterator<node_data> itr = path.iterator();
        node_data prev = null, curr;
        if (itr.hasNext()) prev = itr.next();
        while (itr.hasNext()) { //a loop that draws line between all the vertex in the path result
            curr = itr.next();
            Point3D p_prev = prev.getLocation();
            Point3D p_curr = curr.getLocation();
            StdDraw.line(p_curr.x(), p_curr.y(), p_prev.x(), p_prev.y());
            prev = curr;
        }
        StdDraw.setPenColor(Color.ORANGE);
        StdDraw.setPenRadius(0.1);
        String p = "";
        node_data n;
        itr = path.iterator();
        while (itr.hasNext()) {
            n = itr.next();
            if (itr.hasNext()) p += n.getKey() + " --> "; //we're not in the last one
            else p += n.getKey();
        }
        StdDraw.setPenRadius(1.5);
        StdDraw.text(midX, maxY + 1, "The path is: " + p);
    }

    @Override
    public void run() {
        if (this.mc != this.ga.getG().getMC()){
            this.mc = this.ga.getG().getMC();
            drawGraph(false, null);
        }
    }

    /**
     * Initialization method to init the GUI window
     * The method set menuBar, menus, items - that represent all the options to choose on the program.
     */
    private void init() {
        this.setVisible(true);
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.mc = this.ga.getG().getMC();
        ActionListener listen = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                run();
            }
        };
        javax.swing.Timer t = new javax.swing.Timer(1000, listen);
        t.setRepeats(true);
        t.start();

        MenuBar menuBar = new MenuBar();
        Menu menu1 = new Menu("graph");
        Menu menu2 = new Menu("algorithms");
        Menu menu3 = new Menu("add / remove");
        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);
        this.setMenuBar(menuBar);

        MenuItem item = new MenuItem("show graph");
        item.addActionListener(this);

        MenuItem item1 = new MenuItem("save");
        item1.addActionListener(this);

        MenuItem item2 = new MenuItem("load");
        item2.addActionListener(this);

        menu1.add(item);
        menu1.add(item1);
        menu1.add(item2);

        MenuItem item3 = new MenuItem("isConnected");
        item3.addActionListener(this);

        MenuItem item4 = new MenuItem("shortestPathDist");
        item4.addActionListener(this);

        MenuItem item5 = new MenuItem("shortestPath");
        item5.addActionListener(this);

        MenuItem item6 = new MenuItem("TSP");
        item6.addActionListener(this);

        menu2.add(item3);
        menu2.add(item4);
        menu2.add(item5);
        menu2.add(item6);

        MenuItem item7 = new MenuItem("addNode");
        item7.addActionListener(this);

        MenuItem item8 = new MenuItem("connect");
        item8.addActionListener(this);

        MenuItem item9 = new MenuItem("removeNode");
        item9.addActionListener(this);

        MenuItem item10 = new MenuItem("removeEdge");
        item10.addActionListener(this);

        menu3.add(item7);
        menu3.add(item8);
        menu3.add(item9);
        menu3.add(item10);

        //add am image in background when the GUI window open
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        try {
            String path = "http://www.up2me.co.il/imgs/22922760.png";
            URL url = new URL(path);
            BufferedImage image = ImageIO.read(url);
            JLabel label = new JLabel(new ImageIcon(image));
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.getContentPane().add(label);
            this.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that "listening" to the user's choice and send to the fitting method.
     * if there is an error: we're pop up error window to the user and tell him where did he wrong.
     * @param e = the event that the user choose.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //graph menu
        if (e.getActionCommand() == "show graph") {
            this.drawGraph(false, null);
        }
        if (e.getActionCommand() == "save") {
            String name = (String) JOptionPane.showInputDialog(this, "write the name of the file you want to save: ");
            ga.save(name);
            JOptionPane.showMessageDialog(this,"Object has been serialized");
        }
        if (e.getActionCommand() == "load") {
            String name = (String) JOptionPane.showInputDialog(this, "write the name of the file you want to save: ");
            ga.init(name);
            JOptionPane.showMessageDialog(this,"Object has been deserialized");
            drawGraph(false, null);
        }
        //algorithms menu
        if (e.getActionCommand() == "isConnected") {
            boolean b = ga.isConnected();
            if (b) {
                JOptionPane.showMessageDialog(this, "The graph is connected :)",
                        "isConnected result", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "The graph is NOT connected !",
                        "isConnected result", JOptionPane.PLAIN_MESSAGE);
            }
        }
        if (e.getActionCommand() == "shortestPathDist") {
            String[] s = askPath();
            if (s.length != 2) throwErr();
            int key1 = Integer.parseInt(s[0]);
            int key2 = Integer.parseInt(s[1]);
            try {
                double d = ga.shortestPathDist(key1, key2);
                String message = "The length of the shortest path in your graph is: " + d;
                JOptionPane.showMessageDialog(this, message, "shortestPathDist result", JOptionPane.PLAIN_MESSAGE);
            } catch (Exception err) {
                throwErr();
            }
        }
        if (e.getActionCommand() == "shortestPath") {
            String[] s = askPath();
            if (s.length != 2) throwErr();
            try {
                int key1 = Integer.parseInt(s[0]);
                int key2 = Integer.parseInt(s[1]);
                List<node_data> path = this.ga.shortestPath(key1, key2);
                drawGraph(true, path);
            } catch (Exception err) {
                throwErr();
            }
        }
        if (e.getActionCommand() == "TSP") {
            String path = (String) JOptionPane.showInputDialog(this,
                    "write a list of integers representing the keys\n" +
                            "of all the vertexes you want to reach.\n" +
                            "Format: int,int,..,int, WITHOUT SPACES");
            String[] s = path.split(",");
            List<Integer> reach = new ArrayList<Integer>();
            try {
                for (int i = 0; i < s.length; i++) {
                    int k = Integer.parseInt(s[i]);
                    reach.add(k);
                }
                drawGraph(true, ga.TSP(reach));
            } catch (Exception err) {
                JOptionPane.showMessageDialog(this, "Please choose targets, according to the request ", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        //add / remove menu
        if (e.getActionCommand() == "addNode") {
            String node = (String) JOptionPane.showInputDialog(this, "Write a node's key (ID - unique to every node)\n" +
                    " plus 2 double numbers that represent the location [x,y]\n" +
                    "of a node you want to add \n + " +
                    "Format : int,double,double");
            try {
                String[] spl = node.split(",");
                int key = Integer.parseInt(spl[0]);
                Point3D p = new Point3D(Double.parseDouble(spl[1]), Double.parseDouble(spl[2]));
                ga.getG().addNode(new Node(key, p));
                JOptionPane.showMessageDialog(this, "Successfully added", "Add status", JOptionPane.INFORMATION_MESSAGE);
                drawGraph(false, null);
            } catch (Exception err) {throwErr2();}
        }
        if (e.getActionCommand() == "removeNode") {
            String node = (String) JOptionPane.showInputDialog(this, "Write a node's key of a node you want to remove\n" +
                    "(that already in the graph)");
            try {
                int key = Integer.parseInt(node);
                ga.getG().removeNode(key);
                JOptionPane.showMessageDialog(this, "Successfully removed", "Remove status", JOptionPane.INFORMATION_MESSAGE);
                drawGraph(false, null);
            } catch (Exception err) {throwErr2();}
        }
        if (e.getActionCommand() == "connect") {
            String edge = (String) JOptionPane.showInputDialog(this, "Choose source and destination nodes on the graph\n" +
                    "that you want to connect plus the weight of the edge");
            try {
                String[] spl = edge.split(",");
                int src = Integer.parseInt(spl[0]);
                int dest = Integer.parseInt(spl[1]);
                double weight = Double.parseDouble(spl[2]);
                ga.getG().connect(src,dest,weight);
                JOptionPane.showMessageDialog(this, "Successfully connect", "Connect status", JOptionPane.INFORMATION_MESSAGE);
                drawGraph(false, null);
            } catch (Exception err) {throwErr2();}
        }
        if(e.getActionCommand() == "removeEdge") {
            String edge = (String) JOptionPane.showInputDialog(this, "Choose source and destination nodes on the graph\n" +
                    "that you want to remove the edge between them");
            try {
                String[] spl = edge.split(",");
                int src = Integer.parseInt(spl[0]);
                int dest = Integer.parseInt(spl[1]);
                ga.getG().removeEdge(src,dest);
                JOptionPane.showMessageDialog(this, "Successfully remove", "Remove status", JOptionPane.INFORMATION_MESSAGE);
                drawGraph(false, null);
            } catch (Exception err) {throwErr2();}
        }
    }

    private void throwErr() {
        JOptionPane.showMessageDialog(this,"Please choose 2 keys, according to the request ","Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void throwErr2() {
        JOptionPane.showMessageDialog(this, "Please follow the instructions", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private String[] askPath() {
        String path = (String)JOptionPane.showInputDialog(this,
                "write the source and the destination keys (integers)\n" +
                        "with a ',' between them, WITHOUT SPACES.\n " +
                        "[like this: src,dest]");
        String[] s = path.split(",");
        return s;
    }

}


