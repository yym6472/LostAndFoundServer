package com.yymstaygold.lostandfound.server.util.match;

import com.yymstaygold.lostandfound.server.entity.Found;
import com.yymstaygold.lostandfound.server.entity.Item;
import com.yymstaygold.lostandfound.server.entity.Lost;
import com.yymstaygold.lostandfound.server.util.distance.DistanceUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by yanyu on 2018/4/16.
 */
public class MatchUtil {

    private static Double SIGMA = null;

    static {
        loadAlgorithmFactors();
    }

    private static void loadAlgorithmFactors() {
        try {
            Properties prop = new Properties();
            FileInputStream settings = new FileInputStream(
                    "./webapps/LostAndFoundServer/WEB-INF/classes/settings.properties");
            prop.load(settings);
            settings.close();
            SIGMA = new Double(prop.getProperty("server.algorithm.sigma"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getTotalScore(Lost lost, Found found) {

        return getLocationMatchScore(lost, found) *
                getItemMatchScore(lost.getItem(), found.getItem());
    }

    private static double getLocationMatchScore(Lost lost, Found found) {
        double score = 0.0;
        int recordSize = lost.getLostPositionInfoTime().size();
        assert lost.getLostPositionInfoPositionX().size() == recordSize;
        assert lost.getLostPositionInfoPositionY().size() == recordSize;
        for (int i = 0; i < recordSize; ++i) {
            if (lost.getLostPositionInfoTime().get(i).compareTo(found.getFoundTime()) < 0) {
                score += getProbability(
                        lost.getLostPositionInfoPositionX().get(i),
                        lost.getLostPositionInfoPositionY().get(i),
                        found.getFoundPositionX(),
                        found.getFoundPositionY());
            }
        }
        return score / recordSize;
    }

    private static double getItemMatchScore(Item item1, Item item2) {
        if (item1.getType() == item2.getType()) {
            return 1.0;
        } else {
            return 0.1;
        }
    }

    private static double getProbability(double x1, double y1, double x2, double y2) {
        double distanceMeter = DistanceUtil.getDistance(x1, y1, x2, y2);
        return (Math.exp((-1.0 * distanceMeter * distanceMeter) / (2.0 * SIGMA * SIGMA))) /
                (2.0 * Math.PI * SIGMA * SIGMA);
    }
}
