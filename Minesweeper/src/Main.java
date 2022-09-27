import java.util.Random;
import java.util.Scanner;
/*
Basics of this program: minefield is a boolean double array where true means there's a bomb there. main is the
visuals shown to the player, which changes depending on the locations of bombs in minefield. Any function has a basic
description to say what/how it works.
 */


public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Hello");

        boolean[][] minefield;
        int[][] main = new int[15][30];
        for (int x = 0; x < 15; x++)
        {
            for (int y = 0; y < 30; y++)
            {
                main[x][y] = 9;
            }
        }
        print_show(main);

        boolean game = true;
        int x;
        int y;
        System.out.print("Enter x coord: ");
        x = in.nextInt();
        System.out.print("Enter y coord: ");
        y = in.nextInt();
        minefield = generate_field(x, y);
        reveal_num(main, minefield, x, y);
        print_show(main);

        do {
            System.out.print("Enter x coord: ");
            x = in.nextInt();
            System.out.print("Enter y coord: ");
            y = in.nextInt();
            if (minefield[x][y])
            {
                print_show(main);
                System.out.println("Bomb! You lose.");
                game = false;
            }
            else
            {
                reveal_num(main, minefield, x, y);

                if (win_check(main, minefield))
                {
                    System.out.println("You win!");
                    game = false;
                }
                print_show(main);
            }



        } while (game);


    }


    public static boolean[][] generate_field(int xStart, int yStart) {
        //generates random pattern for bombs, returns boolean double array of bombs.

        int bombs = 100; // choose how many bombs you want
        boolean[][] board = new boolean[15][30];
        Random rand = new Random();

        //adds mines to board
        int x = rand.nextInt(15);
        int y = rand.nextInt(30);
        for (int i = 1; i <= bombs; i++) {
            boolean counted = false;
            while (!counted) {
                x = rand.nextInt(15);
                y = rand.nextInt(30);
                //loop makes sure no mines are on the first click or adjacent tiles for a consistent starting point
                if (!board[x][y] && (Math.abs(x - xStart) > 1 || Math.abs(y - yStart) > 1))
                {
                    counted = true;
                    board[x][y] = true;
                }
            }
        }
        return board;
    }

    public static void print_field (boolean[][] array) {
        //prints the show_board in a visually understanding way
        System.out.println("    0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29");
        System.out.println("   -----------------------------------------------------------------------------------------");

        for (int x = 0; x < 15; x++)
        {
            System.out.printf("%3d| ", x);
            for (int y = 0; y < 30; y++)
            {
                if (array[x][y])
                {
                    System.out.print("X  ");
                }
                else
                {
                    System.out.print("o  ");
                }
            }
            System.out.println();

        }
    }
    public static void print_show (int[][] array) {
        //prints bomb pattern (not currently implemented, but useful for bug checking)
        System.out.println("    0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29");
        System.out.println("   -----------------------------------------------------------------------------------------");

        for (int x = 0; x < 15; x++)
        {
            System.out.printf("%3d| ", x);
            for (int y = 0; y < 30; y++)
            {
                System.out.print(array[x][y] + "  ");
            }
            System.out.println();

        }
    }
    public static void reveal_num (int[][] main, boolean[][] minefield, int x, int y) {
        //counts bombs around a tile, then reveals surrounding blocks if there are no adjacent bombs
        int count = 0;
        //System.out.println("Revealing a num!"); //bug testing

        //counts bombs around it, checks for 'edge' cases
        if (y < 29) { if (minefield[x][y+1]) {count += 1;} }
        if (y > 0) { if (minefield[x][y-1]) {count += 1;} }
        if (y < 29 && x < 14) { if (minefield[x+1][y+1]) {count += 1;} }
        if (y != 0 && x < 14) { if (minefield[x+1][y-1]) {count += 1;} }
        if (y < 29 && x > 0) { if (minefield[x-1][y+1]) {count += 1;} }
        if (y > 0 && x > 0) { if (minefield[x-1][y-1]) {count += 1;} }
        if (x > 0) { if (minefield[x-1][y]) {count += 1;} }
        if (x < 14) { if (minefield[x+1][y]) {count += 1;} }
        main[x][y] = count;

        /*
        if there are no adjacent bombs, recursively call this function to reveal adjacent tiles. It only calls
        the function again if the next tile hasn't been revealed yet, to minimize recursion and improve speed.
        all if statements are separate bc you need to check the current tile's location before calling adjacent
       tiles to make sure you don't get an index range error.
        */
        if (count == 0)
        {
            if (y < 29) { if (x < 14) {if(main[x+1][y+1] == 9){reveal_num(main, minefield, x+1, y+1);}}}
            if (x < 14) {if(main[x+1][y] == 9){reveal_num(main, minefield, x+1, y);}}
            if (y > 0) { if (x < 14) {if(main[x+1][y-1] == 9){reveal_num(main, minefield, x+1, y-1);}}}
            if (y > 0)  {if(main[x][y-1] == 9){reveal_num(main, minefield, x, y-1);}}
            if (y > 0) { if (x > 0) {if(main[x-1][y-1] == 9){reveal_num(main, minefield, x-1, y-1);}}}
            if (x > 0) {if(main[x-1][y] == 9){reveal_num(main, minefield, x-1, y);}}
            if (y < 29) { if (x > 0) {if(main[x-1][y+1] == 9){reveal_num(main, minefield, x-1, y+1);}}}
            if (y < 29) {if(main[x][y+1] == 9){reveal_num(main, minefield, x, y+1);}}
        }
    }

    public static void shortcut_reveal(int[][] main, boolean[][] minefield, int x, int y) {
        /*
        needs to:
        check adjacent tiles for UNMARKED bombs. if no unmarked bombs, reveal adjacent hidden tiles w reveal_num.
        can't implement yet bc there's no way to test for marked bombs.
         */
    }
    public static void mark_tile(int[][] main, boolean[][] minefield, int x, int y) {
        /*
        replace unrevealed square w a marked square
         */
    }



    public static boolean win_check(int[][] main, boolean[][] minefield) {
        //checks if all tiles are either shown or mines, if so returns true
        for(int x = 0; x < 15; x++)
        {
            for(int y = 0; y < 30; y++)
            {
                if (main[x][y] == 9 && !minefield[x][y])
                    return false;
            }
        }
        return true;
    }
}
