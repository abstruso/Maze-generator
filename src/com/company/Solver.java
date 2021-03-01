package com.company;

import java.io.*;
import java.util.Scanner;

public class Solver {
    private final int width;
    private final int height;
    private final Element[][] maze;

    public Solver(Element[][] maze, int width, int height) {
        this.maze = maze;
        this.width = width;
        this.height = height;
    }

    /**
     * Creates sover basing on given file
     * @param fileName
     * @throws FileNotFoundException
     * @throws InvalidColumnLengthException
     * @throws WrongInputException
     */
    public Solver(String fileName) throws FileNotFoundException, InvalidColumnLengthException, WrongInputException {
        Scanner scanner = new Scanner(new File(fileName));
        int lineCount = 0;
        while (scanner.hasNextLine()) {
            lineCount++;
            scanner.nextLine();
        }
        this.height = lineCount;

        Scanner scanner1 = new Scanner(new File(fileName));
        String line = scanner1.nextLine();
        this.width = line.length();

        maze = new Element[height][width];
        readFile(fileName);
    }

    /**
     * Method operates with finding path operations
     */
    public void findPath() {
        if (solve(1, 1)) {
            printMaze();
        } else {
            System.out.println("wystąpił błąd\n");
        }
    }

    /**
     * Important path finding method
     *
     * @param i starting point column
     * @param j starting point row
     * @return true if maze can be solved
     */
    public boolean solve(int i, int j) {
        if (maze[i][j] == Element.WALL) {
            return false;
        }
        if (maze[i][j] == Element.EXIT) {
            return true;
        }
        if (maze[i][j] == Element.PATH_VISITED) {
            return false;
        }

        maze[i][j] = Element.PATH_VISITED;

        if ((solve(i + 1, j))) {
            return true;
        }

        if ((solve(i, j - 1))) {
            return true;
        }

        if ((solve(i, j + 1))) {
            return true;
        }

        if ((solve(i - 1, j))) {
            return true;
        }

        maze[i][j] = Element.PATH_UNVISITED;
        return false;
    }

    /**
     * Printing maze with marked path
     */
    private void printMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze[i][j] == Element.WALL) {
                    System.out.print("#");
                } else if (maze[i][j] == Element.PATH_VISITED) {
                    System.out.print("*");
                } else if(maze[i][j] == Element.EXIT){
                    System.out.print("E");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Method reading maze from file
     *
     * @param fileName describes from where to read
     * @throws FileNotFoundException when file does not exist
     */
    public void readFile(String fileName) throws FileNotFoundException, WrongInputException, InvalidColumnLengthException {
        Scanner scanner = new Scanner(new File(fileName));
        int previousLine = width;
        int nLine = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!(line.length() == previousLine)) {
                throw new InvalidColumnLengthException("Kolumny nie są równej długości!");
            }
            previousLine = line.length();
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '#') {
                    maze[nLine][i] = Element.WALL;
                } else if (line.charAt(i) == ' ') {
                    maze[nLine][i] = Element.PATH_UNVISITED;
                } else if (line.charAt(i) == 'E') {
                    maze[nLine][i] = Element.EXIT;
                } else {
                    throw new WrongInputException("W pliku znajduje sie nieprawidlowy znak");
                }
            }
            nLine++;
        }
        findPath();
    }
}
