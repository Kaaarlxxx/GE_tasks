import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static java.util.Collections.shuffle;


public class MinTaskDelay {
    private static final double PC = 0.6;

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        Random r = new Random();
        ArrayList input = Helper.readTasksFromFile("projekt8.txt");
        ArrayList probs = new ArrayList<Double>();
        ArrayList<Permutation> population = new ArrayList<>();
        ArrayList<Permutation> populationAfterRoulette = new ArrayList<>();
        int N = 5000; //rozmiar populacji
        Permutation BestPerm;
        Permutation tmpBestPerm;
        int selectionMethod = 0;
        PrintWriter highest = null;
        PrintWriter temp = null;

        System.out.println("Type selection method : 1.ruletka\n2.steadystate\n");
        selectionMethod = s.nextInt();
        if (selectionMethod == 1) {
            System.out.println("Roulette Selected");
            try {
                highest = new PrintWriter(new FileOutputStream("Results/Ruletka/highest" + N + ".txt"));
                temp = new PrintWriter(new FileOutputStream("Results/Ruletka/tmp" + N + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (selectionMethod == 2) {
            System.out.println("SteadyState Selected");
            try {
                highest = new PrintWriter(new FileOutputStream("Results/SteadyState/highest" + N + ".txt"));
                temp = new PrintWriter(new FileOutputStream("Results/SteadyState/tmp" + N + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        //wypelnianie populacji taskami w losowej kolejnosci
        Permutation tmpperm = new Permutation(input, 0);
        for (int i = 0; i < N; i++) {
            shuffle(tmpperm.tasks);
            tmpperm.countDelay();
            population.add(tmpperm.copy());
            //System.out.println(population.get(i).delay);
        }

        probs = Helper.countProbs(population, N);


        // wybieranie najlepszej permutacji (z najmniejszym opoznieniem)
        BestPerm = population.get(0).copy();
        assert highest != null;
        highest.println(BestPerm.delay);
        temp.println(BestPerm.delay);

        for (int i = 1; i < N; i++) {
            if (population.get(i).delay < BestPerm.delay) {
                BestPerm = population.get(i).copy();
            }
        }
        double probability;
        boolean found = false;

        for (int ev = 0; ev < 10000; ev++) {
            //Mutowanie
            for (int i = 0; i < N; i++) {
                probability = r.nextDouble();
                if (probability < 0.2) {
                    population.get(i).mutate();
                }
            }
            assert temp != null;
            // PMX
            for (int i = 0; i < N; i++) {
                double crosswordProbability = Helper.randomDouble(0, 1);
                if (crosswordProbability <= PC) {
                    int secondPermutation;
                    do {
                        secondPermutation = Helper.randomInt(0, population.size() - 1);
                    } while (secondPermutation == i);
                    Permutation currentParent = population.get(i);
                    Permutation secondParent = population.get(secondPermutation);
                    Permutation[] children = Permutation.crossword(currentParent, secondParent);
                    population.set(i, children[0]);
                    population.set(secondPermutation, children[1]);
                }
            }

            //ruletka
            if (selectionMethod == 1) {
                for (int i = 0; i < N; i++) {
                    populationAfterRoulette.add(population.get(Helper.posRoulette(probs, N)).copy());
                }
                population = (ArrayList<Permutation>) populationAfterRoulette.clone();
                populationAfterRoulette.clear();
            }
            probs = Helper.countProbs(population, N);
            if (selectionMethod == 2) {
                population = Helper.steadyStateSelection(population, N);
            }
            population = Helper.countDelay4All(population, N);

            tmpBestPerm = population.get(0).copy();
            for (int i = 0; i < N; i++) {
                if (population.get(i).delay < tmpBestPerm.delay) {
                    tmpBestPerm = population.get(i).copy();
                }
            }


            if (ev % 10 != 0) {
                if (tmpBestPerm.delay < BestPerm.delay) {
                    BestPerm = tmpBestPerm.copy();
                    found = true;
                }
            }

            if (ev % 10 == 0) {
                if (tmpBestPerm.delay < BestPerm.delay) BestPerm = tmpBestPerm.copy();
                if (found) temp.println(BestPerm.delay);
                else temp.println(tmpBestPerm.delay);
                highest.println(BestPerm.delay);
                found = false;
            }
        }

        highest.println("nr_zadania\tczas_przybycia\tczas_wykonania\twymagany_czas_zakonczenia");
        highest.println(BestPerm.toString());


        highest.close();
        temp.close();

        Helper.hr();
        for (int i = 0; i < N; i++) {
            System.out.println(population.get(i).delay);
        }
        Helper.hr();
    }
}
