package com.tcgr.wx.parser.basic;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Basic Parser
 *
 * Created by thomas on 15/02/16.
 */
public abstract class BasicParser<T> extends DefaultHandler {

    protected StringBuilder content;

    @Override
    public abstract void startElement(String uri, String localName, String qName, Attributes attributes);

    @Override
    public abstract void endElement(String uri, String localName, String qName) throws SAXException;

    @Override
    public void characters(char[] ch, int start, int length) {
        if (length == 0) return;
        int end = (start + length) - 1;
        while (ch[start] <= '\u0020') {
            if (start == end) return;
            start++;
            length--;
        }
        while (ch[end] <= '\u0020') {
            if (end == start) return;
            length--;
            end--;
        }
        content.append(ch, start, length);
    }

    public abstract T getList();
    public abstract int getSize();

    protected String getContent() {
        return content.toString();
    }
}