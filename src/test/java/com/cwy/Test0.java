package com.cwy;

import com.cwy.post_friend.frame.bean.XMLObject;
import com.cwy.post_friend.frame.tool.XMLAnalysis;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @Classname Test0
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 17:49
 * @Since 1.0.0
 */
public class Test0 {
    /**
     * 检查那个解析 XML 工具类的可用性
     * @throws URISyntaxException ...
     * @throws ParserConfigurationException ...
     * @throws IOException ...
     * @throws SAXException ...
     */
    @Test
    public void test0_0() throws URISyntaxException, ParserConfigurationException, IOException, SAXException {
        XMLObject xmlObject = XMLAnalysis.getXmlObject("com/cwy/post_friend/dao/impl/UserDaoImpl.xml");
        System.out.println("xmlObject = " + xmlObject);
    }
}
