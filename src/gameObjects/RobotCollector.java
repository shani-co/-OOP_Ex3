package gameObjects;

import java.util.ArrayList;
import java.util.List;

public class RobotCollector {

    private List<Robot> RC = new ArrayList<>();

    //constructor

    //methods

    public int getSize() {
        return this.RC.size();
    }

    public void addRobot(Robot r) {
        this.RC.add(r);
    }

    public Robot getRobot(int id) {
        for (Robot r : RC) {
            if(r.getId() == id) return r;
        }
        return null;
    }

}
