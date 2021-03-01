package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

interface Things {
    String line = "*".repeat(30);
}

public class Interface implements Things {
    public static void main(String[] args) throws FileNotFoundException, InvalidColumnLengthException, WrongInputException {
        menu();
    }

    /**
     * Main display function
     * @throws FileNotFoundException
     * @throws InvalidColumnLengthException
     * @throws WrongInputException
     */
    private static void menu() throws FileNotFoundException, InvalidColumnLengthException, WrongInputException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.format("\n%s\n*       GRA W LABIRYNT       *\n" +
                        line, line);
                System.out.format("\n*            Menu:           *\n");
                System.out.format(line + "\n*   [1]\tGRA                  *\n"
                        + "*   [2]\tAUTORZY              *\n" +
                        "*   [3]\tWYJSCIE Z GRY        *\n" + line +
                        "\nWybierz opcje: ");
                int opt = sc.nextInt();
                System.out.println();

                switch (opt) {
                    case 1:
                        game();
                        break;
                    case 2:
                        authors();
                        break;
                    case 3:
                        return;
                    case 4:
                    default:
                        System.out.println("taka mozliwosc nie istnieje");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("nalezy wpisac cyfre");
                menu();
            }
        }
    }

    /**
     * Reads maze from file
     * @param fileName
     */
    private static void read(String fileName) {
        System.out.println("wczytuje plik " + fileName);
        try {
            Solver solver = new Solver(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("nie znaleziono pliku " + fileName);
        } catch (InvalidColumnLengthException e) {
            System.out.println("podany plik zawiera błędną liczę kolumn");
        } catch (WrongInputException e) {
            System.out.println("błędne dane wejściowe");
        }

    }

    /**
     * Displays authors credentials
     */
    private static void authors() {
        System.out.println(line + "\n*   Tworcy gry:              *\n" + line +
                "\n*   Jan Golianek             *\n" +
                "*   Szymon Włoczewski        *\n" +
                "*   Kacper Małkiewicz        *\n" + line);
        System.out.println("*   [1]\tpowrot do menu       *\n" + line + "\n wybierz opcje:");
        Scanner sc1 = new Scanner(System.in);
        //input control loop
        while (true) {
            try {
                int opt1 = sc1.nextInt();
                if (opt1 == 1) {
                    return;
                } else {
                    System.out.println("wpisz odpowiednia cyfre");
                }
            } catch (InputMismatchException e) {
                authors();
            }
                break;
        }
    }

    /**
     * Provides service for maze functions
     * @throws InvalidColumnLengthException
     * @throws WrongInputException
     * @throws FileNotFoundException
     */
    private static void game() throws InvalidColumnLengthException, WrongInputException, FileNotFoundException {
        System.out.println(line + "\n*   proszę wybrać sposób\t *\n*\tgenerowania labiryntu    *\n" +
                line + "\n*   [1]\tDFS                  *\n" +
                "*   [2]\tKruskal              *" +
                "\n*   [3]\todczyt z pliku       *\n" + line +
                "\n*   [4]\tpowrot do menu       *\n" +
                "\nwybierz opcje:");
        Scanner sc2 = new Scanner(System.in);
        int declaredWidth, declaredHeight;
        String questionSave;

        while (true) {
            try {
                int opt2 = sc2.nextInt();
                switch (opt2) {
                    case 1:
                        System.out.println("tworzenie labiryntu za pomocą randomizowanego algorytmu DFS");
                        System.out.println("prosze podać szerokość");
                        declaredWidth = sc2.nextInt();
                        System.out.println("proszę podać wysokość");
                        declaredHeight = sc2.nextInt();
                        MazeDFS mazeDFS = new MazeDFS(declaredWidth, declaredHeight);
                        mazeDFS.generateMaze();
                        mazeDFS.showMaze();
                        System.out.println();
                        System.out.println("Czy zapisać ten labirynt? t/n");
                        questionSave = sc2.next();
                        Solver solver = new Solver(mazeDFS.formatMaze(), mazeDFS.getWidth(), mazeDFS.getHeight());
                        if (questionSave.charAt(0) == 't') {
                            System.out.println("proszę podać nazwę pliku, musi konczyć się na .txt");
                            String fileName = "";
                            //sprawdzenie poprawności nazwy
                            boolean fraudControl = true;
                            while (fraudControl) {
                                fileName = sc2.next();
                                if (!fileName.endsWith(".txt")) {
                                    System.out.println("proszę podać prawidłową nazwę pliku z rozszerzeniem txt");
                                    fraudControl = true;
                                } else fraudControl = false;
                            }
                            System.out.println();
                            try {
                                mazeDFS.saveFile(fileName);
                            } catch (FileNotFoundException e) {
                                System.out.println("podana nazwa pliku jest błędna");
                            }

                            System.out.println("\nproponowana ścieżka przejścia:\n");

                            solver.findPath();

                        } else if (questionSave.charAt(0) == 'n') {

                            System.out.println("\nproponowana ścieżka przejścia:\n");
                            solver.findPath();

                            System.out.println("\npowracam do menu");
                            return;
                        } else System.out.println("podano zły znak");

                        return;
                    case 2:
                        System.out.println("tworzenie labiryntu za pomocą randomizowanego algorytmu Kruskala");
                        MazeKruskal mazeKruskal = new MazeKruskal();
                        System.out.println("prosze podac szerokość ");
                        declaredWidth = sc2.nextInt();
                        System.out.println("prosze podać wysokość ");
                        declaredHeight = sc2.nextInt();
                        System.out.println();
                        mazeKruskal.generateMaze(declaredWidth, declaredHeight);
                        mazeKruskal.showMap();
                        System.out.println();
                        System.out.println("Czy zapisać ten labirynt? t/n");
                        questionSave = sc2.next();
                        if (questionSave.charAt(0) == 't') {
                            System.out.println("proszę podać nazwę pliku, musi konczyć się na .txt");
                            String fileName = "";
                            //sprawdzenie poprawności nazwy
                            boolean fraudControl = true;
                            while (fraudControl) {
                                fileName = sc2.next();
                                if (!fileName.endsWith(".txt")) {
                                    System.out.println("proszę podać prawidłową nazwę pliku z rozszerzeniem txt");
                                    fraudControl = true;
                                } else fraudControl = false;
                            }
                            System.out.println();

                            try {
                                mazeKruskal.saveFile(fileName);
                                Solver solver1 = new Solver(fileName);
                            } catch (FileNotFoundException e) {
                                System.out.println("podana nazwa pliku jest błędna");
                            }

                            System.out.println();
                        } else if (questionSave.charAt(0) == 'n') {
                            mazeKruskal.saveFile("temp.txt");
                            Solver solver2 = new Solver("temp.txt");
//                            solver2.findPath();
                            File temp = new File("temp.txt");
                            temp.delete();
                            System.out.println("\npowracam do menu");
                            return;
                        } else System.out.println("podano zły znak");

                        return;
                    case 3:
                        System.out.println("prosze podać nazwę pliku tekstowego");
                        String fileName;
                        fileName = sc2.next();

                        try {
                            read(fileName);
                        } catch (Exception e) {
                            System.out.println(
                                    "napotkano problem ze znalezieniem pliku " + fileName);
                        }
                    case 4:
                        return;

                    default:
                        System.out.println("wpisz odpowiednia cyfre");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("podano zły znak");
                return;
            }
        }
    }
}
