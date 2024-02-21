import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final char DOT_HUMAN = 'X';

    private static final char DOT_AI = 'O';

    private static final char DOT_EMPTY = '*';

    private static final Scanner scanner = new Scanner(System.in);

    private static final Random random = new Random();

    private static final int WIN_COUNT = 4;

    private static char[][] field;

    private static int fieldSizeX;

    private static int fieldSizeY;

    static void initialize() {

        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeX][fieldSizeY];

        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field.length; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    static void printField() {

        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");

        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print((x + 1) + "|");
            for (int y = 0; y < field.length; y++) {
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    static void humanTurn() {

        int x;
        int y;
        do {
            System.out.print("Input coords x and y separated by space: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    static void aiTurn() {

        if (winAi(WIN_COUNT))
            return;
        if (blockAi(WIN_COUNT))
            return;
        if (predictAi(WIN_COUNT))
            return;
        int x;
        int y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    static boolean blockAi(int win) {

        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = DOT_HUMAN;
                    if (checkWin(DOT_HUMAN, win)) {
                        field[i][j] = DOT_AI;
                        return true;
                    } else
                        field[i][j] = DOT_EMPTY;
                }
            }
        }
        return false;
    }

    static boolean predictAi(int win) {

        if (checkWin(DOT_HUMAN, win - 1))
            return false; // when there're 3 X but no need to block
        return blockAi(win - 1);
    }

    static boolean moveAi(int win) {

        if (checkWin(DOT_AI, win)) return false; // when it's stuck
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = DOT_AI;
                    if (checkWin(DOT_AI, win)) {
                        return true;
                    } else
                        field[i][j] = DOT_EMPTY;
                }
            }
        }
        return false;
    }

    static boolean winAi(int win) { // try to make it a bit smarter)

        return moveAi(win);
    }

    static boolean checkDraw() {

        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field.length; y++) {
                if (isCellEmpty(x, y))
                    return false;
            }
        }
        return true;
    }

    static boolean isCellEmpty(int x, int y) {

        return field[x][y] == DOT_EMPTY;
    }

    static boolean isCellValid(int x, int y) {

        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    static boolean checkWin(char dot) { // V1 for cube

        boolean flag = true;

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j] != dot) {
                    flag = false;
                    break;
                }
            }

            if (flag)
                return flag;
            flag = true;

            for (int j = 0; j < field.length; j++) {
                if (field[j][i] != dot) {
                    flag = false;
                    break;
                }
            }

            if (flag)
                return flag;
            flag = true;
        }

        for (int i = 0; i < field.length; i++) {

            if (field[i][i] != dot) {
                flag = false;
                break;
            }
        }

        if (flag)
            return flag;
        flag = true;

        for (int i = 0; i < field.length; i++) {

            if (field[i][field.length - 1 - i] != dot) {
                flag = false;
                break;
            }
        }

        return flag;
    }

    static boolean checkWin(char dot, int win) {

        if (check1(dot, win) || check2(dot, win) || check3(dot, win))
            return true;
        return false;
    }

    static boolean check1(char dot, int win) {

        String row = "";
        for (int i = 0; i < fieldSizeX - (win - 1); i++) {
            for (int k = 0; i + k < fieldSizeX && i + k < fieldSizeY; k++) {
                row = row + field[i + k][k];
            }
            if (row.contains(Character.toString(dot).repeat(win)))
                return true;
            row = "";
        }
        for (int j = 1; j < fieldSizeX - win + 1; j++) {
            for (int k = 0; j + k < fieldSizeX && j + k < fieldSizeY; k++) {
                row = row + field[k][j + k];
            }
            if (row.contains(Character.toString(dot).repeat(win)))
                return true;
            row = "";
        }

        return false;
    }

    static boolean check2(char dot, int win) {

        String row = "";
        for (int i = 0; i < fieldSizeX - (win - 1); i++) {
            for (int k = 0; i + k < fieldSizeX && i + k < fieldSizeY; k++) {
                row = row + field[fieldSizeX - (i + k + 1)][k];
            }
            if (row.contains(Character.toString(dot).repeat(win)))
                return true;
            row = "";
        }
        for (int j = 1; j < fieldSizeX - win + 1; j++) {
            for (int k = 0; j + k < fieldSizeX && j + k < fieldSizeY; k++) {
                row = row + field[fieldSizeX - (k + 1)][j + k];
            }
            if (row.contains(Character.toString(dot).repeat(win)))
                return true;
            row = "";
        }

        return false;
    }

    static boolean check3(char dot, int win) {

        String row = "";
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                row += field[i][j];
            }
            if (row.contains(Character.toString(dot).repeat(win)))
                return true;
            row = "";
        }

        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                row += field[j][i];
            }
            if (row.contains(Character.toString(dot).repeat(win)))
                return true;
            row = "";
        }

        return false;
    }

    static boolean checkState(char dot, String s) {

        if (checkWin(dot, WIN_COUNT)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Draw!");
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        while (true) {

            initialize();
            printField();

            while (true) {

                humanTurn();
                printField();
                if (checkState(DOT_HUMAN, "You win!"))
                    break;
                aiTurn();
                printField();
                if (checkState(DOT_AI, "You lose("))
                    break;
            }

            System.out.println("Wanna play more? Y - for yes");
            if (!scanner.next().equalsIgnoreCase("y"))
                break;
        }
    }
}