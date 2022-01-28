package domain;

import enums.RegionType;
import org.jgrapht.graph.Pseudograph;
import panel.GameScreen;

import java.util.*;

public class GameMap {

    private static GameMap INSTANCE;

    private GameScreen gameScreen;
    private Pseudograph<Town, Road> mapGraph = new Pseudograph<>(Road.class);
    private List<Town> townList = new ArrayList<>();
    private List<Road> roadList = new ArrayList<>();
    private Map<String, Town> townMap = new HashMap<>();

    private GameMap(GameScreen pGameScreen) {
        this.gameScreen = pGameScreen;
        initializeTowns();
        initializeRoads();
    }

    public static GameMap getInstance() {
        return INSTANCE;
    }

    /**
     * Call this once before getting instance
     */
    public static GameMap init(GameScreen gameScreen) {
        if (INSTANCE == null) {
            INSTANCE = new GameMap(gameScreen);
        }
        return INSTANCE;
    }

    public Town getTown(String name) {
        return townMap.get(name);
    }

    public List<Town> getTownList() {
        return townList;
    }

    public List<Road> getRoadList() {
        return roadList;
    }

    public Set<Road> getRoadsFromTown(Town t) {
        return mapGraph.outgoingEdgesOf(t);
    }

    public Set<Road> getRoadsBetween(Town srcTown, Town destTown) {
        return mapGraph.getAllEdges(srcTown, destTown);
    }

    public Town getRoadSource(Road r) {
        return mapGraph.getEdgeSource(r);
    }

    public Town getRoadTarget(Road r) {
        return mapGraph.getEdgeTarget(r);
    }

    public Town getTownByName(String name) {
        return townMap.get(name);
    }

    public void clearAllCounters() {
        for (Road road: roadList) {
            road.clear();
        }
    }

    private void initializeTowns() {
        townList.add(new Town("Elvenhold", 750, 275, 115, 70, gameScreen));
        townList.add(new Town("Wylhien", 241, 30, 74, 37, gameScreen));
        townList.add(new Town("Jaccaranda", 435, 65, 74, 37, gameScreen));
        townList.add(new Town("Lisselen", 59, 105, 74, 37, gameScreen));
        townList.add(new Town("Yttar", 54, 222, 74, 37, gameScreen));
        townList.add(new Town("Grangor", 79, 353, 74, 37, gameScreen));
        townList.add(new Town("Mah'Davikia", 75, 460, 74, 37, gameScreen));
        townList.add(new Town("Jxara", 347, 467, 74, 37, gameScreen));
        townList.add(new Town("Dag'Amura", 374, 336, 74, 37, gameScreen));
        townList.add(new Town("Al'Baran", 372, 226, 74, 37, gameScreen));
        townList.add(new Town("Throtmanni", 606, 135, 74, 37, gameScreen));
        townList.add(new Town("Feodor", 546, 254, 74, 37, gameScreen));
        townList.add(new Town("Virst", 627, 478, 74, 37, gameScreen));
        townList.add(new Town("Strykhaven", 819, 437, 74, 37, gameScreen));
        townList.add(new Town("Beata", 940, 392, 74, 37, gameScreen));
        townList.add(new Town("Tichih", 794, 91, 74, 37, gameScreen));
        townList.add(new Town("Rivinia", 739, 199, 74, 37, gameScreen));
        townList.add(new Town("Kihromah", 218, 303, 74, 37, gameScreen));
        townList.add(new Town("Erg'Eren", 941, 210, 74, 37, gameScreen));
        townList.add(new Town("Lapphalya", 548, 374, 74, 37, gameScreen));
        townList.add(new Town("Parundia", 230, 171, 74, 37, gameScreen));

        for (Town town: townList) {
            townMap.put(town.getName(), town);
            mapGraph.addVertex(town);
        }
    }

    private void initializeRoads() {
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Beata"), createAndSaveRoad(RegionType.PLAIN, 890, 360));
        mapGraph.addEdge(townMap.get("Beata"), townMap.get("Elvenhold"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Strykhaven"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Virst"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Strykhaven"), townMap.get("Virst"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Rivinia"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Erg'Eren"), createAndSaveRoad(RegionType.WOODS, 910, 259));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Lapphalya"), createAndSaveRoad(RegionType.PLAIN, 686, 360));
        mapGraph.addEdge(townMap.get("Beata"), townMap.get("Strykhaven"), createAndSaveRoad(RegionType.PLAIN, 938, 455));
        mapGraph.addEdge(townMap.get("Virst"), townMap.get("Strykhaven"), createAndSaveRoad(RegionType.MOUNTAIN, 770, 513));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Virst"), createAndSaveRoad(RegionType.PLAIN, 580, 430));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Rivinia"), createAndSaveRoad(RegionType.WOODS, 663, 278));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Feodor"), createAndSaveRoad(RegionType.WOODS, 580, 317));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Dag'Amura"), createAndSaveRoad(RegionType.WOODS, 468, 367));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Jxara"), createAndSaveRoad(RegionType.WOODS, 452, 430));
        mapGraph.addEdge(townMap.get("Virst"), townMap.get("Jxara"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Virst"), townMap.get("Jxara"), createAndSaveRoad(RegionType.PLAIN, 499, 506));
        mapGraph.addEdge(townMap.get("Jxara"), townMap.get("Dag'Amura"), createAndSaveRoad(RegionType.WOODS, 368, 406));
        mapGraph.addEdge(townMap.get("Jxara"), townMap.get("Mah'Davikia"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Jxara"), townMap.get("Mah'Davikia"), createAndSaveRoad(RegionType.MOUNTAIN, 237, 474));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Feodor"), createAndSaveRoad(RegionType.DESERT, 466, 294));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 389, 290));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Kihromah"), createAndSaveRoad(RegionType.WOODS, 314, 311));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Mah'Davikia"), createAndSaveRoad(RegionType.MOUNTAIN, 227, 401));
        mapGraph.addEdge(townMap.get("Feodor"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 485, 234));
        mapGraph.addEdge(townMap.get("Feodor"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.DESERT, 573, 199));
        mapGraph.addEdge(townMap.get("Feodor"), townMap.get("Rivinia"), createAndSaveRoad(RegionType.WOODS, 637, 226));
        mapGraph.addEdge(townMap.get("Rivinia"), townMap.get("Tichih"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Rivinia"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.WOODS, 697, 163));
        mapGraph.addEdge(townMap.get("Tichih"), townMap.get("Erg'Eren"), createAndSaveRoad(RegionType.WOODS, 894, 156));
        mapGraph.addEdge(townMap.get("Tichih"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.PLAIN, 730, 124));
        mapGraph.addEdge(townMap.get("Tichih"), townMap.get("Jaccaranda"), createAndSaveRoad(RegionType.MOUNTAIN, 613, 68));
        mapGraph.addEdge(townMap.get("Jaccaranda"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.MOUNTAIN, 538, 117));
        mapGraph.addEdge(townMap.get("Jaccaranda"), townMap.get("Wylhien"), createAndSaveRoad(RegionType.MOUNTAIN, 353, 49));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.PLAIN, 134, 47));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Parundia"), createAndSaveRoad(RegionType.PLAIN, 227, 107));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 349, 122));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.WOODS, 164, 136));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Yttar"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Grangor"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 332, 187));
        mapGraph.addEdge(townMap.get("Throtmanni"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 501, 175));
        mapGraph.addEdge(townMap.get("Yttar"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.WOODS, 70, 164));
        mapGraph.addEdge(townMap.get("Yttar"), townMap.get("Grangor"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Yttar"), townMap.get("Grangor"), createAndSaveRoad(RegionType.MOUNTAIN, 66, 296));
        mapGraph.addEdge(townMap.get("Mah'Davikia"), townMap.get("Grangor"), createAndSaveRoad(RegionType.MOUNTAIN, 63, 427));
        mapGraph.addEdge(townMap.get("Mah'Davikia"), townMap.get("Grangor"), createAndSaveRoad(RegionType.RIVER, 0, 0));

    }

    private Road createAndSaveRoad(RegionType regionType, int x, int y) {
        Road r = new Road(regionType, x, y, gameScreen);
        roadList.add(r);
        return r;
    }

}
