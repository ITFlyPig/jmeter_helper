package com.wyl.xml;

import com.sun.istack.internal.NotNull;
import com.wyl.util.Preconditions;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2022/4/19
 * 描述    : xml文件解析和修改
 */
public class XmlFile {
    private String path;
    private Document doc;

    public XmlFile(String path) {
        Preconditions.checkArgument(new File(path).exists(), "xml文件不存在");

        this.path = path;
    }

    /**
     * 读取xml文件
     * @return
     */
    public boolean read() {
        SAXReader saxReader = new SAXReader();
        try {
            doc = saxReader.read(new File(path));
            return true;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改特定节点的值
     * @param xpath
     * @param value
     */
    public void set(String xpath, String value) {
        Preconditions.checkArgument(doc != null, "必须先正确读取xml文件");
        if (xpath == null) return;
        if (value == null) return;
        Node node = doc.getRootElement().selectSingleNode(xpath);
        if (node == null) return;
        node.setText(value);
    }

    /**
     * 将xml文件写入到指定路劲
     * @param path
     * @return
     */
    public boolean save(@NotNull String path) {
        try {
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(path));
            xmlWriter.write(doc);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
