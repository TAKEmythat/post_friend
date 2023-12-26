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
    // 标签的名字，例如：<h1></h1>，这里的标签名为：[h1]
    private String labelName;
    // 标签中的值，例如：<h1>你好</h1>，这里的标签值为：[你好]
    private String value;
    // 标签内部的属性名与属性值，例如：<h1 id="a"></h1>，这里的属性名与属性值就是 [id : a]
    private Map<String, String> Attributes;

    public XMLLabel(String labelName, String value, Map<String, String> attributes) {
        this.labelName = labelName;
        this.value = value;
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
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        return Objects.equals(labelName, xmlLabel.labelName) && Objects.equals(value, xmlLabel.value) && Objects.equals(Attributes, xmlLabel.Attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelName, value, Attributes);
    }

    @Override
    public String toString() {
        return "XMLLabel{" +
                "labelName='" + labelName + '\'' +
                ", value='" + value + '\'' +
                ", Attributes=" + Attributes +
                '}';
    }
}
