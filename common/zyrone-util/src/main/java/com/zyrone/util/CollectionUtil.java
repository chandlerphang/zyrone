package com.zyrone.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.zyrone.util.collection.ArrayHashMap;
import com.zyrone.util.collection.ArrayHashSet;

import static com.zyrone.util.BasicConstant.*;

/**
 * 方便创建容器对象的工具。
 *
 * @author Michael Zhou
 */
public final class CollectionUtil {
    /** 创建一个<code>ArrayList</code>。 */
    public static <T> ArrayList<T> createArrayList() {
        return new ArrayList<T>();
    }

    /** 创建一个<code>ArrayList</code>。 */
    public static <T> ArrayList<T> createArrayList(int initialCapacity) {
        return new ArrayList<T>(initialCapacity);
    }

    /** 创建一个<code>ArrayList</code>。 */
    public static <T> ArrayList<T> createArrayList(Iterable<? extends T> c) {
        ArrayList<T> list;

        if (c instanceof Collection<?>) {
            list = new ArrayList<T>((Collection<? extends T>) c);
        } else {
            list = new ArrayList<T>();

            iterableToCollection(c, list);

            list.trimToSize();
        }

        return list;
    }

    /** 创建一个<code>ArrayList</code>。 */
    @SafeVarargs
	public static <T, V extends T> ArrayList<T> createArrayList(V... args) {
        if (args == null || args.length == 0) {
            return new ArrayList<T>();
        } else {
            ArrayList<T> list = new ArrayList<T>(args.length);

            for (V v : args) {
                list.add(v);
            }

            return list;
        }
    }

    /** 创建一个<code>LinkedList</code>。 */
    public static <T> LinkedList<T> createLinkedList() {
        return new LinkedList<T>();
    }

    /** 创建一个<code>LinkedList</code>。 */
    public static <T> LinkedList<T> createLinkedList(Iterable<? extends T> c) {
        LinkedList<T> list = new LinkedList<T>();

        iterableToCollection(c, list);

        return list;
    }

    /** 创建一个<code>LinkedList</code>。 */
    @SafeVarargs
	public static <T, V extends T> LinkedList<T> createLinkedList(V... args) {
        LinkedList<T> list = new LinkedList<T>();

        if (args != null) {
            for (V v : args) {
                list.add(v);
            }
        }

        return list;
    }

    /**
     * 创建一个<code>List</code>。
     * <p>
     * 和{@code createArrayList(args)}不同，本方法会返回一个不可变长度的列表，且性能高于
     * {@code createArrayList(args)}。
     * </p>
     */
    @SafeVarargs
	public static <T> List<T> asList(T... args) {
        if (args == null || args.length == 0) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(args);
        }
    }

    /** 创建一个<code>HashMap</code>。 */
    public static <K, V> HashMap<K, V> createHashMap() {
        return new HashMap<K, V>();
    }

    /** 创建一个<code>HashMap</code>。 */
    public static <K, V> HashMap<K, V> createHashMap(int initialCapacity) {
        return new HashMap<K, V>(initialCapacity);
    }

    /** 创建一个<code>ArrayHashMap</code>。 */
    public static <K, V> ArrayHashMap<K, V> createArrayHashMap() {
        return new ArrayHashMap<K, V>();
    }

    /** 创建一个<code>ArrayHashMap</code>。 */
    public static <K, V> ArrayHashMap<K, V> createArrayHashMap(int initialCapacity) {
        return new ArrayHashMap<K, V>(initialCapacity);
    }

    /** 创建一个<code>LinkedHashMap</code>。 */
    public static <K, V> LinkedHashMap<K, V> createLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    /** 创建一个<code>LinkedHashMap</code>。 */
    public static <K, V> LinkedHashMap<K, V> createLinkedHashMap(int initialCapacity) {
        return new LinkedHashMap<K, V>(initialCapacity);
    }

    /** 创建一个<code>TreeMap</code>。 */
    public static <K, V> TreeMap<K, V> createTreeMap() {
        return new TreeMap<K, V>();
    }

    /** 创建一个<code>TreeMap</code>。 */
    public static <K, V> TreeMap<K, V> createTreeMap(Comparator<? super K> comparator) {
        return new TreeMap<K, V>(comparator);
    }

    /** 创建一个<code>ConcurrentHashMap</code>。 */
    public static <K, V> ConcurrentHashMap<K, V> createConcurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }

    /** 创建一个<code>HashSet</code>。 */
    public static <T> HashSet<T> createHashSet() {
        return new HashSet<T>();
    }

    /** 创建一个<code>HashSet</code>。 */
    @SafeVarargs
	public static <T, V extends T> HashSet<T> createHashSet(V... args) {
        if (args == null || args.length == 0) {
            return new HashSet<T>();
        } else {
            HashSet<T> set = new HashSet<T>(args.length);

            for (V v : args) {
                set.add(v);
            }

            return set;
        }
    }

    /** 创建一个<code>HashSet</code>。 */
    public static <T> HashSet<T> createHashSet(Iterable<? extends T> c) {
        HashSet<T> set;

        if (c instanceof Collection<?>) {
            set = new HashSet<T>((Collection<? extends T>) c);
        } else {
            set = new HashSet<T>();
            iterableToCollection(c, set);
        }

        return set;
    }

    /** 创建一个<code>ArrayHashSet</code>。 */
    public static <T> ArrayHashSet<T> createArrayHashSet() {
        return new ArrayHashSet<T>();
    }

    /** 创建一个<code>ArrayHashSet</code>。 */
    @SafeVarargs
	public static <T, V extends T> ArrayHashSet<T> createArrayHashSet(V... args) {
        if (args == null || args.length == 0) {
            return new ArrayHashSet<T>();
        } else {
            ArrayHashSet<T> set = new ArrayHashSet<T>(args.length);

            for (V v : args) {
                set.add(v);
            }

            return set;
        }
    }

    /** 创建一个<code>ArrayHashSet</code>。 */
    public static <T> ArrayHashSet<T> createArrayHashSet(Iterable<? extends T> c) {
        ArrayHashSet<T> set;

        if (c instanceof Collection<?>) {
            set = new ArrayHashSet<T>((Collection<? extends T>) c);
        } else {
            set = new ArrayHashSet<T>();
            iterableToCollection(c, set);
        }

        return set;
    }

    /** 创建一个<code>LinkedHashSet</code>。 */
    public static <T> LinkedHashSet<T> createLinkedHashSet() {
        return new LinkedHashSet<T>();
    }

    /** 创建一个<code>LinkedHashSet</code>。 */
    @SafeVarargs
	public static <T, V extends T> LinkedHashSet<T> createLinkedHashSet(V... args) {
        if (args == null || args.length == 0) {
            return new LinkedHashSet<T>();
        } else {
            LinkedHashSet<T> set = new LinkedHashSet<T>(args.length);

            for (V v : args) {
                set.add(v);
            }

            return set;
        }
    }

    /** 创建一个<code>LinkedHashSet</code>。 */
    public static <T> LinkedHashSet<T> createLinkedHashSet(Iterable<? extends T> c) {
        LinkedHashSet<T> set;

        if (c instanceof Collection<?>) {
            set = new LinkedHashSet<T>((Collection<? extends T>) c);
        } else {
            set = new LinkedHashSet<T>();
            iterableToCollection(c, set);
        }

        return set;
    }

    /** 创建一个<code>TreeSet</code>。 */
    public static <T> TreeSet<T> createTreeSet() {
        return new TreeSet<T>();
    }

    /** 创建一个<code>TreeSet</code>。 */
    @SuppressWarnings("unchecked")
    public static <T, V extends T> TreeSet<T> createTreeSet(V... args) {
        return (TreeSet<T>) createTreeSet(null, args);
    }

    /** 创建一个<code>TreeSet</code>。 */
    public static <T> TreeSet<T> createTreeSet(Iterable<? extends T> c) {
        return createTreeSet(null, c);
    }

    /** 创建一个<code>TreeSet</code>。 */
    public static <T> TreeSet<T> createTreeSet(Comparator<? super T> comparator) {
        return new TreeSet<T>(comparator);
    }

    /** 创建一个<code>TreeSet</code>。 */
    @SafeVarargs
	public static <T, V extends T> TreeSet<T> createTreeSet(Comparator<? super T> comparator, V... args) {
        TreeSet<T> set = new TreeSet<T>(comparator);

        if (args != null) {
            for (V v : args) {
                set.add(v);
            }
        }

        return set;
    }

    /** 创建一个<code>TreeSet</code>。 */
    public static <T> TreeSet<T> createTreeSet(Comparator<? super T> comparator, Iterable<? extends T> c) {
        TreeSet<T> set = new TreeSet<T>(comparator);

        iterableToCollection(c, set);

        return set;
    }

    /** 将列表中的对象连接成字符串。 */
    public static String join(Iterable<?> objs, String sep) {
        StringBuilder buf = new StringBuilder();

        join(buf, objs, sep);

        return buf.toString();
    }

    /** 将列表中的对象连接起来。 */
    public static void join(StringBuilder buf, Iterable<?> objs, String sep) {
        try {
            join((Appendable) buf, objs, sep);
        } catch (IOException e) {
            Assert.unexpectedException(e);
        }
    }

    /** 将列表中的对象连接起来。 */
    public static void join(Appendable buf, Iterable<?> objs, String sep) throws IOException {
        if (objs == null) {
            return;
        }

        if (sep == null) {
            sep = EMPTY_STRING;
        }

        for (Iterator<?> i = objs.iterator(); i.hasNext(); ) {
            buf.append(String.valueOf(i.next()));

            if (i.hasNext()) {
                buf.append(sep);
            }
        }
    }

    private static <T> void iterableToCollection(Iterable<? extends T> c, Collection<T> list) {
        for (T element : c) {
            list.add(element);
        }
    }
}
