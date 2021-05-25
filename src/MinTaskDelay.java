import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import static java.util.Collections.shuffle;


public class MinTaskDelay {
    private static final double PC = 0.6;

    public static void main(String[] args) {

        Random r = new Random();
        ArrayList<Task> input = Helper.readTasksFromFile("projekt8.txt");
        ArrayList<Double> probs = new ArrayList<Double>();
        ArrayList<Permutation> population = new ArrayList<>();
        ArrayList<Permutation> populationAfterRoulette = new ArrayList<>();
        int N = 200; //rozmiar populacji
        Permutation BestPerm;
        Permutation tmpBestPerm;

        PrintWriter highest = null;
        PrintWriter temp = null;

        try {
            highest = new PrintWriter(new FileOutputStream("highest.txt"));
            temp = new PrintWriter(new FileOutputStream("tmp.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        highest.println(BestPerm.delay);

        for (int i = 1; i < N; i++) {
            if (population.get(i).delay < BestPerm.delay) {
                BestPerm = population.get(i).copy();
            }
        }
        double probability;

        for (int ev = 0; ev < 1000; ev++) {

            //Mutowanie
            for (int i = 0; i < N; i++) {
                probability = r.nextDouble();
                if (probability < 0.2) {
                    population.get(i).mutate();
                }
            }

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

            for (int i = 0; i < N; i++) {
                populationAfterRoulette.add(population.get(Helper.posRoulette(probs, N)).copy());
            }
            population = (ArrayList<Permutation>) populationAfterRoulette.clone();
            populationAfterRoulette.clear();

            probs = Helper.countProbs(population, N);


//            population = Helper.steadyStateSelection(population, N);
//            population = Helper.countDelay4All(population, N);


            tmpBestPerm = population.get(0).copy();
            for (int i = 0; i < N; i++) {
                if (population.get(i).delay < tmpBestPerm.delay) {
                    tmpBestPerm = population.get(i).copy();
                }
            }

            if (tmpBestPerm.delay < BestPerm.delay) BestPerm = tmpBestPerm.copy();
            temp.println(tmpBestPerm.delay);
            highest.println(BestPerm.delay);
        }


        highest.close();
        temp.close();

        Helper.hr();
        for (int i = 0; i < N; i++) {
            System.out.println(population.get(i).delay);
        }
        Helper.hr();
    }
}
