package gameClient;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import oop_dataStructure.OOP_DGraph;
import org.json.JSONException;
import org.json.JSONObject;
//import com.google.gson.Gson;

import java.io.*;
import java.util.Iterator;

public class testGUI {

    public static void main(String[] args) {

        int scenario_num = 2;
        game_service game = Game_Server.getServer(scenario_num);
        String g = game.getGraph();
        DGraph dGraph = new DGraph(g);
        myGameGUI gameGUI = new myGameGUI(dGraph, scenario_num);
    }
}
