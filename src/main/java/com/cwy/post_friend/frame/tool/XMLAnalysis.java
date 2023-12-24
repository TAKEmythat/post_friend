package com.cwy.post_friend.frame.tool;

import com.cwy.post_friend.frame.bean.XMLLabel;
import com.cwy.post_friend.frame.bean.XMLObject;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析 XML 文件
 *
 * @Classname XMLAnalysis
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 17:14
 * @Since 1.0.0
 */

public class XMLAnalysis {
    private XMLAnalysis() {
    }

    /**
     * 扫描 XML 文件，返回 XML 文件对象
     * @param fileName 对应的 XML 文件名
     * @return 返回的 XML 文件对象
     * @throws URISyntaxException 资源找不到错误
     * @throws ParserConfigurationException ...
     * @throws IOException IO 错误
     * @throws SAXException XML 文件转换失败
     */
    public static XMLObject getXmlObject(String fileName) throws URISyntaxException,
            ParserConfigurationException, IOException, SAXException {
        XMLObject xmlObject = new XMLObject();
        List<XMLLabel> xmlLabels = new ArrayList<>();
        URL resource = Thread.currentThread().getContextClassLoader().getResource(fileName);
        assert resource != null;
        URI uri = resource.toURI();
        File file = new File(uri);
        xmlObject.setXmlName(file.getName());
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document parse = documentBuilder.parse(file);
        Element documentElement = parse.getDocumentElement();
        NodeList childNodes = documentElement.getChildNodes();
        int length = childNodes.getLength();
        for (int i = 0; i < length; i++) {
            Node item = childNodes.item(i);
            if (item instanceof Element) {
                Element element = (Element) item;
                String tagName = element.getTagName();
                String nodeValue = element.getTextContent();
                NamedNodeMap attributes0 = element.getAttributes();
                int length1 = attributes0.getLength();
                Map<String, String> attributeMap = new HashMap<>();
                for (int n = 0; n < length1; n++) {
                    Node item1 = attributes0.item(n);
                    String nodeName = item1.getNodeName();
                    String nodeValue1 = item1.getNodeValue();
                    attributeMap.put(nodeName, nodeValue1);
                }
                xmlLabels.add(new XMLLabel(tagName, nodeValue, attributeMap));
            }
        }
        xmlObject.setXmlLabels(xmlLabels);
        return xmlObject;
    }

    public static XMLObject getXmlObject(File file) {
        return null;
    }
}
