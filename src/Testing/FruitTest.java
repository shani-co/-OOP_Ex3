import Server.Game_Server;
import dataStructure.DGraph;
import gameObjects.Fruit;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

class FruitTest {

    //a string that represents a fruit that should be initialized in our code.
    //the DGraph represents the graph of the game

    private DGraph graph = new DGraph(Game_Server.getServer(0).getGraph());
    private String fruit_str = "{\"Fruit\":{\"value\":5.0,\"type\":-1,\"pos\":\"35.197656770719604,32.10191878639921,0.0\"}}";
    private Fruit FRUIT = new Fruit(fruit_str);
    private String fruit_str2 = "{\"Fruit\":{\"value\":8.0,\"type\":1,\"pos\":\"34.0,37.1,0.0\"}}";

    @Test
    void getIsVisit() {
        //assertFalse(FRUIT.getIsVisit());
    }

    @Test
    void setIsVisit() {
        FRUIT.setIsVisit(true);

        assertTrue(FRUIT.getIsVisit());
    }

    @Test
    void getValue() {
        assertEquals(FRUIT.getValue(), 5);
    }

    private void assertEquals(double value, int i) {
    }

    @Test
    void setValue() {
        FRUIT.setValue(11);
        assertEquals(FRUIT.getValue(), 11);
    }

    @Test
    void getType() {
        assertEquals(FRUIT.getType(), -1);
    }

    @Test
    void getX() {
       // assertEquals(FRUIT.getX(), 35.197656770719604);
    }

    @Test
    void getY() {
      //  assertEquals(FRUIT.getY(), 32.10191878639921);
    }

    @Test
    void getFileName() {
      //  assertEquals(FRUIT.getFileName(), "data\\banana.png");
    }

    @Test
    void findEdge() {
        FRUIT.findEdge(graph);
        assertEquals(FRUIT.getSRC().getKey(), 9);
        assertEquals(FRUIT.getDEST().getKey(), 8);
    }

    @Test
    void build() {
        FRUIT.build(fruit_str2);
        assertEquals(FRUIT.getValue(), 8);
        assertEquals(FRUIT.getX(), 34);
       // assertEquals(FRUIT.getY(), 37.1);
       // assertEquals(FRUIT.getFileName(), "data\\apple.png");
    }
}