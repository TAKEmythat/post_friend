package com.cwy.post_friend.frame.bean;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Classname XMLObject
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 17:09
 * @Since 1.0.0
 */

public class XMLObject {
    private String xmlName;
    private List<XMLLabel> xmlLabels;

    public XMLObject(String xmlName, List<XMLLabel> xmlLabels) {
        this.xmlName = xmlName;
        this.xmlLabels = xmlLabels;
    }

    public XMLObject() {
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public List<XMLLabel> getXmlLabels() {
        return xmlLabels;
    }

    public void setXmlLabels(List<XMLLabel> xmlLabels) {
        this.xmlLabels = xmlLabels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XMLObject xmlObject = (XMLObject) o;
        return Objects.equals(xmlName, xmlObject.xmlName) && Objects.equals(xmlLabels, xmlObject.xmlLabels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xmlName, xmlLabels);
    }

    @Override
    public String toString() {
        return "XMLObject{" +
                "xmlName='" + xmlName + '\'' +
                ", xmlLabels=" + xmlLabels +
                '}';
    }
}
