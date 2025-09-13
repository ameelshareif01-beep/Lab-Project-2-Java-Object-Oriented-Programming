import java.util.Random;
import java.util.Scanner;

// ------------------ Animal hierarchy ------------------
abstract class Animal {
    // simple identity method
    public abstract String toString();
    // optional: clone factory to create same type
    public abstract Animal createOffspring();
}

class Bear extends Animal {
    @Override public String toString() { return "B"; }
    @Override public Animal createOffspring() { return new Bear(); }
}

class Fish extends Animal {
    @Override public String toString() { return "F"; }
    @Override public Animal createOffspring() { return new Fish(); }
}

// ------------------ Ecosystem ------------------
public class Ecosystem {
    private Animal[] river;
    private Random rand;
    private int size;

    public Ecosystem(int size, double fillProbability) {
        this.size = size;
        river = new Animal[size];
        rand = new Random();
        // initial populate
        for (int i = 0; i < size; i++) {
            double r = rand.nextDouble();
            if (r < fillProbability/2.0) river[i] = new Bear();
            else if (r < fillProbability) river[i] = new Fish();
            else river[i] = null;
        }
    }

    // Visualize the river
    public void visualize() {
        for (Animal a : river) {
            System.out.print((a == null ? "-" : a.toString()) + " ");
        }
        System.out.println();
    }

    // Run a single step using a next-state approach to avoid double moves
    public void runStep() {
        Animal[] next = new Animal[size];

        // First, iterate over current state and attempt moves
        for (int i = 0; i < size; i++) {
            Animal current = river[i];
            if (current == null) continue;

            // randomly decide movement: -1 left, 0 stay, +1 right
            int move = rand.nextInt(3) - 1;
            int target = i + move;

            // clamp to bounds
            if (target < 0) target = 0;
            if (target >= size) target = size - 1;

            // If next[target] is empty, place or handle collision logic if other animal tries later
            if (next[target] == null) {
                // if river[target] (current array) contains an animal and target != i,
                // we may handle collisions when both attempt same cell. We'll handle collisions below:
                next[target] = current;
            } else {
                // collision in next-state: next[target] already has an animal (from other mover)
                Animal existing = next[target];

                // If same species -> birth: place offspring in a random empty cell in next
                if (existing.getClass() == current.getClass()) {
                    // keep one in the target cell, and try to place an offspring somewhere empty
                    placeOffspringRandom(next, existing.createOffspring());
                } else {
                    // If different species and one is Bear and the other Fish -> bear eats fish.
                    // Keep the Bear in the cell (prefer bear). So if either is a Bear, keep Bear.
                    if (existing instanceof Bear || current instanceof Bear) {
                        next[target] = (existing instanceof Bear) ? existing : current;
                    } else {
                        // fallback: keep existing
                        // (shouldn't be needed but safe)
                    }
                }
            }
        }

        // Additionally, handle collisions that involve animals that stayed put and whose target cell
        // was previously occupied in the original river (two animals could end up in the same place
        // because one moved into where the other previously was). The above next-state logic already
        // accounts for multiple movers to same next cell. Now set river = next.
        river = next;
    }

    // Helper to place offspring into a random empty spot in the next array
    private void placeOffspringRandom(Animal[] next, Animal baby) {
        int start = rand.nextInt(size);
        for (int i = 0; i < size; i++) {
            int idx = (start + i) % size;
            if (next[idx] == null) {
                next[idx] = baby;
                return;
            }
        }
        // if full, do nothing (no space to place newborn)
    }

    // run multiple steps with visualization and short pause
    public void run(int steps, int delayMs) throws InterruptedException {
        System.out.println("Initial:");
        visualize();
        for (int s = 1; s <= steps; s++) {
            runStep();
            System.out.println("Step " + s + ":");
            visualize();
            if (delayMs > 0) Thread.sleep(delayMs);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("River size (e.g., 20): ");
        int size = sc.nextInt();
        System.out.print("Initial fill probability [0.0 - 1.0] (e.g., 0.6): ");
        double fill = sc.nextDouble();
        System.out.print("Steps to run: ");
        int steps = sc.nextInt();
        Ecosystem eco = new Ecosystem(size, fill);
        eco.run(steps, 300);
    }
}
