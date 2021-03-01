package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Creates and saves to file maze generated with Deep First Search Algorithm
 */
public class MazeDFS {

    int[][] maze;
    int height, width;
    Stack<Pixel> stack;

    public MazeDFS(int width, int height) {
        if (height >= 3 && width >= 3) {
            this.height = height;
            this.width = width;

            if (height % 2 == 0) {
                this.height++;
            }
            if (width % 2 == 0) {
                this.width++;
            }
        } else
            System.out.println("podane wymiary są zbyt małe");
    }

    /**
     * Important method generating maze as two dimensional int table
     *
     * @return 2D array with generated maze
     */
    public int[][] generateMaze() {
        maze = new int[height][width];
        System.out.println("tworze labirynt " + width + " " + height);

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                maze[i][j] = 1;
        int row = 1;
        int column = 1;
        maze[row][column] = 0;
        stack = new Stack<>();
        stack.push(new Pixel(row, column));
        while (!stack.empty()) {
            recursion(stack.peek().getRow(), stack.pop().getColumn());
        }
//maze entry and exit on diagonal
        maze[0][1] = 0;
        maze[height - 1][width - 2] = 3;
        return maze;
    }

    public void recursion(int row, int column) {
        Integer[] randDirs = generateRandomDirections();
        // generating next random directions
        for (int i = 0; i < randDirs.length; i++) {

            //sprawdzanie dwie komórki w wybranym kierunku
            switch (randDirs[i]) {
                case 1: // Up
                    //checking if Pixel was visited
                    //possible visiting and marking Pixel
                    //minimal step by two cells to left space for walls and floors
                    if (row - 2 <= 0)
                        continue;
                    if (maze[row - 2][column] != 0) {
                        maze[row - 2][column] = 0;
                        maze[row - 1][column] = 0;
                        stack.push(new Pixel(row - 2, column));
                    }
                    break;
                case 2: // Right
                    if (column + 2 >= width - 1)
                        continue;
                    if (maze[row][column + 2] != 0) {
                        maze[row][column + 2] = 0;
                        maze[row][column + 1] = 0;
                        stack.push(new Pixel(row, column + 2));
                    }
                    break;
                case 3: // Down
                    if (row + 2 >= height - 1)
                        continue;
                    if (maze[row + 2][column] != 0) {
                        maze[row + 2][column] = 0;
                        maze[row + 1][column] = 0;
                        stack.push(new Pixel(row + 2, column));
                    }
                    break;
                case 4: // Left
                    if (column - 2 <= 0)
                        continue;
                    if (maze[row][column - 2] != 0) {
                        maze[row][column - 2] = 0;
                        maze[row][column - 1] = 0;
                        stack.push(new Pixel(row, column - 2));
                    }
                    break;
            }
        }
    }

    /**
     * Generate an array with random directions 1-4
     *
     * @return Array containing 4 directions in random order
     */
    public Integer[] generateRandomDirections() {
        ArrayList<Integer> randoms = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            randoms.add(i + 1);
        Collections.shuffle(randoms);

        return randoms.toArray(new Integer[4]);
    }

    public void showMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze[i][j] == 1) {
                    System.out.print("#");
                } else System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * Saves maze to file with given name
     *
     * @param fileName name of the file with maze
     * @throws FileNotFoundException when file does not exist
     */
    public void saveFile(String fileName) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(fileName);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze[i][j] == 1) {
                    printWriter.print("#");
                } else if (maze[i][j] == 3) {
                    printWriter.printf("E");
                } else {
                    printWriter.printf(" ");
                }
            }
            printWriter.println();
        }
        printWriter.close();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


    /**
     * Method transform maze to enum form
     *
     * @return 2D table of enums
     */
    public Element[][] formatMaze() {
        Element[][] map = new Element[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze[i][j] == 1) {
                    map[i][j] = Element.WALL;
                } else if (maze[i][j] == 3) {
                    map[i][j] = Element.EXIT;
                } else {
                    map[i][j] = Element.PATH_UNVISITED;
                }
            }
        }
        return map;
    }
}
