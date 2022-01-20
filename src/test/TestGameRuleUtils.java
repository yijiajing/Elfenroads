package test;

import domain.CounterType;
import domain.GameMap;
import domain.Town;
import domain.TravelCard;
import org.minueto.MinuetoTool;
import panel.GameScreen;
import utils.CommonUtils;
import utils.GameRuleUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TestGameRuleUtils {
    public static void main(String[] args) {

        JFrame gameScreen = new JFrame("GameScreen");
        gameScreen.setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
        gameScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameScreen.add(GameScreen.getInstance(gameScreen));
        gameScreen.setVisible(false);

        // test initialization
        GameMap gameMap = new GameMap(GameScreen.getInstance(new JFrame()));
        Town elvenhold = gameMap.getTownByName("Elvenhold");
        Town feodor = gameMap.getTownByName("Feodor");
        Town beata = gameMap.getTownByName("Beata");
        TravelCard unicorn = new TravelCard(CounterType.UNICORN, 1, 1);
        TravelCard raft = new TravelCard(CounterType.RAFT, 1, 1);
        TravelCard cloud = new TravelCard(CounterType.MAGICCLOUD, 1, 1);

        // no road from Elvenhold to Feodor, expect false
        assert !GameRuleUtils.validateMove(gameMap, elvenhold, feodor, Arrays.asList(unicorn, raft));
        assert !GameRuleUtils.validateMove(gameMap, elvenhold, feodor, Arrays.asList(unicorn, raft, raft));

        // a river and a plain road from Beata to Elvenhold
        assert GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(raft, raft));
        assert GameRuleUtils.validateMove(gameMap, beata, elvenhold, Arrays.asList(raft));
        assert !GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(unicorn));
        assert !GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(cloud));
        assert GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(cloud, cloud));
        assert !GameRuleUtils.validateMove(gameMap, elvenhold, beata, Arrays.asList(cloud, cloud, cloud));
    }


}
