import java.util.ArrayList;
import java.util.Random;


public class Permutation {
    ArrayList<Task> tasks = new ArrayList<Task>();

    int delay = 0;

    public Permutation() {
    }

    public Permutation(ArrayList list, int del) {
        tasks = list;
        delay = del;
    }


    public void addTask(Task Obj) {
        tasks.add(Obj);
    }

    public void countDelay() {
        int donetime = 0;

        Task tmp;
        for (int i = 0; i < tasks.size(); i++) {
            tmp = tasks.get(i).copy();
            donetime += tmp.getNeededTime();
            this.delay += (donetime - tmp.getNeededTime());
        }
        this.delay = this.delay / tasks.size();
    }

    public void clear() {
        this.tasks.clear();
    }

    public Permutation copy() {
        ArrayList<Task> input = new ArrayList<Task>(this.tasks);
        int del = this.delay;
        Permutation cop = new Permutation(input, del);
        return cop;
    }

    public void print() {
        Task tmp;
        System.out.println("--------------------------------------------------------------------");
        for (int i = 0; i < tasks.size(); i++) {
            tmp = tasks.get(i);
            tmp.print();
        }
        System.out.println("delay = " + this.delay);
        System.out.println("--------------------------------------------------------------------");

    }

    //zamienia ze soba miejscami taski w permutacji (na podstawie prawdopodobienstwa)
    public void mutate() {
        double prob = 0.2;
        Random r = new Random();
        Task tmp;
        int pos;
        for (int i = 0; i < tasks.size(); i++) {
            if (r.nextDouble() < prob) {
                tmp = tasks.get(i).copy();
                pos = r.nextInt(tasks.size());
                while (pos != i) pos = r.nextInt(tasks.size());
                tasks.set(i, tasks.get(pos).copy());
                tasks.set(pos, tmp);
            }
        }
    }

    public static Permutation crossword(Permutation parent1, Permutation parent2) {
        Permutation child = new Permutation();
        int crossPoint1;
        int crossPoint2;
        do {
            crossPoint1 = Helper.randomInt(0, parent1.tasks.size() - 1);
            crossPoint2 = Helper.randomInt(0, parent2.tasks.size() - 1);
        } while (crossPoint2 < crossPoint1);

        for (int i = 0; i < crossPoint1; i++) {
            child.tasks.add(parent1.tasks.get(i));
        }

        for (int i = crossPoint1; i < crossPoint2; i++) {
            child.tasks.add(parent2.tasks.get(i));
        }

        for (int i = crossPoint2; i < parent1.tasks.size(); i++) {
            child.tasks.add(parent1.tasks.get(i));
        }

        return child;
    }

    public ArrayList<Integer> repetitionIds() {
        ArrayList<Integer> repetitions = new ArrayList<>();
        int size = this.tasks.size();
        for (int i = 0; i < size; i++) {
            Task current = this.tasks.get(i);
            for (int j = i; j < size; j++) {
                if (current == this.tasks.get(j) && j != i)
                    repetitions.add(i);
            }
        }
        return repetitions;
    }
}
