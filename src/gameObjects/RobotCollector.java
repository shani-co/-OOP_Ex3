package gameObjects;

import java.util.Collection;

public class RobotCollector {

    private Collection<Robot> RC;

    //constructor

    //methods

    public int getSize() {
        return this.RC.size();
    }

    public void addRobot(Robot r) {
        this.RC.add(r);
    }

}
