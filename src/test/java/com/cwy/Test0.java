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
    @Test
    public void test0_0() throws URISyntaxException, ParserConfigurationException, IOException, SAXException {
        XMLObject xmlObject = XMLAnalysis.getXmlObject("com/cwy/post_friend/dao/UserDao.xml");
        System.out.println("xmlObject = " + xmlObject);
    }
}
