package com.zyrone.framework.core.spring;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.zyrone.xml.merge.XmlMergeException;
import com.zyrone.xml.merge.support.NamedInputStream;

/**
 * 处理spring配置文件中的import元素
 * 
 * @author Jason Phang
 */
class SpringImportProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SpringImportProcessor.class);
    private static final String IMPORT_PATH = "/beans/import";

    protected ResourceLoader loader;
    protected SAXReader reader;

    public SpringImportProcessor(ResourceLoader loader) {
        this.loader = loader;
        this.reader = new SAXReader();
    }

    public NamedInputStream[] extract(NamedInputStream[] sources) throws XmlMergeException {
        if (sources == null) {
            return null;
        }
        try {
            DynamicResourceIterator resourceList = new DynamicResourceIterator();
            resourceList.addAll(Arrays.asList(sources));
            while(resourceList.hasNext()) {
                NamedInputStream myStream = resourceList.nextResource();
                Document doc = reader.read(myStream);
                List<?> nodeList = doc.selectNodes(IMPORT_PATH);
                for (Iterator<?> iter = nodeList.iterator(); iter.hasNext(); ) {
                    Element element = (Element) iter.next();
                    Resource resource = loader.getResource(element.attributeValue("resource"));
                    NamedInputStream ris = new NamedInputStream(resource.getInputStream(), resource.getURL().toString());
                    resourceList.addEmbeddedResource(ris);
                    
                    // 从原文档中删除该import节点
                    element.getParent().remove(element);
                }
                // 因为doc已经被修改（删除了所有import），所以重新生成一个stream
                if (nodeList.size() > 0) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    XMLWriter writer = new XMLWriter(baos);
                    writer.write(doc);
                    byte[] itemArray = baos.toByteArray();

                    resourceList.set(resourceList.getPosition() - 1, new NamedInputStream(new ByteArrayInputStream(itemArray), null, myStream.getNames()));
                } else {
                    myStream.reset();
                }
            }

            return resourceList.toArray(new NamedInputStream[resourceList.size()]);
        } catch (Exception e) {
            throw new XmlMergeException(e);
        }
    }
    
    private static class DynamicResourceIterator extends ArrayList<NamedInputStream> {

    	private static final long serialVersionUID = 1L;

        private int position = 0;
        private int embeddedInsertPosition = 0;

        public NamedInputStream nextResource() {
            NamedInputStream ris = get(position);
            position++;
            embeddedInsertPosition = position;
            return ris;
        }

        public int getPosition() {
            return position;
        }

        public void addEmbeddedResource(NamedInputStream ris) {
            if (embeddedInsertPosition == size()) {
                add(ris);
            } else {
                add(embeddedInsertPosition, ris);
            }
            embeddedInsertPosition++;
        }

        public boolean hasNext() {
            return position < size();
        }

        @Override
        public boolean add(NamedInputStream resourceInputStream) {
            byte[] sourceArray;
            try {
                sourceArray = buildArrayFromStream(resourceInputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            NamedInputStream ris = new NamedInputStream(new ByteArrayInputStream(sourceArray), null, resourceInputStream.getNames());
            return super.add(ris);
        }

        @Override
        public boolean addAll(Collection<? extends NamedInputStream> c) {
            for (NamedInputStream ris : c) {
                if (!add(ris)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void add(int index, NamedInputStream resourceInputStream) {
            byte[] sourceArray;
            try {
                sourceArray = buildArrayFromStream(resourceInputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            NamedInputStream ris = new NamedInputStream(new ByteArrayInputStream(sourceArray), null, resourceInputStream.getNames());
            super.add(index, ris);
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
                    LOG.error("无法将输入流转换到字节数组", e);
                }
            }

            return baos.toByteArray();
        }
    }
    
}


