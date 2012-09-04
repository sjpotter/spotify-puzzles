/* 
 Copyright (c) 2012 Shaya Potter <spotter@gmail.com> 

 Java Port and bug fix of 
 http://stackoverflow.com/questions/521525/how-can-i-compute-the-minimum-bipartite-vertex-cover
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Bilateral {
    private int vertexCount;
    private HashMap<Integer, HashSet<Integer>> edges;
    private HashSet<Integer> leftVertices;
    private HashSet<Integer> rightVertices;
    private HashSet<Integer> vertexCoverResult;
    private int[] matching;
    private HashMap<Integer, Integer> mapping;

    private void PrintResults() {
       System.out.println(vertexCoverResult.size());
       for(int i : vertexCoverResult) {
           System.out.println(mapping.get(i));
       }
    }

    private void findVertexCover() throws Exception {
        findBipartiteMatching();

        HashSet<Integer> treeSet = new HashSet<Integer>();
        for (int v : leftVertices)
            if (matching[v] < 0)
                depthFirstSearch(treeSet, v, false);
        
        Set<Integer> intersect = new HashSet<Integer>(rightVertices);
        intersect.retainAll(treeSet);
        
        Set<Integer> difference = new HashSet<Integer>(leftVertices);
        difference.removeAll(treeSet);
        
        vertexCoverResult = new HashSet<Integer>(difference);
        vertexCoverResult.addAll(intersect);        
    }

    private void depthFirstSearch(HashSet<Integer> TreeSet, int v, boolean right) {
        if (TreeSet.contains(v))
            return;
        
        TreeSet.add(v);
        
        if (!right) {
            for (int u : edges.get(v))
                if (u != matching[v])
                    depthFirstSearch(TreeSet, u, true);
        } else if (matching[v] >= 0)
            depthFirstSearch(TreeSet, matching[v], false);

    }

    private void findBipartiteMatching() throws Exception {
        Bicolorate();
        
        for(int i=0 ; i < vertexCount; i++) {
            matching[i] = -1;
        }
        
        for (int i : leftVertices) {
            boolean[] seen = new boolean[vertexCount];
            BipartiteMatchingInternal(seen, i); 
        }
    }

    private boolean BipartiteMatchingInternal(boolean[] seen, int u) {
        for (int v : edges.get(u)) {
            if (seen[v]) 
                continue;
            
            seen[v] = true;
            
            if (matching[v] < 0 || BipartiteMatchingInternal(seen, matching[v])) {
                matching[u] = v;
                matching[v] = u;
                return true;
            }
        }
        return false;
    }

    private void Bicolorate() throws Exception {
        leftVertices = new HashSet<Integer>();
        rightVertices = new HashSet<Integer>();

        int[] colors = new int[vertexCount];
        for (int i = 0; i < vertexCount; i++)
            if (colors[i] == 0 && !BicolorateInternal(colors, i, 1))
                throw new Exception("Graph is NOT bipartite.");
    }

    private boolean BicolorateInternal(int[] colors, int i, int color) {
        if (colors[i] == 0) {
            if (color == 1) {
                leftVertices.add(i);
            } else { 
                rightVertices.add(i);
            }
            colors[i] = color;
        } else if (colors[i] != color) {
            return false;
        } else {
            return true;
        }

        for (int j : edges.get(i))
            if (!BicolorateInternal(colors, j, 3 - color))
                return false;
        
        return true;
    }

    private void parseInput() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        HashMap<Integer, Integer> internal_mapping = new HashMap<Integer, Integer>();

        int count = 0;
        mapping = new HashMap<Integer, Integer>();

        String input;
        input = in.readLine();
        int edgeCount = Integer.parseInt(input);

        edges = new HashMap<Integer, HashSet<Integer>>();

        for (int i = 0; i < edgeCount; i++) {
            int x, y;
            input = in.readLine();
            StringTokenizer st = new StringTokenizer(input);
            
            x = Integer.parseInt(st.nextToken());
            y = Integer.parseInt(st.nextToken());
            
            if (!internal_mapping.containsKey(x)) {
                internal_mapping.put(x, count);
                mapping.put(count, x);
                x = count++;
            } else {
                x = internal_mapping.get(x);
            }
            if (!internal_mapping.containsKey(y)) {
                internal_mapping.put(y, count);
                mapping.put(count, y);
                y = count++;
            } else {
                y = internal_mapping.get(y);
            }
            
            if (!edges.containsKey(x)) {
                edges.put(x, new HashSet<Integer>());
            }
            if (!edges.containsKey(y)) {
                edges.put(y, new HashSet<Integer>());
            }
            
            edges.get(x).add(y);
            edges.get(y).add(x);
        }
        
        vertexCount = mapping.size();
        matching = new int[vertexCount];
    }
    
    public static void main(String[] args) throws Exception {
        Bilateral v = new Bilateral();
        v.parseInput();
        v.findVertexCover();
        v.PrintResults();
    }
}