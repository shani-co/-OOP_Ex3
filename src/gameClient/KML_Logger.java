package gameClient;

import dataStructure.node_data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KML_Logger {

    //attributes
    private String kml; //the string that we'll build in the code, on KML form.
    private AutoDrive Agame; //initialized iff the game is automatic game
    private MyGameGUI Mgame; //initialized iff the game is manual game

    //get
    public String getKml() {
        return this.kml;
    }

    //constructors

    public KML_Logger(AutoDrive g) {
        Agame = g;
        this.kml = "";
        build();
    }

    public KML_Logger(MyGameGUI g) {
        Mgame = g;
        this.kml = "";
        build();
    }

    /**
     * The main function that create the KML string with all the data.
     * Initializes it on the kml field.
     */
    private void build() {
        kml+= upper();
        //We set random numbers to differentiate between the different icons:
        kml+= IconNodes(4);
        kml+= IconRobots(3);
        kml+= IconFruits(2); //banana
        kml+= IconFruits(1); //apple
        //loops goes all the graph's nodes and make a placemark.
        if(Agame != null) {
            for (node_data n : Agame.getGa().getG().getV()) { //put vertices
                double x = n.getLocation().x();
                double y = n.getLocation().y();
                placemark(x, y, 4);
            }
        }
        if(Mgame != null) {
            for (node_data n : Mgame.getGA().getG().getV()) { //put vertices
                double x = n.getLocation().x();
                double y = n.getLocation().y();
                placemark(x, y, 4);
            }
        }
    }

    /**
     * The beginning of the KML file (constant).
     * @return s = a string representing the start of the KML.
     */
    private String upper() {
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://earth.google.com/kml/2.2\">\n"+
                "<Document>\n" +
                "\t<name>Points with TimeStamps</name>\n";
        return s;
    }

    /**
     * create a string that represent a 'placemark' Kml code snippet
     * @param x = the x coordinate
     * @param y = the y coordinate
     * @param id = type
     */
    public void placemark(double x, double y, int id) {
        String s = "    <Placemark>\n" +
                "      <TimeStamp>\n" +
                "        <when>";
        if(id == 4) s+= date();
        s+= "</when>\n" +
                "      </TimeStamp>\n" +
                "      <styleUrl>"+id+"</styleUrl>\n" +
                "      <Point>\n" +
                "        <coordinates>" + x+","+y+",0" + "</coordinates>\n" +
                "      </Point>\n" +
                "    </Placemark>\n";
        kml+= s;
    }

    /**
     * Method that uses Date class of java to init finalDate, that used while creating placemark.
     * @return String that represents the real time now.
     */
    private String date() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        String timeStr = df.format(date);
        String timeStr2 = df2.format(date);
        String finalDate = timeStr+"T"+timeStr2+"Z";
        return finalDate;
    }

    /**
     * Puts a Icon of robot (from google earth's icons)
     * @param id = a number that represents the type
     * @return s = a string that represent a 'style' Kml code snippet
     */
    private String IconRobots(int id){
        String s = "<Style id=\""+id+"\">\n" +
                "      <IconStyle>\n" +
                "        <Icon>\n" +
                "          <href>http://maps.google.com/mapfiles/kml/shapes/man.png</href>\n" +
                "        </Icon>\n" +
                "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
                "      </IconStyle>\n" +
                "    </Style>\n";
        return s;
    }

    /**
     * puts icons of Fruits: YELLOW sign for banana
     *                       GREEN sign for apple
     * @param id = the relevant type
     * @return s = a string that represent a 'style' Kml code snippet
     */
    private String IconFruits(int id){
        String s = "";
        //apple
        if(id == 1) {
            s = "<Style id=\"" + id + "\">\n" +
                    "      <IconStyle>\n" +
                    "        <Icon>\n" +
                    "          <href>http://maps.google.com/mapfiles/kml/paddle/grn-blank.png</href>\n" +
                    "        </Icon>\n" +
                    "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
                    "      </IconStyle>\n" +
                    "    </Style>\n";
        }
        //banana
        else if(id == 2){
            s = "<Style id=\"" + id + "\">\n" +
                    "      <IconStyle>\n" +
                    "        <Icon>\n" +
                    "          <href>http://maps.google.com/mapfiles/kml/paddle/ylw-blank.png</href>\n" +
                    "        </Icon>\n" +
                    "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
                    "      </IconStyle>\n" +
                    "    </Style>\n";
        }
        return s;
    }

    /**
     * Add icon of vertex or node: a pink thumbtack.
     *  @param id = the relevant type
     *  @return s = a string that represent a 'style' Kml code snippet
     */
    private String IconNodes(int id){
        String s = "<Style id=\""+id+"\">\n" +
                "      <IconStyle>\n" +
                "        <Icon>\n" +
                "          <href>http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png</href>\n" +
                "        </Icon>\n" +
                "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
                "      </IconStyle>\n" +
                "    </Style>\n";
        return s;
    }

    /**
     * Add the end of the KML file and try to create a string
     * (using BufferWriter)
     */
    public void toKML_file() {
        kml+= "  </Document>\n";
        kml+= "</kml>";
        try {
            String filename = "";
            if(Mgame != null) filename = "ManualGame/" + this.Mgame.getScenario_num() + ".kml";
            if(Agame != null) filename = "AutoGame/" + this.Agame.getScenario_num() + ".kml";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(kml);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
