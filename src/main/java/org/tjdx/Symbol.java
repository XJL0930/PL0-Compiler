package org.tjdx;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于存储符号
 */
@Setter
@Getter
public class Symbol {
    private int layer;
    private String symbol;
    private int trueList;
    private int flaseList;
    private int quad;
    private int nextList=-1;
    public Symbol(int layer,String symbol){setLayer(layer);setSymbol(symbol);}
}
