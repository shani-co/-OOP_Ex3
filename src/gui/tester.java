package gui;

import algorithms.Graph_Algo;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.node_data;
import utils.Point3D;

import java.util.concurrent.TimeUnit;

public class tester
{
    static node_data n1;
    static node_data n2;
    static node_data n3;
    static node_data n4;
    static node_data n5;
    static node_data n6;
    static node_data n7;
    static node_data n8;
    static node_data n9;

    public static void graph1() {
        Graph_GUI gg = new Graph_GUI();
        Node n1 = new Node(0, new Point3D(3,5));
        Node n2 = new Node(1, new Point3D(-2,5));
        Node n3 = new Node(2, new Point3D(3,-5));
        Node n4 = new Node(3, new Point3D(2,7));
        gg.getGA().getG().addNode(n1);
        gg.getGA().getG().addNode(n2);
        gg.getGA().getG().addNode(n3);
        gg.getGA().getG().addNode(n4);
        gg.getGA().getG().connect(0,1, 8);
        gg.getGA().getG().connect(1,0, 15);
        gg.getGA().getG().connect(2,3, 4);
        gg.getGA().getG().connect(1,2, 7);
        gg.getGA().getG().connect(1,3, 30);
    }

    public static void graph2() {
        DGraph g = new DGraph();
        Node n1 = new Node(0, new Point3D(3,5));
        Node n2 = new Node(1, new Point3D(-2,5));
        Node n3 = new Node(2, new Point3D(3,-5));
        Node n4 = new Node(3, new Point3D(2,7));
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.connect(0,1, 8);
        g.connect(1,0, 15);
        g.connect(2,3, 4);
        g.connect(1,2, 7);
        g.connect(1,3, 30);
        Graph_Algo ga = new Graph_Algo();
        ga.init(g);
        Graph_GUI gg = new Graph_GUI(ga);
        //g.addNode(new Node(6));
    }

    public static void graph3() {
        Graph_Algo GA = new Graph_Algo();

        Node N1 = new Node(1, new Point3D(0,-5));
        Node N2 = new Node(2, new Point3D(3,4));
        Node N3 = new Node(3, new Point3D(4,-10));
        Node N4 = new Node(4, new Point3D(2,11));
        Node N5 = new Node(5, new Point3D(-15,4));

        GA.getG().addNode(N1);
        GA.getG().addNode(N2);
        GA.getG().addNode(N3);
        GA.getG().addNode(N4);
        GA.getG().addNode(N5);

        GA.getG().connect(1,5,3);
        GA.getG().connect(5,2,2);
        GA.getG().connect(2,3,5);
        GA.getG().connect(3,4,6);
        GA.getG().connect(4,3,7);
        GA.getG().connect(3,1,4);
        GA.getG().connect(3,5,1);

        Graph_GUI gg = new Graph_GUI(GA);

        for(int i = 0; i < 10;i++){
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GA.getG().addNode(new Node(6+i, new Point3D(-7+i, 15)));
        }
    }

    public static void graph4() {
        Graph_Algo ga = new Graph_Algo();
        ga.init("A0.txt");
        Graph_GUI gg = new Graph_GUI(ga);
    }

    public static void graph5() {
        DGraph dGraph = new DGraph();

        n1 = new Node(0, new Point3D(-1,1));
        n2 = new Node(1, new Point3D(-5,6));
        n3 = new Node(2, new Point3D(-4,3));
        n4 = new Node(3, new Point3D(-6,7));
        n5 = new Node(4, new Point3D(-32,54));
        n6 = new Node(5, new Point3D(23,65));
        n7 = new Node(6, new Point3D(-19,33));
        n8 = new Node(7, new Point3D(-45,72));
        n9 = new Node(8, new Point3D(-12,85));

        dGraph.addNode(n1);
        dGraph.addNode(n2);
        dGraph.addNode(n3);
        dGraph.addNode(n4);
        dGraph.addNode(n5);
        dGraph.addNode(n6);
        dGraph.addNode(n7);
        dGraph.addNode(n8);
        dGraph.addNode(n9);

        dGraph.connect(n1.getKey(), n2.getKey(),1);
        dGraph.connect(n1.getKey(), n3.getKey(),4);
        dGraph.connect(n1.getKey(), n4.getKey(),9);
        dGraph.connect(n2.getKey(), n1.getKey(),1);
        dGraph.connect(n2.getKey(), n4.getKey(),2);
        dGraph.connect(n3.getKey(), n2.getKey(),1);
        dGraph.connect(n3.getKey(), n4.getKey(),3);
        dGraph.connect(n4.getKey(), n3.getKey(),10);
        dGraph.connect(n5.getKey(), n6.getKey(),1);
        dGraph.connect(n5.getKey(), n7.getKey(),2);
        dGraph.connect(n5.getKey(), n8.getKey(),3);
        dGraph.connect(n6.getKey(), n5.getKey(),1);
        dGraph.connect(n6.getKey(), n8.getKey(),5);
        dGraph.connect(n7.getKey(), n6.getKey(),6);
        dGraph.connect(n7.getKey(), n8.getKey(),7);
        dGraph.connect(n8.getKey(), n7.getKey(),9);
        dGraph.connect(n1.getKey(),n9.getKey(),10);
        dGraph.connect(n9.getKey(),n1.getKey(),1);
        dGraph.connect(n5.getKey(),n9.getKey(),1);
        dGraph.connect(n9.getKey(),n5.getKey(),1);

        Graph_GUI gg = new Graph_GUI(dGraph);
    }

    public static void main(String[] args) {
        //graph2();
        //graph3();
        //graph4();
        graph5();
    }
}
