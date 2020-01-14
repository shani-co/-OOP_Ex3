package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;

public class testGUI {

    public static void main(String[] args) {

        int scenario_num = 19;
        game_service game = Game_Server.getServer(scenario_num);
        String g = game.getGraph();
        DGraph dGraph = new DGraph(g);
        Graph_Algo ga = new Graph_Algo(dGraph);
        //myGameGUI gameGUI = new myGameGUI(dGraph, scenario_num, game);
        AutoDrive game1 = new AutoDrive(ga, game);
    }
}
