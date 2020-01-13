package gameObjects;

import Server.game_service;
import org.json.JSONException;
import org.json.JSONObject;

public class Robot {
    private int id;
    private int src;
    private int dest;
    private double speed;
    private double loc_x;
    private double loc_y;
    private String fileName;

    //getters and setters

    public int getId() {
        return id;
    }

    public int getSrc() {
        return src;
    }

    public int getDest() {
        return dest;
    }

    public double getSpeed() {
        return speed;
    }

    public double getX() {
        return loc_x;
    }

    public double getY() {
        return loc_y;
    }

    public String getFileName() {
        return fileName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setX(double loc_x) {
        this.loc_x = loc_x;
    }

    public void setY(double loc_y) {
        this.loc_y = loc_y;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //constructor
    public Robot(String s) {
        build(s);
    }

    public void build(String s) {
        try {
            JSONObject robot = new JSONObject(s);
            JSONObject rob = robot.getJSONObject("Robot");
            this.id = rob.getInt("id");
            this.src = rob.getInt("src");
            this.dest = rob.getInt("dest");
            this.speed = rob.getDouble("speed");
            String pos = rob.getString("pos");
            String[] spl = pos.split(",");
            this.loc_x = Double.parseDouble(spl[0]);
            this.loc_y = Double.parseDouble(spl[1]);
            if(id == 0) this.fileName = "data\\spiderman.png";
            if(id == 1) this.fileName = "data\\deadpool.png";
            if(id == 2) this.fileName = "data\\wolverine.png";
            if(id == 3) this.fileName = "data\\thor.png";
            if(id == 4) this.fileName = "data\\groot.png";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
