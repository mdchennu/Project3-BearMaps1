package bearmaps;

import bearmaps.utils.graph.MyTrieSet;
import bearmaps.utils.graph.streetmap.Node;
import bearmaps.utils.graph.streetmap.StreetMapGraph;
import bearmaps.utils.ps.KDTree;
import bearmaps.utils.ps.Point;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private List<Node> nodes;
    private List<Point> points = new ArrayList<>();
    private HashMap<Point, Node> pToN = new HashMap<>();
    private KDTree kd;
    private MyTrieSet trie = new MyTrieSet();
    private HashMap<String, List<Node>> cleanToDirty = new HashMap<>();

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        for (Node n : nodes) {
            String cleaned = cleanString(n.name());
            trie.add(cleaned);
            if (!cleanToDirty.containsKey(cleaned)) {
                List<Node> lst = new ArrayList<>();
                lst.add(n);
                cleanToDirty.put(cleaned, lst);
            } else {
                List<Node> curr = cleanToDirty.get(cleanString(n.name()));
                curr.add(n);
            }
            if (!neighbors(n.id()).isEmpty()) {
                Point p = new Point(n.lon(), n.lat());
                pToN.put(p, n);
                points.add(p);
            }
        }
        kd = new KDTree(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point closest = kd.nearest(lon, lat);
        Node vertex = pToN.get(closest);
        return vertex.id();
    }


    /**
     * For Project Part III (extra credit)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     *
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String cleaned = cleanString(prefix);
        List<String> namesCleaned = trie.keysWithPrefix(cleaned);
        List<String> returned = new ArrayList<>();

        for(String n : namesCleaned) {
            List<Node> lst = cleanToDirty.get(n);
            for(Node m : lst) {
                returned.add(m.name());
            }
        }

        return returned;
    }

    /**
     * For Project Part III (extra credit)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     *
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String cleaned = cleanString(locationName);
        List<Map<String, Object>> rtn = new ArrayList<>();
        List<Node> needed = cleanToDirty.get(cleaned);
        for (Node n : needed) {
            Map<String, Object> m = new HashMap<>();
            m.put("lat", n.lat());
            m.put("lon", n.lon());
            m.put("name", n.name());
            m.put("id", n.id());
            rtn.add(m);
        }
        return rtn;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}