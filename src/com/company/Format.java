package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Format {
    int[][] maze;
    int width = 51, length = 31;

    public int[][] getMaze() {
        return maze;
    }

    public Format() throws FileNotFoundException, WrongInputException, InvalidColumnLengthException {
        this.maze = readFile("test.txt");
    }

    public int[][] readFile(String fileName) throws FileNotFoundException, WrongInputException, InvalidColumnLengthException {
        int[][] maze = new int[31][51];
        Scanner scanner = new Scanner(new File(fileName));
        int previousLine = 0;
        int nLine = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            previousLine = line.length();
            if(!(line.length() == previousLine)){
                throw new InvalidColumnLengthException("Kolumny nie są równej długości!");
            }
            for(int i = 0; i < line.length(); i++){
                if(line.charAt(i) == '#'){
                    maze[nLine][i] = 1;
                }
                else if(line.charAt(i) == ' '){
                    maze[nLine][i] = 0;
                }
                else if(line.charAt(i) == 'E'){
                    maze[nLine][i] = 3;
                }
                else{
                    throw new WrongInputException("Złe wejście!");
                }
            }
            nLine++;
        }
        return maze;
    }

    public void printMaze(int[][] maze){
        for(int i = 0; i < 31; i++){
            for(int j = 0; j < 51; j++){
                if(maze[i][j]==1){
                    System.out.print("#");
                }
                else if(maze[i][j]==4){
                    System.out.print("*");
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public void showInteger(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < length; j++){
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }
}
