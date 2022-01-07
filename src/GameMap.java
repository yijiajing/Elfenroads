import org.jgrapht.graph.Pseudograph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {

    private GameScreen gameScreen;
    private Pseudograph<Town, Road> mapGraph = new Pseudograph<>(Road.class);
    private List<Town> townList = new ArrayList<>();
    private List<Road> roadList = new ArrayList<>();
    private Map<String, Town> townMap = new HashMap<>();
    private int width;
    private int height;

    public GameMap(GameScreen pGameScreen) {
        this.gameScreen = pGameScreen;
        this.width = pGameScreen.getWidth();
        this.height = pGameScreen.getHeight();
        initializeTowns();
        initializeRoads();
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

    public Town getRoadSource(Road r) {
        return mapGraph.getEdgeSource(r);
    }

    public Town getRoadTarget(Road r) {
        return mapGraph.getEdgeTarget(r);
    }

    private void initializeTowns() {
        townList.add(new Town("Elvenhold", 750, 275, 115, 70, gameScreen));
        townList.add(new Town("Wylhien", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Jaccaranda", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Lisselen", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Yttar", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Grangor", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Mah'Davikia", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Jxara", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Dag'Amura", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Al'Baran", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Throtmanni", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Feodor", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Virst", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Strykhaven", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Beata", 940, 392, 74, 37, gameScreen));
        townList.add(new Town("Tichih", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Rivinia", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Kihromah", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Erg'Eren", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Lapphalya", 0, 0, 74, 37, gameScreen));
        townList.add(new Town("Parundia", 0, 0, 74, 37, gameScreen));

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
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Erg'Eren"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Elvenhold"), townMap.get("Lapphalya"), createAndSaveRoad(RegionType.PLAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Beata"), townMap.get("Strykhaven"), createAndSaveRoad(RegionType.PLAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Virst"), townMap.get("Strykhaven"), createAndSaveRoad(RegionType.MOUNTAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Virst"), createAndSaveRoad(RegionType.PLAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Rivinia"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Feodor"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Dag'Amura"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Lapphalya"), townMap.get("Jxara"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Virst"), townMap.get("Jxara"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Virst"), townMap.get("Jxara"), createAndSaveRoad(RegionType.PLAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Jxara"), townMap.get("Dag'Amura"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Jxara"), townMap.get("Mah'Davikia"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Jxara"), townMap.get("Mah'Davikia"), createAndSaveRoad(RegionType.MOUNTAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Feodor"), createAndSaveRoad(RegionType.DESERT, 0, 0));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 0, 0));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Kihromah"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Dag'Amura"), townMap.get("Mah'Davikia"), createAndSaveRoad(RegionType.MOUNTAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Feodor"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 0, 0));
        mapGraph.addEdge(townMap.get("Feodor"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.DESERT, 0, 0));
        mapGraph.addEdge(townMap.get("Feodor"), townMap.get("Rivinia"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Rivinia"), townMap.get("Tichih"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Rivinia"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Tichih"), townMap.get("Erg'Eren"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Tichih"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.PLAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Tichih"), townMap.get("Jaccaranda"), createAndSaveRoad(RegionType.MOUNTAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Jaccaranda"), townMap.get("Throtmanni"), createAndSaveRoad(RegionType.MOUNTAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Jaccaranda"), townMap.get("Wylhien"), createAndSaveRoad(RegionType.MOUNTAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.PLAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Parundia"), createAndSaveRoad(RegionType.PLAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 0, 0));
        mapGraph.addEdge(townMap.get("Wylhien"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.RIVER, 0, 0));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Yttar"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Grangor"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Parundia"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 0, 0));
        mapGraph.addEdge(townMap.get("Throtmanni"), townMap.get("Al'Baran"), createAndSaveRoad(RegionType.DESERT, 0, 0));
        mapGraph.addEdge(townMap.get("Yttar"), townMap.get("Lisselen"), createAndSaveRoad(RegionType.WOODS, 0, 0));
        mapGraph.addEdge(townMap.get("Yttar"), townMap.get("Grangor"), createAndSaveRoad(RegionType.LAKE, 0, 0));
        mapGraph.addEdge(townMap.get("Yttar"), townMap.get("Grangor"), createAndSaveRoad(RegionType.MOUNTAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Mah'Davikia"), townMap.get("Grangor"), createAndSaveRoad(RegionType.MOUNTAIN, 0, 0));
        mapGraph.addEdge(townMap.get("Mah'Davikia"), townMap.get("Grangor"), createAndSaveRoad(RegionType.RIVER, 0, 0));

    }

    private Road createAndSaveRoad(RegionType regionType, int x, int y) {
        Road r = new Road(regionType, x, y, gameScreen);
        roadList.add(r);
        return r;
    }

}
