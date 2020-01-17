package gameClient;

import dataStructure.node_data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KML_Logger {

    private String kml;
    private AutoDrive Agame;
    private MyGameGUI Mgame;

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

    private String date(){
        Date date =new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        String timeStr = df.format(date);
        String timeStr2 = df2.format(date);
        String finalDate = timeStr+"T"+timeStr2+"Z";
        return finalDate;
    }

    private void build() {
        kml+= upper();
        kml+=IconNodes(4);
        kml+= IconRobots(3);
        kml+=IconFruits(2);
        kml+=IconFruits(1);
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

    public void toKML_file() {
        kml+= "  </Document>\n";
        kml+= "</kml>";
        try {
            String filename = "";
            if(Mgame != null) filename = "ManualGame.kml";
            if(Agame != null) filename = "AutoGame.kml";
            BufferedWriter writer = new BufferedWriter(new FileWriter("YourGame.kml"));
            writer.write(kml);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void placemark( double x, double y, int id) {
        String s = "    <Placemark>\n" +
                "      <TimeStamp>\n" +
                "        <when>"+date()+"</when>\n" +
                "      </TimeStamp>\n" +
                "      <styleUrl>"+id+"</styleUrl>\n" +
                "      <Point>\n" +
                "        <coordinates>" + x+","+y+",0" + "</coordinates>\n" +
                "      </Point>\n" +
                "    </Placemark>\n";
        kml+= s;
    }

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
        // banana
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

    private String upper() {
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://earth.google.com/kml/2.2\">\n"+
                "<Document>\n" +
                "\t<name>Points with TimeStamps</name>\n";
        return s;
    }
}
