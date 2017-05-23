package com.company.blackjack.utils;

import org.w3c.dom.Element;

public class XmlUtils {

    public static String getTagValue(String tag, Element element) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    public static Integer getIntTagValue(String tag, Element element) {
        return Integer.parseInt(getTagValue(tag, element));
    }

    public static Double getDoubleTagValue(String tag, Element element) {
        return Double.parseDouble(getTagValue(tag, element));
    }
}
