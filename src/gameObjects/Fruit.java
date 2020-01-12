package gameObjects;

import org.json.JSONException;
import org.json.JSONObject;
import utils.StdDraw;

public class Fruit {

    private double value; //?
    private int type;
    private double loc_x;
    private double loc_y;
    private String fileName;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getX() {
        return loc_x;
    }

    public void setX(double loc_x) {
        this.loc_x = loc_x;
    }

    public double getY() {
        return loc_y;
    }

    public void setY(double loc_y) {
        this.loc_y = loc_y;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //constructor
    public Fruit(String s) {
        try {
            JSONObject line = new JSONObject(s);
            this.value = line.getJSONObject("Fruit").getDouble("value");
            this.type = line.getJSONObject("Fruit").getInt("type");
            String pos = line.getJSONObject("Fruit").getString("pos");
            String[] spl = pos.split(",");
            this.loc_x = Double.parseDouble(spl[0]);
            this.loc_y = Double.parseDouble(spl[1]);
            if(type == 1) this.fileName = "data\\apple.png"; //apple
            if(type == -1) this.fileName = "data\\banana.png"; //banana
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
