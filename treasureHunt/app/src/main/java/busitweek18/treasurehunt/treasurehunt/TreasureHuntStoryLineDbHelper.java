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


//    public enum Terrain {REAL, BAR};
    public enum Terrain {REAL, CAMPUS};

    // CHANGE FOR CAMPUS/REAL LOCATIONS:
//    private Terrain playTerrain = Terrain.valueOf(test());
    private Terrain playTerrain = Terrain.CAMPUS;

    public static HashMap<String, Integer> markerResources = new HashMap<>();

    public TreasureHuntStoryLineDbHelper() {
        super(7);
        addLocations();
        ArrayList<LatLng> locations;
    }

    public Terrain getPlayTerrain() {
        return playTerrain;
    }

    public void setPlayTerrain(Terrain playTerrain) {
        this.playTerrain = playTerrain;
    }

    @Override
    protected void onCreate(StoryLineBuilder builder) {
        // Mahen Theater
        builder.addGPSTask("Mahen Theater")
                .radius(20)
                .location(locations.get(0).latitude, locations.get(0).longitude)
                .victoryPoints(5)
                .simplePuzzle()
                .question("What is written on the building")
                .answer("Mahenovo Divadlo")
                .hint("Mahen Theatre, built as German Deutsches Stadttheater in 1882, was one of the first public buildings in the world lit entirely by electric light. It was built in a combination of Neo-renaissance, Neo-baroque and Neoclassical architectural styles.")
                .puzzleDone()
                .taskDone();
        markerResources.put("1", R.drawable.ic_theatre_masks);

        // Church of St. Josef
        builder.addBeaconTask("Church of St. Josef")
                .location(locations.get(1).latitude, locations.get(1).longitude)

                .beacon(30265, 64901)    // BLUE beacon
                .imageSelectPuzzle()
                .question("Which statue do you see on the church?")
                .hint("There is a famous Brno legend connected to the statue. The statue is placed in een certain position in the direction of Saint Peter and Paul's cathedral. It all arises from a competition between the two churches to build the higher churchtower. ")
                .addImage(R.drawable.correctstatue, true)
                .addImage(R.drawable.wrongstatue1, false)
                .addImage(R.drawable.wrongstatue2, false)
                .addImage(R.drawable.wrongstatue3, false)
                .puzzleDone()
                .taskDone();
        markerResources.put("Church of St. Josef", R.drawable.ic_church);


        // Cathedral of St. Peter and Paul
        builder.addBeaconTask("Cathedral of St. Peter and Paul")
                .location(locations.get(2).latitude, locations.get(2).longitude)
                .beacon(63187, 53881)               // YELLOW beacon
                .simplePuzzle()
                .question("What's the name of this church?")    // TODO add question
                .answer("Church of St. Josef")                  // TODO add answer
                .puzzleDone()
                .taskDone();
        markerResources.put("Cathedral of St. Peter and Paul", R.drawable.ic_church_1);

        // Cabbage Market Square
        builder.addCodeTask("Cabbage Market Square")
                .location(locations.get(3).latitude, locations.get(3).longitude)
                .qr("CabbageSquareMarket") // qr-code file found in git (folder 'qr_codes')
                .choicePuzzle()
                .question("The church is build in two styles, which styles?")
                .hint("Traditionally, the bells of the cathedral are rung at 11 o'clock in the morning instead of 12 noon. The reason for this, according to legend,[1] is that during the Thirty Years' War the invading Swedes had promised, when laying siege to Brno, that they would call off their attack if they had not succeeded in taking the city by midday on the 15th of August. The battle underway, some shrewd citizens decided to ring the bells an hour early on this date, fooling the Swedes into breaking off the siege and leaving empty-handed.")
                .addChoice("Renaissance, Baroque", false)
                .addChoice("Gothic Revival, Neoclassical", false)
                .addChoice("Renaissance, Neoclassical", false)
                .addChoice(	"Baroque, Gothic Revival", true)
                .puzzleDone()
                .taskDone();
        markerResources.put("Cabbage Market Square", R.drawable.ic_augustus_of_prima_porta);

        // Brnenské kolo & Old Town Hall
        builder.addBeaconTask("Old Town Hall")
                .location(locations.get(4).latitude, locations.get(4).longitude)
                .beacon(23482, 14779)                          // RED beacon
                .simplePuzzle()
                .hint("One of the most famous legends in the city of Brno is that of the dragon that once threatened the people. It is said that the beast was savaging the citizens and their livestock and no one seemed to know how to stop it. That is until a visiting butcher had a brainstorm. The tradesman called for an animal hide (ox or sheep depending on the telling) and a large amount of caustic lime. The lime was placed in the hide and sewn up to look like a juicy meal for the dragon. The trojan feast was fed to the dragon and it was successfully vanquished.")
                .question("What animal is the dragon actually?")
                .answer("Crocodile")
                .puzzleDone()
                .taskDone();
        markerResources.put("Old Town Hall", R.drawable.ic_crocodile_facing_right);

        // Astronomical Clock
        builder.addCodeTask("Astronomical Clock")
                .location(locations.get(5).latitude, locations.get(5).longitude)
                .qr("glass marble") // qr-code file found in git (folder 'qr_codes')
                .simplePuzzle()
                .hint("At 11 o’clock every day, the shiny black marble obelisk releases a stream of glass marbles—the color of the city’s coat of arms—that spectators gather around to catch and keep as a souvenir.")
                .question("What is it?")        // TODO beter verwoorden
                .answer("Clock")
                .puzzleDone()
                .taskDone();
        markerResources.put("Astronomical Clock", R.drawable.ic_big_ben);

        // Špilberk Castle
        builder.addGPSTask("Špilberk Castle")
                .location(locations.get(6).latitude, locations.get(6).longitude)
                .radius(20)
                .choicePuzzle()
                .question("What function did the castle had before it became a museum?")
                .addChoice("Market" , false)
                .addChoice("Court", false)
                .addChoice("Prison", true)
                .puzzleDone()
                .taskDone();
        markerResources.put("Špilberk Castle", R.drawable.ic_sand_castle);

        //treasure
        builder.addGPSTask("Treasure")
                .location(locations.get(7).latitude, locations.get(7).longitude)
                .radius(20)
                .choicePuzzle()
                .question("You've reached yor destination! Press 'done'," +
                        " go inside and enjoy a good meal!")        // TODO add question
                .addChoice("Done", true)                // TODO add answers
                .puzzleDone()
                .taskDone();
        markerResources.put("Treasure", R.drawable.ic_logomakr_3uv46h);
    }

//    private String test(){
//
//        Constants c = new Constants();
//
//        return c.getMap();
//
//    }

    private void addLocations() {




        switch (getPlayTerrain()) {

//            case BAR:
//                locations.add(new LatLng(49.20997, 16.61479)); // Mahen Theater (STARTPOINT)
//                locations.add(new LatLng(49.21053, 16.61532)); // Church of St. Josef
//                locations.add(new LatLng(49.21096, 16.61648)); // Cathedral of St. Peter and Paul
//                locations.add(new LatLng(49.21197, 16.61624)); // Cabbage Market Square
//                locations.add(new LatLng(49.21198, 16.61697)); // Brnenské kolo
//                locations.add(new LatLng(49.21095, 16.61755)); // Astronomical Clock
//                locations.add(new LatLng(49.20995, 16.61646)); // Špilberk Castle
//                locations.add(new LatLng(49.20962, 16.61499)); // Úvozna (ENDPOINT)
//                break;
            case CAMPUS:
                locations.add(new LatLng(49.20997, 16.61479)); // Mahen Theater (STARTPOINT)
                locations.add(new LatLng(49.21053, 16.61532)); // Church of St. Josef
                locations.add(new LatLng(49.21096, 16.61648)); // Cathedral of St. Peter and Paul
                locations.add(new LatLng(49.21197, 16.61624)); // Cabbage Market Square
                locations.add(new LatLng(49.21198, 16.61697)); // Brnenské kolo
                locations.add(new LatLng(49.21095, 16.61755)); // Astronomical Clock
                locations.add(new LatLng(49.20995, 16.61646)); // Špilberk Castle
                locations.add(new LatLng(49.20962, 16.61499)); // Úvozna (ENDPOINT)
                break;
            case REAL:
                locations.add(new LatLng(49.195799, 16.613774)); // Mahen Theater (STARTPOINT)
                locations.add(new LatLng(49.193100, 16.612106)); // Church of St. Josef
                locations.add(new LatLng(49.191033, 16.606986)); // Cathedral of St. Peter and Paul
                locations.add(new LatLng(49.192366, 16.608915)); // Cabbage Market Square
                locations.add(new LatLng(49.193149, 16.608712)); // Brnenské kolo
                locations.add(new LatLng(49.194809, 16.608588)); // Astronomical Clock
                locations.add(new LatLng(49.194706, 16.598401)); // Špilberk Castle
                locations.add(new LatLng(49.199024, 16.593342)); // Úvozna (ENDPOINT)
        }
    }
}
