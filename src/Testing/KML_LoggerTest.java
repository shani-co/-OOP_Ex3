import Server.Game_Server;
import dataStructure.DGraph;
import gameClient.AutoDrive;
import gameClient.KML_Logger;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class KML_LoggerTest {

    private DGraph graph = new DGraph(Game_Server.getServer(0).getGraph());
    private AutoDrive game = new AutoDrive(graph);

    private String date() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        String timeStr = df.format(date);
        String timeStr2 = df2.format(date);
        String finalDate = timeStr+"T"+timeStr2+"Z";
        return finalDate;
    }

    @Test
    void toKML_file() {
        KML_Logger kml = new KML_Logger(game);
        boolean saved = true;
        try {
            kml.toKML_file();
        } catch (Exception e) {
            saved = false;
        }
        assertTrue(saved);
    }

    @Test
    void placemarkTest() {
        KML_Logger kml = new KML_Logger(game);
        Point3D p = graph.getNode(0).getLocation();
        kml.placemark(p.x(), p.y(), 4);
        String actual = kml.getKml();
        String expected = "    <Placemark>\n" +
                "      <TimeStamp>\n" +
                "        <when>"+date()+"</when>\n" +
                "      </TimeStamp>\n" +
                "      <styleUrl>"+4+"</styleUrl>\n" +
                "      <Point>\n" +
                "        <coordinates>" + p.x()+","+p.y()+",0" + "</coordinates>\n" +
                "      </Point>\n" +
                "    </Placemark>\n";
        assertTrue(actual.contains(expected));
    }
}