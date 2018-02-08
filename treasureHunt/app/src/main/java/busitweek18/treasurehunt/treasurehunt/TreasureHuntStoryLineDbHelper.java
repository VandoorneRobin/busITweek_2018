package busitweek18.treasurehunt.treasurehunt;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import cz.mendelu.busItWeek.library.StoryLineDatabaseHelper;
import cz.mendelu.busItWeek.library.builder.StoryLineBuilder;

/**
 * Created by Axel Köhler on 08-Feb-18.
 */



public class TreasureHuntStoryLineDbHelper extends StoryLineDatabaseHelper {
    ArrayList<LatLng> locations = new ArrayList<LatLng> ();
    public enum Terrain {REAL, CAMPUS};

    // CHANGE FOR CAMPUS/REAL LOCATIONS:
    private Terrain playTerrain = Terrain.CAMPUS;

    public TreasureHuntStoryLineDbHelper() {
        super(1);
        addLocations();
        ArrayList<LatLng> locations;
    }

    @Override
    protected void onCreate(StoryLineBuilder builder) {
        // Mahen Theater
        builder.addGPSTask("1")
                .radius(20)
                .location(locations.get(0).latitude, locations.get(0).longitude)
                .victoryPoints(5)
                .simplePuzzle()
                .question("You're at the Mahem Theater") // TODO add question
                .answer("")                              // TODO add answer
                .puzzleDone()
                .taskDone();
        // Church of St. Josef
        builder.addBeaconTask("2")
                .location(locations.get(1).latitude, locations.get(1).longitude)
                .beacon(30265, 64901)            // BLUE beacon
                .simplePuzzle()
                .question("What's the name of this church?") // TODO add question
                .answer("Church of St. Josef")               // TODO add answer
                .puzzleDone()
                .taskDone();

        // Cathedral of St. Peter and Paul
        builder.addBeaconTask("3")
                .location(locations.get(2).latitude, locations.get(2).longitude)
                .beacon(63187, 53881)               // YELLOW beacon
                .simplePuzzle()
                .question("What's the name of this church?")    // TODO add question
                .answer("Church of St. Josef")                  // TODO add answer
                .puzzleDone()
                .taskDone();

        // Cabbage Market Square
        builder.addCodeTask("4")
                .location(locations.get(3).latitude, locations.get(3).longitude)
                .qr("CabbageSquareMarket") // qr-code file found in git (folder 'qr_codes')
                .simplePuzzle()
                .question("What famous composer played in this " +
                        "building when he was only 6 years old?")       // TODO add question
                .answer("Mozart")                                       // TODO add answer
                .puzzleDone()
                .taskDone();

        // Brněnské kolo
        builder.addBeaconTask("5")
                .location(locations.get(4).latitude, locations.get(4).longitude)
                .beacon(23482, 14779)                          // RED beacon
                .simplePuzzle()
                .question("What animal, that people saw as a dragon, " +
                        "is hanging from the ceiling here?")               // TODO add question
                .answer("Crocodile")                                       // TODO add answer
                .puzzleDone()
                .taskDone();

        // Astronomical Clock
        builder.addCodeTask("6")
                .location(locations.get(5).latitude, locations.get(5).longitude)
                .qr("glass marble") // qr-code file found in git (folder 'qr_codes')
                .simplePuzzle()
                .question("At what time is a marble released " +
                        "from the clock everyday (HH:mm)?")                 // TODO add question
                .answer("11:00")                                            // TODO add answer
                .puzzleDone()
                .taskDone();

        // Špilberk Castle
        builder.addGPSTask("7")
                .location(locations.get(6).latitude, locations.get(6).longitude)
                .radius(20)
                .choicePuzzle()
                .question("What are you, stupid?")        // TODO add question
                .addChoice("Yes", true)      // TODO add answers
                .addChoice("No", false)
                .puzzleDone()
                .taskDone();

        //
        builder.addGPSTask("Treasure")
                .location(locations.get(7).latitude, locations.get(7).longitude)
                .radius(20)
                .choicePuzzle()
                .question("You've reached yor destination! Press 'done'," +
                        " go inside and enjoy a good meal!")        // TODO add question
                .addChoice("Done", true)                // TODO add answers
                .puzzleDone()
                .taskDone();
    }

    private void addLocations() {
        switch (playTerrain) {
            case CAMPUS:
                locations.add(new LatLng(49.20997, 16.61479)); // Mahen Theater (STARTPOINT)
                locations.add(new LatLng(49.21053, 16.61532)); // Church of St. Josef
                locations.add(new LatLng(49.21096, 16.61648)); // Cathedral of St. Peter and Paul
                locations.add(new LatLng(49.21197, 16.61624)); // Cabbage Market Square
                locations.add(new LatLng(49.21198, 16.61697)); // Brněnské kolo
                locations.add(new LatLng(49.21095, 16.61755)); // Astronomical Clock
                locations.add(new LatLng(49.20995, 16.61646)); // Špilberk Castle
                locations.add(new LatLng(49.20962, 16.61499)); // Úvozna (ENDPOINT)
                break;
            case REAL:
                locations.add(new LatLng(49.195799, 16.613774)); // Mahen Theater (STARTPOINT)
                locations.add(new LatLng(49.193100, 16.612106)); // Church of St. Josef
                locations.add(new LatLng(49.191033, 16.606986)); // Cathedral of St. Peter and Paul
                locations.add(new LatLng(49.192366, 16.608915)); // Cabbage Market Square
                locations.add(new LatLng(49.193149, 16.608712)); // Brněnské kolo
                locations.add(new LatLng(49.194809, 16.608588)); // Astronomical Clock
                locations.add(new LatLng(49.194706, 16.598401)); // Špilberk Castle
                locations.add(new LatLng(49.199024, 16.593342)); // Úvozna (ENDPOINT)
        }
    }
}
