package com.zyrone.xml.merge;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.zyrone.util.Assert;
import com.zyrone.xml.merge.support.NamedInputStream;

/**
 * Xml文件合并器。多项目环境下，各个项目的特定框架配置文件需要先做合并，整合成一个后才能传给
 * 框架用于容器的启动配置。这里的“合并”只是用来描述“多文件输入单文件输出”这一行为的，通过处理
 * 器的配置，“合并”可以具体为节点的添加，删除，修改等任意具体行为。
 * 
 * @author Jason Phang
 */
public class XmlMerger {
	
	private static final Logger LOG = LoggerFactory.getLogger(XmlMerger.class);
	private XmlMergeHandlerChain handlerChain;
	private DocumentBuilder builder;
	
	public XmlMerger(XmlMergeHandlerChain handlerChain) {
		Assert.notNull(handlerChain);
		this.handlerChain = handlerChain;
		init();
	}
    
	private void init() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOG.error("无法创建文档解析器");
            throw new RuntimeException(e);
        }
	}
	
    public NamedInputStream merge(NamedInputStream[] sources) throws XmlMergeException {
    	if (sources == null) return null;
        if (sources.length == 1) return sources[0];
        
        int j = 0;
        try {
	        Document[] pair = new Document[2];
	        for (pair[0] = builder.parse(sources[0]), j=1; j<sources.length; j++) {
			    pair[1] = builder.parse(sources[j]);
			    handlerChain.merge(pair[0], pair[1]);
			}
	        
	        TransformerFactory tFactory = TransformerFactory.newInstance();
	        Transformer xmlTransformer = tFactory.newTransformer();
	        xmlTransformer.setOutputProperty(OutputKeys.VERSION, "1.0");
	        xmlTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        xmlTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
	
	        DOMSource source = new DOMSource(pair[0]);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos, "UTF-8"));
	        StreamResult result = new StreamResult(writer);
	        xmlTransformer.transform(source, result);
	        
	        return new NamedInputStream(new ByteArrayInputStream(baos.toByteArray()), null);
        } catch (Exception e) {
        	if(LOG.isErrorEnabled()) {
				if(j > 1) {
					LOG.error("XML解析错误: {}", sources[j].getName(), e);
				} else {
					LOG.error("XML解析错误: {} / {}", sources[0].getName(), sources[j].getName(), e);
				}
			}
        	throw new XmlMergeException(e);
        }
    }
    
    public String serialize(InputStream in) {
        InputStreamReader reader = null;
        int temp;
        StringBuilder item = new StringBuilder();
        boolean eof = false;
        try {
            reader = new InputStreamReader(in, "utf-8");
            while (!eof) {
                temp = reader.read();
                if (temp == -1) {
                    eof = true;
                } else {
                    item.append((char) temp);
                }
            }
        } catch (IOException e) {
            LOG.error("Unable to merge source and patch locations", e);
        } finally {
            if (reader != null) {
                try{ reader.close(); } catch (Throwable e) {
                    LOG.error("Unable to merge source and patch locations", e);
                }
            }
        }

        return item.toString();
    }

    protected byte[] buildArrayFromStream(InputStream source) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean eof = false;
        try{
            while (!eof) {
                int temp = source.read();
                if (temp == -1) {
                    eof = true;
                } else {
                    baos.write(temp);
                }
            }
        } finally {
            try{ source.close(); } catch (Throwable e) {
                LOG.error("Unable to merge source and patch locations", e);
            }
        }

        return baos.toByteArray();
    }
}
