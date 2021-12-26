package com.tcgr.wx.parser.basic;

import com.tcgr.wx.base.Notam;

import java.util.ArrayList;

/**
 * Basic Notam Parser
 * Created by thomas on 22/02/16.
 */
public abstract class BasicNotamParser extends BasicParser<ArrayList<Notam>> {

    protected ArrayList<Notam> notamArrayList = new ArrayList<>();
    protected Notam notam;

    @Override
    public ArrayList<Notam> getList() {
        return notamArrayList;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        this.content.append(ch, start, length);
    }

    public int getSize() {
        return (notamArrayList == null) ? 0 : notamArrayList.size();
    }
}
