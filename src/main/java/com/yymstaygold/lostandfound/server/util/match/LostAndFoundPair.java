package com.yymstaygold.lostandfound.server.util.match;

import com.yymstaygold.lostandfound.server.entity.Found;
import com.yymstaygold.lostandfound.server.entity.Lost;

/**
 * Created by yanyu on 2018/4/18.
 */
public class LostAndFoundPair implements Comparable<LostAndFoundPair>{
    private Lost lost;
    private Found found;

    public LostAndFoundPair(Lost lost, Found found) {
        this.lost = lost;
        this.found = found;
    }

    @Override
    public int compareTo(LostAndFoundPair o) {
        return new Double(MatchUtil.getTotalScore(o.lost, o.found))
                .compareTo(new Double(MatchUtil.getTotalScore(lost, found)));
    }

    public int getLostId() {
        return lost.getLostId();
    }

    public int getFoundId() {
        return found.getFoundId();
    }
}
