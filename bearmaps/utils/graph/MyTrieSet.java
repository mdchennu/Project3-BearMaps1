package bearmaps.utils.graph;

import bearmaps.utils.TrieSet61BL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyTrieSet implements TrieSet61BL {

    private Node root;

    public MyTrieSet() {
        root = new Node(false);
    }

    /**
     * Clears all items out of Trie
     */
    @Override
    public void clear() {
        root.children = new HashMap<>();

    }

    /**
     * Returns true if the Trie contains KEY, false otherwise
     */
    @Override
    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            return false;
        }
        int n = key.length();
        Node curr = root;
        for (int i = 0; i < n; i++) {
            char c = key.charAt(i);
            if (!curr.children.containsKey(c)) {
                return false;
            }
            if (i == n - 1) {
                if (!curr.children.get(c).flagged) {
                    return false;
                }
            }
            curr = curr.children.get(c);
        }

        return true;
    }

    /**
     * Inserts string KEY into Trie
     */
    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        int n = key.length();
        for (int i = 0; i < n; i++) {
            char c = key.charAt(i);
            if (!curr.children.containsKey(c)) {
                curr.children.put(c, new Node(c, false));
            }
            curr = curr.children.get(c);
        }
        curr.flagged = true;
    }

    /**
     * Returns a list of all words that start with PREFIX
     */
    @Override
    public List<String> keysWithPrefix(String prefix) {
        //go through every child node for last node in prefix; if they are flagged add to list
        List<String> returned = new ArrayList<>();

        //finding the prefix last node
        Node curr = root;
        int n = prefix.length();
        if(!prefix.equals("")) {
            for (int i = 0; i < n; i++) {
                char c = prefix.charAt(i);
                if (curr.children.containsKey(c)) {
                    curr = curr.children.get(c);
                }
            }

        }
        for (Character ch : curr.children.keySet()) {
            String s = Character.toString(ch);
            String temp = prefix + s;
            Node m = curr.children.get(ch);
            keysHelper(temp, returned, m);
        }

        //iterate through prefix children and add to list
        return returned;
    }

    public void keysHelper(String prefix, List<String> returned, Node n) {
        if (n == null) {
            return;
        }
        if (n.flagged) {
            returned.add(prefix);
        }

        for (Character ch : n.children.keySet()) {
            String s = Character.toString(ch);
            String temp = prefix + s;
            n = n.children.get(ch);
            keysHelper(temp, returned, n);
        }


    }

    /**
     * Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 18. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }

    private static class Node {

        private char item;
        private HashMap<Character, Node> children;
        private boolean flagged;

        private Node() {
            children = new HashMap<>();
        }

        private Node(boolean flagged) {

            children = new HashMap<>();
            this.flagged = flagged;
        }

        private Node(char item, boolean flagged) {
            this.item = item;
            this.flagged = flagged;
            children = new HashMap<>();
        }


    }

    public static void main(String[] args) {
        MyTrieSet t = new MyTrieSet();
        t.add("hello");
        t.add("hello");

    }


}
