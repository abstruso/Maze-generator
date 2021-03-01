package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class MazeKruskal {
    public class Edge {
        public int x1, y1, x2, y2;

        public Edge(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Edge{");
            sb.append("x1=").append(x1);
            sb.append(", y1=").append(y1);
            sb.append(", x2=").append(x2);
            sb.append(", y2=").append(y2);
            sb.append('}');
            return sb.toString();
        }
    }


    Set<Edge> set;                     //edge collection
    Vector<ArrayList<Pixel>> vektor;   //collections of merged pixels
    boolean[][] map;
    int width, height;

    /**
     * Generates maze with randomized Kruskal algorithm
     *
     * @param width
     * @param height
     */
    public void generateMaze(int width, int height) {
        //dimensions must be odd
        if (width % 2 == 0) width++;
        if (height % 2 == 0) height++;
        this.width = width;
        this.height = height;
        set = new HashSet<>();
        vektor = new Vector<>();
        map = new boolean[2 * height - 1][2 * width - 1];
        //preparation of set and vector
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                vektor.add(new ArrayList<Pixel>());     //adding list to vector
                vektor.get(vektor.size() - 1).add(new Pixel(j, i));
                // on the begining in each list there is only one pixel

                if (j > 0) {
                    set.add(new Edge(j - 1, i, j, i));
                }
                if (i > 0) {
                    set.add(new Edge(j, i - 1, j, i));
                }
            }
        }
        //map preparation
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = i % 2 == 0 && j % 2 == 0;
            }
        }

        Random rand = new Random();
        Edge randomEdge;
        int allEdges = set.size();
        while (!set.isEmpty()) {
            randomEdge = randomFromSet();
            set.remove(randomEdge);     //getting random edge

            if (!merged(randomEdge)) {
                merge(randomEdge);
                //here maze is created
            }
        }

        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    map[i][j] = false;
                }
            }
        }
    }

    public void showMap() {

        System.out.println("# " + "#".repeat(width));
        for (int i = 0; i < this.height; i++) {
            System.out.print("#");
            for (int j = 0; j < this.width; j++) {
                if (map[i][j]) System.out.print(" ");
                else if (!map[i][j]) System.out.print("#");
            }
            System.out.print("#\n");
        }
        System.out.println("#".repeat(width) + " #");

    }

    public void saveFile(String fileName) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(fileName);
        pw.println("# " + "#".repeat(width));
        for (int i = 0; i < this.height; i++) {
            pw.print("#");
            for (int j = 0; j < this.width; j++) {
                if (map[i][j]) pw.print(" ");
                else if (!map[i][j]) pw.print("#");
            }
            pw.println("#");
        }
        pw.println("#".repeat(width) + "E#");
        pw.close();
    }

    /**
     * Method transform maze to enum form
     *
     * @return 2D table of enums
     */
    public Element[][] formatMaze() {
        Element[][] maze = new Element[height + 2][width + 2];
        maze[0][0] = Element.WALL;
        maze[0][1] = Element.WALL;
        for (int ii = 2; ii < width + 2; ii++) {
            maze[0][ii] = Element.WALL;
        }
        for (int i = 1; i < height; i++) {
            maze[i][0] = Element.WALL;
            for (int j = 1; j < width; j++) {
                if (!map[i][j]) {
                    maze[i][j] = Element.WALL;
                } else {
                    maze[i][j] = Element.PATH_UNVISITED;
                }
            }
            maze[i][width] = Element.WALL;
        }
        for (int k = 0; k < width - 1; k++) {
            maze[height][k] = Element.WALL;
        }
        maze[height][width - 1] = Element.EXIT;
        maze[height][width] = Element.WALL;
        return maze;
    }


    /**
     * Checks if pixels from given edge are in the same collection
     *
     * @param edge
     * @return
     */
    public boolean merged(Edge edge) {
        Pixel one = new Pixel(edge.x1, edge.y1);
        Pixel two = new Pixel(edge.x2, edge.y2);


        for (ArrayList<Pixel> i : vektor
        ) {
            if (i.contains(one) && i.contains(two)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Migrates pixels from given edge, and all other form their collection, to common list
     *
     * @param edge
     */
    public void merge(Edge edge) {
        Pixel one = new Pixel(edge.x1, edge.y1);
        Pixel two = new Pixel(edge.x2, edge.y2);
        int list1 = 0, list2 = 0;


        for (ArrayList<Pixel> iter : vektor
        ) {
            if (iter.contains(one) && !iter.contains(two)) {
                iter.add(two);
                break;
            }
            list1++;

        }

        for (ArrayList<Pixel> iter2 : vektor
        ) {
            if (!iter2.contains(one) && iter2.contains(two)) {
                iter2.remove(two);
                break;
            }
            list2++;
        }
        //migrates remaining objects
        while (vektor.get(list2).size() > 0) {
            vektor.get(list1).add(vektor.get(list2).get(0));
            vektor.get(list2).remove(0);
        }
        //marking on map
        Pixel between = new Pixel((one.x + two.x) / 2, (one.y + two.y) / 2);
        map[between.y][between.x] = true;
    }

    public Edge randomFromSet() {
        int size = set.size();
        if (size > 0) {
            int item = new Random().nextInt(size);
            int i = 0;
            for (Edge obj : set) {
                if (i == item)
                    return obj;
                i++;
            }
            return null;
        }
        return null;
    }

    //wymiary powiększone o 3 bo:
    //+1 za parzystość wejścia
    //+2 za wyświetlenie ograniczających #e
    public int getWidth() {
        return width + 2;
    }

    public int getHeight() {
        return height + 2;
    }
}


