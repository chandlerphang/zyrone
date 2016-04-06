package com.zyrone.xml.merge.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 带名称的InputStream。可以用于日志追踪
 * 
 * @author Jason Phang
 */
public class NamedInputStream extends InputStream {

    private final InputStream is;
    private List<String> names = new ArrayList<String>(20);

    public NamedInputStream(InputStream is, String name) {
        this.is = is;
        names.add(name);
    }

    public NamedInputStream(InputStream is, String name, List<String> previousNames) {
        this.is = is;
        names.addAll(previousNames);
        if (!StringUtils.isEmpty(name)) {
            names.add(name);
        }
    }

    public List<String> getNames() {
        return names;
    }

    public String getName() {
        assert names.size() == 1;
        return names.get(0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        int size = names.size();
        for (int j=0;j<size;j++) {
            sb.append(names.get(j));
            if (j < size - 1) {
                sb.append(" : ");
            }
        }

        return sb.toString();
    }

    @Override
    public int available() throws IOException {
        return (is==null) ? -1 : is.available();
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

    @Override
    public void mark(int i) {
        is.mark(i);
    }

    @Override
    public boolean markSupported() {
        return is.markSupported();
    }

    @Override
    public int read() throws IOException {
        return is.read();
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return is.read(bytes);
    }

    @Override
    public int read(byte[] bytes, int i, int i1) throws IOException {
        return is.read(bytes, i, i1);
    }

    @Override
    public void reset() throws IOException {
        is.reset();
    }

    @Override
    public long skip(long l) throws IOException {
        return is.skip(l);
    }
   
}
