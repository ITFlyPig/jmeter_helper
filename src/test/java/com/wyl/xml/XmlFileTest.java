package com.wyl.xml;

import junit.framework.TestCase;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2022/4/20
 * 描述    :
 */
public class XmlFileTest extends TestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();

    }

    public void testSet() {
        String jmxPath = "/Users/yuelinwang/Downloads/testplan/plan.jmx";
        XmlFile xmlFile = new XmlFile(jmxPath);
        assertTrue(xmlFile.read());
        xmlFile.set("//ThreadGroup/stringProp[@name='ThreadGroup.num_threads']", "111");
        boolean success = xmlFile.save("/Users/yuelinwang/Downloads/testplan/plan2222.jmx");
        assertTrue(success);
    }
}