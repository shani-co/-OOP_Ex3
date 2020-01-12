package gameObjects;

import java.util.ArrayList;
import java.util.List;

public class FruitCollector {

    private List<Fruit> FC = new ArrayList<>();

    //constructor

    //methods

    public int getSize() {
        return this.FC.size();
    }

    public void addFruit(Fruit f) {
        this.FC.add(f);
    }
}
