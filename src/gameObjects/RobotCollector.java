package gameObjects;

import java.util.ArrayList;
import java.util.Collection;
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

}
