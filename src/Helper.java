import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Helper {

    public static void printTable(String tab[]) {

        for (int i = 0; i < tab.length; i++) {
            System.out.print(tab[i] + "   ");
        }

        System.out.println();
    }

    public static ArrayList reverseProb(ArrayList<Double> probs, int population_size) {
        ArrayList<Double> newProbs = new ArrayList<Double>();
        for (int i = 0; i < population_size; i++) {
            newProbs.add((1 - probs.get(i)) / (population_size - 1));
        }
        return newProbs;
    }

    public static ArrayList readTasksFromFile(String fileName) {
        String[] val;
        Task tmp = new Task();
        ArrayList<Task> input = new ArrayList<Task>();
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            int count = 0;

            //odczytywanie do plików i wpisywanie Tasków do listy
            while ((line = br.readLine()) != null) {
                if (count != 0) {

                    val = line.split("\t");
                    tmp.set(Integer.parseInt(val[0]), Integer.parseInt(val[1]), Integer.parseInt(val[2]), Integer.parseInt(val[3]));
                    input.add(count - 1, tmp.copy());
                }
                count++;
            }
            br.close();
        } catch (IOException e) {
        }
        return input;
    }

    public static int posRoulette(ArrayList<Double> probs, int population_size) {
        Random r = new Random();
        double posib = 0;
        int pos = 0;
        double rand = r.nextDouble();
        for (int i = 0; i < population_size; i++) {
            posib += probs.get(i);
            if (rand <= posib) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public static ArrayList countProbs(ArrayList<Permutation> Population, int pop_size) {
        ArrayList<Double> newProbs = new ArrayList<Double>();
        double sum = 0;
        int i = 0;
        while (i < pop_size) sum += Population.get(i++).delay;
        for (int j = 0; j < pop_size; j++) {
            newProbs.add(Population.get(j).delay / sum);
        }

        return Helper.reverseProb(newProbs, pop_size);
    }

    public static double randomDouble(double min, double max) {
        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
    }

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max + 1) + min;
    }

    // CountDelay4All todo

}



