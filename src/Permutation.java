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

    private Integer[] getCrossPoints() {
        int crossPoint1;
        int crossPoint2;
        do {
            crossPoint1 = Helper.randomInt(0, this.tasks.size() - 1);
            crossPoint2 = Helper.randomInt(0, this.tasks.size() - 1);
        } while (crossPoint2 < crossPoint1);

        return new Integer[]{crossPoint1, crossPoint2};
    }

    private void fillTasksWithNull(int size) {
        for (int i = 0; i < size; i++) {
            this.tasks.add(null);
        }
    }

    public static Permutation[] crossword(Permutation parent1, Permutation parent2) {
        Permutation[] children = new Permutation[2];
        children[0] = new Permutation();
        children[0].fillTasksWithNull(parent1.tasks.size());
        children[1] = new Permutation();
        children[1].fillTasksWithNull(parent1.tasks.size());
        Integer[] crossPoints = parent1.getCrossPoints();

        for (int i = crossPoints[0]; i < crossPoints[1]; i++) {
            children[0].tasks.set(i, parent2.tasks.get(i));
            children[1].tasks.set(i, parent1.tasks.get(i));
        }

        for (int i = 0; i < crossPoints[0]; i++) {
            Task parent1Task = parent1.tasks.get(i);
            Task parent2Task = parent2.tasks.get(i);
            if (!children[0].tasks.contains(parent1Task)) {
                children[0].tasks.set(i, parent1Task);
            }

            if (!children[1].tasks.contains(parent2Task)) {
                children[1].tasks.set(i, parent2Task);
            }
        }

        for (int i = crossPoints[1]; i < parent1.tasks.size(); i++) {
            Task parent1Task = parent1.tasks.get(i);
            Task parent2Task = parent2.tasks.get(i);
            if (!children[0].tasks.contains(parent1Task)) {
                children[0].tasks.set(i, parent1Task);
            }

            if (!children[1].tasks.contains(parent2Task)) {
                children[1].tasks.set(i, parent2Task);
            }
        }

        for (int i = 0; i < parent1.tasks.size(); i++) {
            if (children[0].tasks.get(i) == null) {
                for (int j = 0; j < parent2.tasks.size(); j++) {
                    Task parent2Task = parent2.tasks.get(j);
                    if (!children[0].tasks.contains(parent2Task)) {
                        children[0].tasks.set(i, parent2Task);
                        break;
                    }
                }
            }

            if (children[1].tasks.get(i) == null) {
                for (int j = 0; j < parent1.tasks.size(); j++) {
                    Task parent1Task = parent1.tasks.get(j);
                    if (!children[1].tasks.contains(parent1Task)) {
                        children[1].tasks.set(i, parent1Task);
                        break;
                    }
                }
            }
        }
        children[0].countDelay();
        children[1].countDelay();
        return children;
    }
}
