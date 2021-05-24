import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import static java.util.Collections.shuffle;


public class MinTaskDelay {


    public static void main(String[] args) {

        Random r = new Random();
        ArrayList<Task> input = Helper.readTasksFromFile("projekt8.txt");
        ArrayList<Double> probs = new ArrayList<Double>();
        ArrayList<Permutation> population = new ArrayList<>();
        ArrayList<Permutation> populationAfterRoulette = new ArrayList<>();
        int N = 200; //rozmiar populacji
        Permutation bestPerm;
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
            population.add(tmpperm.copy());
            population.get(i).countDelay();
            System.out.println(population.get(i).delay);
        }

        probs = Helper.countProbs(population, N);


//       // just print for test
//          for (int i = 0; i < N; i++) {
//              tmpperm=population.get(i);
//              tmpperm.print();
//        }


        // wybieranie najlepszej permutacji (z najmniejszym opoznieniem)
        bestPerm = population.get(0).copy();
        highest.println(bestPerm.delay);

        for (int i = 1; i < N; i++) {
            if (population.get(i).delay < bestPerm.delay) {
                bestPerm = population.get(i).copy();
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


            //PMX todo
            

            //ruletka
            for (int i = 0; i < N; i++) {
                populationAfterRoulette.add(population.get(Helper.posRoulette(probs, N)).copy());
            }


            population = (ArrayList<Permutation>) populationAfterRoulette.clone();
            populationAfterRoulette.clear();

            for (int i = 0; i < N; i++) {
                tmpperm = population.get(i).copy();
                tmpperm.countDelay();
                population.set(i, tmpperm);

            }


            probs = Helper.countProbs(population, N);


            tmpBestPerm = population.get(0).copy();
            for (int i = 0; i < N; i++) {
                if (population.get(i).delay < tmpBestPerm.delay) {
                    tmpBestPerm = population.get(i).copy();
                }
            }


            if (tmpBestPerm.delay < bestPerm.delay) bestPerm = tmpBestPerm.copy();
            temp.println(tmpBestPerm.delay);
            highest.println(bestPerm.delay);
        }


        highest.close();
        temp.close();
        bestPerm.print();

    }
}

//Check
