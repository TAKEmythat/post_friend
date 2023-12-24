package com.cwy.post_friend.frame.bean;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * XML 的标签对象
 *
 * @Classname XMLLabel
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 17:10
 * @Since 1.0.0
 */

public class XMLLabel {
    private String labelName;
    private String Value;
    private Map<String, String> Attributes;

    public XMLLabel(String labelName, String value, Map<String, String> attributes) {
        this.labelName = labelName;
        Value = value;
        Attributes = attributes;
    }

    public XMLLabel() {
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public Map<String, String> getAttributes() {
        return Attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        Attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XMLLabel xmlLabel = (XMLLabel) o;
        return Objects.equals(labelName, xmlLabel.labelName) && Objects.equals(Value, xmlLabel.Value) && Objects.equals(Attributes, xmlLabel.Attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelName, Value, Attributes);
    }

    @Override
    public String toString() {
        return "XMLLabel{" +
                "labelName='" + labelName + '\'' +
                ", Value='" + Value + '\'' +
                ", Attributes=" + Attributes +
                '}';
    }
}
