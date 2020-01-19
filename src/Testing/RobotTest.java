import Server.Game_Server;
import dataStructure.DGraph;
import gameObjects.Fruit;
import gameObjects.Robot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    private DGraph graph = new DGraph(Game_Server.getServer(0).getGraph());
    private String robot_str = "{\"Robot\":{\"id\":0,\"value\":5.0,\"src\":6,\"dest\":5,\"speed\":1.0,\"pos\":\"35.20765730999008,32.10490915623714,0.0\"}}";
    private Robot ROBOT = new Robot(robot_str);
    private String robot_str2 = "{\"Robot\":{\"id\":2,\"value\":11.0,\"src\":8,\"dest\":9,\"speed\":2.0,\"pos\":\"35.1,32.43,0.0\"}}";

    @Test
    void getId() {
        assertEquals(ROBOT.getId(), 0);
    }

    @Test
    void getSrc() {
        assertEquals(ROBOT.getSrc(), 6);
    }

    @Test
    void getDest() {
        assertEquals(ROBOT.getDest(), 5);
    }

    @Test
    void getSpeed() {
        assertEquals(ROBOT.getSpeed(), 1);
    }

    @Test
    void getX() {
        assertEquals(ROBOT.getX(), 35.20765730999008);
    }

    @Test
    void getY() {
        assertEquals(ROBOT.getY(), 32.10490915623714);
    }

    @Test
    void getFileName() {
        assertEquals(ROBOT.getFileName(), "data\\spiderman.png");
    }

    @Test
    void setId() {
        ROBOT.setId(1);
        assertEquals(ROBOT.getId(), 1);
    }

    @Test
    void setSrc() {
        ROBOT.setSrc(7);
        assertEquals(ROBOT.getSrc(), 7);
    }

    @Test
    void setDest() {
        ROBOT.setDest(9);
        assertEquals(ROBOT.getDest(), 9);
    }

    @Test
    void setSpeed() {
        ROBOT.setSpeed(5.0);
        assertEquals(ROBOT.getSpeed(), 5);
    }

    @Test
    void setX() {
        ROBOT.setX(31.333);
        assertEquals(ROBOT.getX(), 31.333);
    }

    @Test
    void setY() {
        ROBOT.setY(30.8);
        assertEquals(ROBOT.getY(), 30.8);
    }

    @Test
    void build() {
        ROBOT.build(robot_str2);
        assertEquals(ROBOT.getId(), 2);
        assertEquals(ROBOT.getSpeed(), 2);
        assertEquals(ROBOT.getSrc(), 8);
        assertEquals(ROBOT.getDest(), 9);
        assertEquals(ROBOT.getX(), 35.1);
        assertEquals(ROBOT.getY(), 32.43);
    }
}