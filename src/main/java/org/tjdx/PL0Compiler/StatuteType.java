package org.tjdx.PL0Compiler;

import lombok.Getter;
import lombok.Setter;

/**
 * 用来表示规约类型的类
 */
@Getter
@Setter
public class StatuteType {
    String type;
    int depth;
    public StatuteType(String type,int depth){setType(type);setDepth(depth);}
}
