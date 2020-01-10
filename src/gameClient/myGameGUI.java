package gameClient;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.node_data;
import utils.Point3D;
import utils.StdDraw;

import java.awt.*;

public class myGameGUI {

    private Graph_Algo ga;
    private int scenario_num;

    //getter
    public Graph_Algo getGA() {
        return this.ga;
    }

    //constructor
    public myGameGUI(int scenario_num) {
        checkScenarioNum(scenario_num);
        this.ga = new Graph_Algo();
        init();
    }

    public myGameGUI(Graph_Algo ga, int scenario_num) {
        checkScenarioNum(scenario_num);
        this.ga = ga;
        init();
    }

    public myGameGUI(DGraph g, int scenario_num) {
        checkScenarioNum(scenario_num);
        Graph_Algo ga = new Graph_Algo();
        ga.init(g);
        this.ga = ga;
        init();
    }

    private void checkScenarioNum(int scenario_num) {
        if(scenario_num < 0 || scenario_num > 23)
            throw new RuntimeException("The number of game you chose is not exist!");
        else
            this.scenario_num = scenario_num;
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

        //draw all the edges that come out of each vertex:

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
                System.out.println(width);
                double height = (maxY - minY + Math.abs(per*maxY) + Math.abs(per*minY)) / 45;
                System.out.println(height);
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
        //draw each vertex & it's key
        for (node_data n : ga.getG().getV()) {
            StdDraw.setPenColor(Color.black);
            StdDraw.setPenRadius(0.05);
            Point3D p = n.getLocation();
            StdDraw.point(p.x(), p.y());
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(p.x(), p.y(), n.getKey() + "");
        }
    }

}
