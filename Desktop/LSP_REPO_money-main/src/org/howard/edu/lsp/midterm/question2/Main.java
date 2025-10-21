package org.howard.edu.lsp.midterm.question2;

public class Main {
    public static void main(String[] args) {
                System.out.println("Circle radius 3.0 → area = " + AreaCalculator.area(3.0));
        System.out.println("Rectangle 5.0 x 2.0 → area = " + AreaCalculator.area(5.0, 2.0));
        System.out.println("Triangle base 10, height 6 → area = " + AreaCalculator.area(10, 6));
        System.out.println("Square side 4 → area = " + AreaCalculator.area(4));
         try {
            System.out.println("Circle radius -5 → area = " + AreaCalculator.area(-5.0));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /*
     * Overloading is the better design choice here because all the methods perform the same general 
     * task—calculating the area—but for different shapes with different parameters. Using the same 
     * method name makes the code cleaner, easier to read, and more intuitive, since the operation’s 
     * purpose doesn’t change. Having separate names like `circleArea` or `rectangleArea` would be 
     * repetitive and less elegant.

     */
}