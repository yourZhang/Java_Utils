package com.travel.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @program: study_java
 * @description: 生成服务对象 工厂类
 * @author: xiaozhang6666
 * @create: 2020-08-21 16:27
 **/
public class FactoryBeanUtil {

    public static Object getBean(String id) {
        Object result = null;
        try {
            //1、加载配置文件
            InputStream in = FactoryBeanUtil.class.getClassLoader().getResourceAsStream("Beans.xml");
            //2、创建dom4j核心解析器
            SAXReader saxReader = new SAXReader();
            //3、读取io，解析xml文件
            Document document = saxReader.read(in);
            //4、编写xpath语法获取bean标签
            String xpath = "//bean[@id='" + id + "']";
            //5、获取bean标签对象
            Element element = (Element) document.selectSingleNode(xpath);
            //6、获取bean标签的class属性值 类全限定名
            String className = element.attributeValue("class");
            //7、通过反射，获取类字节码文件
            Class<?> clazz = Class.forName(className);
            //8、通过反射创建对象
            result = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
