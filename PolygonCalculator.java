import java.util.Scanner;

// ------------------ Polygon interface ------------------
interface Polygon {
    double area();
    double perimeter();
}

// ------------------ Triangles ------------------
abstract class Triangle implements Polygon {
    // leave implementation to subclasses or concrete triangle
}

class GeneralTriangle extends Triangle {
    private double a, b, c; // side lengths

    public GeneralTriangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double perimeter() {
        return a + b + c;
    }

    @Override
    public double area() {
        // Heron's formula
        double s = perimeter() / 2.0;
        double val = s * (s - a) * (s - b) * (s - c);
        return val <= 0 ? 0 : Math.sqrt(val);
    }
}

class IsoscelesTriangle extends Triangle {
    private double base, side; // two equal sides = side, base = base

    public IsoscelesTriangle(double base, double side) {
        this.base = base;
        this.side = side;
    }

    @Override
    public double perimeter() {
        return base + 2 * side;
    }

    @Override
    public double area() {
        // height = sqrt(side^2 - (base/2)^2)
        double half = base / 2.0;
        double h2 = side * side - half * half;
        double h = (h2 <= 0) ? 0 : Math.sqrt(h2);
        return 0.5 * base * h;
    }
}

class EquilateralTriangle extends Triangle {
    private double side;

    public EquilateralTriangle(double side) {
        this.side = side;
    }

    @Override
    public double perimeter() {
        return 3 * side;
    }

    @Override
    public double area() {
        return (Math.sqrt(3) / 4) * side * side;
    }
}

// ------------------ Quadrilateral and rectangles ------------------
class Quadrilateral implements Polygon {
    // Base class for 4-sided shapes. Generic area/perimeter: subclasses override.
    @Override
    public double area() { return 0; }
    @Override
    public double perimeter() { return 0; }
}

class Rectangle extends Quadrilateral {
    protected double length, width;

    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    public double area() { return length * width; }

    @Override
    public double perimeter() { return 2 * (length + width); }
}

class Square extends Rectangle {
    public Square(double side) {
        super(side, side);
    }
}

// ------------------ Regular polygons: Pentagon, Hexagon, Octagon ------------------
abstract class RegularPolygon implements Polygon {
    protected int n;      // number of sides
    protected double s;   // side length

    public RegularPolygon(int n, double s) {
        this.n = n;
        this.s = s;
    }

    @Override
    public double perimeter() {
        return n * s;
    }

    @Override
    public double area() {
        // area = (n * s^2) / (4 * tan(pi/n))
        return (n * s * s) / (4.0 * Math.tan(Math.PI / n));
    }
}

class Pentagon extends RegularPolygon {
    public Pentagon(double side) { super(5, side); }
}

class Hexagon extends RegularPolygon {
    public Hexagon(double side) { super(6, side); }
}

class Octagon extends RegularPolygon {
    public Octagon(double side) { super(8, side); }
}

// ------------------ Console UI ------------------
public class PolygonCalculator {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Polygon Calculator — create shapes and get area & perimeter");
        boolean running = true;
        while (running) {
            System.out.println("\nChoose a polygon to create:");
            System.out.println("1) General Triangle (3 sides)");
            System.out.println("2) Isosceles Triangle");
            System.out.println("3) Equilateral Triangle");
            System.out.println("4) Rectangle");
            System.out.println("5) Square");
            System.out.println("6) Pentagon (regular)");
            System.out.println("7) Hexagon (regular)");
            System.out.println("8) Octagon (regular)");
            System.out.println("9) Exit");
            System.out.print("Enter choice: ");
            int choice = readInt();

            Polygon p = null;
            switch (choice) {
                case 1:
                    System.out.print("Enter side a: "); double a = readDouble();
                    System.out.print("Enter side b: "); double b = readDouble();
                    System.out.print("Enter side c: "); double c = readDouble();
                    p = new GeneralTriangle(a, b, c);
                    break;
                case 2:
                    System.out.print("Enter base: "); double base = readDouble();
                    System.out.print("Enter equal side length: "); double side = readDouble();
                    p = new IsoscelesTriangle(base, side);
                    break;
                case 3:
                    System.out.print("Enter side length: "); double s = readDouble();
                    p = new EquilateralTriangle(s);
                    break;
                case 4:
                    System.out.print("Enter length: "); double length = readDouble();
                    System.out.print("Enter width: "); double width = readDouble();
                    p = new Rectangle(length, width);
                    break;
                case 5:
                    System.out.print("Enter side: "); double sideSq = readDouble();
                    p = new Square(sideSq);
                    break;
                case 6:
                    System.out.print("Enter side length: "); double pentSide = readDouble();
                    p = new Pentagon(pentSide);
                    break;
                case 7:
                    System.out.print("Enter side length: "); double hexSide = readDouble();
                    p = new Hexagon(hexSide);
                    break;
                case 8:
                    System.out.print("Enter side length: "); double octSide = readDouble();
                    p = new Octagon(octSide);
                    break;
                case 9:
                    running = false;
                    continue;
                default:
                    System.out.println("Invalid choice.");
                    continue;
            }

            // Output results
            System.out.printf("Perimeter: %.4f%n", p.perimeter());
            System.out.printf("Area: %.4f%n", p.area());
        }
        System.out.println("Goodbye!");
    }

    private static int readInt() {
        while (!scanner.hasNextInt()) {
            scanner.next(); System.out.print("Please enter an integer: ");
        }
        return scanner.nextInt();
    }

    private static double readDouble() {
        while (!scanner.hasNextDouble()) {
            scanner.next(); System.out.print("Please enter a number: ");
        }
        return scanner.nextDouble();
    }
}
