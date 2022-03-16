package domain;

import enums.GameVariant;
import enums.RegionType;
import networking.GameState;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
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

    private GameMap(GameScreen pGameScreen, GameVariant gameVariant) {
        this.gameScreen = pGameScreen;
        initializeTowns(gameVariant);
        initializeRoads();
    }

    public static GameMap getInstance() {
        return INSTANCE;
    }

    /**
     * Call this once before getting instance
     */
    public static GameMap init(GameScreen gameScreen, GameVariant variant) {
        if (INSTANCE == null) {
            INSTANCE = new GameMap(gameScreen, variant);
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

    /**
     * @param srcTown    the source town
     * @param destTown   the destination town
     * @param regionType the region type
     * @return the road uniquely defined by the towns that it connects and the region type
     */
    public Road getRoadBetween(Town srcTown, Town destTown, RegionType regionType) {
        for (Road r : getRoadsBetween(srcTown, destTown)) {
            if (r.getRegionType() == regionType) {
                return r;
            }
        }
        return null;
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

    public int getDistanceBetween(Town t1, Town t2) {
        BellmanFordShortestPath<Town, Road> alg = new BellmanFordShortestPath<>(mapGraph);
        return alg.getPath(t1, t2).getLength();
    }

    public void clearAllCounters() {
        for (Road road : roadList) {
            TransportationCounter counter = road.getTransportationCounter();
            if (counter != null) {
                counter.setOwned(false);
                GameState.instance().getCounterPile().addDrawable(counter);
                road.clear();
            }
        }
    }

    private void initializeTowns(GameVariant gameVariant) {
        townList.add(new Town(gameVariant, "Elvenhold", 810, 310, 115, 70, gameScreen, 0));
        townList.add(new Town(gameVariant, "Wylhien", 250, 40, 74, 37, gameScreen, 3));
        townList.add(new Town(gameVariant, "Jaccaranda", 445, 80, 74, 37, gameScreen, 5));
        townList.add(new Town(gameVariant, "Usselen", 59, 110, 74, 37, gameScreen, 4));
        townList.add(new Town(gameVariant, "Yttar", 54, 245, 74, 37, gameScreen, 4));
        townList.add(new Town(gameVariant, "Grangor", 79, 385, 74, 37, gameScreen, 5));
        townList.add(new Town(gameVariant, "Mah'Davikia", 90, 505, 74, 37, gameScreen, 5));
        townList.add(new Town(gameVariant, "Jxara", 360, 520, 74, 37, gameScreen, 3));
        townList.add(new Town(gameVariant, "Dag'Amura", 390, 370, 74, 37, gameScreen, 4));
        townList.add(new Town(gameVariant, "Al'Baran", 395, 250, 74, 37, gameScreen, 7));
        townList.add(new Town(gameVariant, "Throtmanni", 640, 150, 74, 37, gameScreen, 3));
        townList.add(new Town(gameVariant, "Feodor", 580, 280, 74, 37, gameScreen, 4));
        townList.add(new Town(gameVariant, "Virst", 670, 520, 74, 37, gameScreen, 3));
        townList.add(new Town(gameVariant, "Strykhaven", 875, 485, 74, 37, gameScreen, 4));
        townList.add(new Town(gameVariant, "Beata", 1010, 430, 74, 37, gameScreen, 2));
        townList.add(new Town(gameVariant, "Tichih", 835, 95, 74, 37, gameScreen, 3));
        townList.add(new Town(gameVariant, "Rivinia", 770, 225, 74, 37, gameScreen, 3));
        townList.add(new Town(gameVariant, "Kihromah", 235, 340, 74, 37, gameScreen, 6));
        townList.add(new Town(gameVariant, "Erg'Eren", 1000, 220, 74, 37, gameScreen, 5));
        townList.add(new Town(gameVariant, "Lapphalya", 580, 410, 74, 37, gameScreen, 2));
        townList.add(new Town(gameVariant, "Parundia", 240, 190, 74, 37, gameScreen, 4));

        for (Town town : townList) {
            townMap.put(town.getName(), town);
            mapGraph.addVertex(town);
        }
    }

    private void initializeRoads() {
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Beata"), createAndSaveRoad(RegionType.PLAIN, 960, 390));
        mapGraph.addEdge(townMap.get("Beata"), townMap.get("Elvenhold"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Strykhaven"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Virst"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Strykhaven"), townMap.get("Virst"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Rivinia"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Erg'Eren"), createAndSaveRoad(RegionType.WOODS, 950, 290));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Lapphalya"), createAndSaveRoad(RegionType.PLAIN, 700, 410));
        mapGraph.addEdge(townMap.get("Beata"), townMap.get("Strykhaven"), createAndSaveRoad(RegionType.PLAIN, 990, 500));
        mapGraph.addEdge(townMap.get("Virst"), townMap.get("Strykhaven"), createAndSaveRoad(RegionType.MOUNTAIN, 790, 560));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Virst"), createAndSaveRoad(RegionType.PLAIN, 620, 480));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Rivinia"), createAndSaveRoad(RegionType.WOODS, 710, 300));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Feodor"), createAndSaveRoad(RegionType.WOODS, 630, 340));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Dag'Amura"), createAndSaveRoad(RegionType.WOODS, 500, 400));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Jxara"), createAndSaveRoad(RegionType.WOODS, 490, 480));
        mapGraph.addEdge(townMap.get("Virst"), townMap.get("Jxara"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Virst"), townMap.get("Jxara"), createAndSaveRoad(RegionType.PLAIN, 510, 550));
        mapGraph.addEdge(townMap.get("Jxara"), townMap.get("Dag'Amura"), createAndSaveRoad(RegionType.WOODS, 390, 440));
        mapGraph.addEdge(townMap.get("Jxara"), townMap.get("Mah'Davikia"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Jxara"), townMap.get("Mah'Davikia"), createAndSaveRoad(RegionType.MOUNTAIN, 270, 520));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Feodor"), createAndSaveRoad(RegionType.DESERT, 500, 330));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 420, 330));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Kihromah"), createAndSaveRoad(RegionType.WOODS, 350, 350));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Mah'Davikia"), createAndSaveRoad(RegionType.MOUNTAIN, 260, 440));
        mapGraph.addEdge(townMap.get("Feodor"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 520, 265));
        mapGraph.addEdge(townMap.get("Feodor"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.DESERT, 610, 230));
        mapGraph.addEdge(townMap.get("Feodor"), townMap.get("Rivinia"), createAndSaveRoad(RegionType.WOODS, 670, 270));
        mapGraph.addEdge(townMap.get("Rivinia"), townMap.get("Tichih"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Rivinia"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.WOODS, 740, 190));
        mapGraph.addEdge(townMap.get("Tichih"), townMap.get("Erg'Eren"), createAndSaveRoad(RegionType.WOODS, 920, 170));
        mapGraph.addEdge(townMap.get("Tichih"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.PLAIN, 750, 135));
        mapGraph.addEdge(townMap.get("Tichih"), townMap.get("Jaccaranda"), createAndSaveRoad(RegionType.MOUNTAIN, 630, 68));
        mapGraph.addEdge(townMap.get("Jaccaranda"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.MOUNTAIN, 550, 130));
        mapGraph.addEdge(townMap.get("Jaccaranda"), townMap.get("Wylhien"), createAndSaveRoad(RegionType.MOUNTAIN, 353, 49));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.PLAIN, 134, 47));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Parundia"), createAndSaveRoad(RegionType.PLAIN, 240, 107));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 349, 122));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.WOODS, 164, 150));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Yttar"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Grangor"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 350, 200));
        mapGraph.addEdge(townMap.get("Throtmanni"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 520, 195));
        mapGraph.addEdge(townMap.get("Yttar"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.WOODS, 70, 180));
        mapGraph.addEdge(townMap.get("Yttar"), townMap.get("Grangor"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Yttar"), townMap.get("Grangor"), createAndSaveRoad(RegionType.MOUNTAIN, 66, 320));
        mapGraph.addEdge(townMap.get("Mah'Davikia"), townMap.get("Grangor"), createAndSaveRoad(RegionType.MOUNTAIN, 63, 450));
        mapGraph.addEdge(townMap.get("Mah'Davikia"), townMap.get("Grangor"), createAndSaveRoad(RegionType.RIVER, 0, 0));

    }

    private Road createAndSaveRoad(RegionType regionType, int x, int y) {
        Road r = new Road(regionType, x, y, gameScreen);
        roadList.add(r);
        return r;
    }

}
