import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Helper {
    public static Random r = new Random();
    public static int tournamentSize = 2;

    public static void createFile(String filename) {
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }

    public static void printTable(String tab[]) {

        for (int i = 0; i < tab.length; i++) {
            System.out.print(tab[i] + "   ");
        }

        System.out.println();
    }

    public static void hr() {
        System.out.println("---------------------------------------------");
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
    public static ArrayList countDelay4All(ArrayList<Permutation> Population, int pop_size) {
        ArrayList<Permutation> newone = new ArrayList<>();
        Permutation tmpperm;

        for (int i = 0; i < pop_size; i++) {
            tmpperm = Population.get(i).copy();
            tmpperm.countDelay();
            newone.add(tmpperm);
        }
        return newone;
    }


    //wybierz losowych uczestnikow do turnieju o podanym rozmiarze
    //najlepszy(dajacy minimalny czas opoznienia) wygrywa i wchodzi na liste
    // powtarzaj turnieje aby otrzymac kompletna populacje

//    public static ArrayList<Permutation> tournament(ArrayList<Permutation> Population, int pop_Size) {
//
//        ArrayList<Permutation> Tournament = new ArrayList<>();
//        ArrayList<Permutation> AfterTournament = new ArrayList<Permutation>();
//        Permutation tmpperm;
//
//        for (int j = 0; j < pop_Size; j++) {
//            Tournament.clear();
//            for (int i = 0; i < tournamentSize; i++) {
//                tmpperm = Population.get(r.nextInt(pop_Size)).copy();
//                Tournament.add(tmpperm);
//
//                if (Tournament.get(0).delay < Tournament.get(1).delay) AfterTournament.add(Tournament.get(0).copy());
//
//                else AfterTournament.add(Tournament.get(1).copy());
//
//            }
//        }
//        //tutaj powinna byc krzyzowka wszystkich zwyciezcow
//        ArrayList<Permutation> winners = pmx(AfterTournament);
//        return winners;
//    }

    public static ArrayList steadyStateSelection(ArrayList<Permutation> Population, int pop_size) {
        Permutation[] table = new Permutation[pop_size];
        Permutation tmp = Population.get(0).copy();
        int pos = 0;
        Population.toArray(table);
        int toDestroy = Math.round(pop_size / 5);
        for (int i = 0; i < toDestroy; i++) {
            pos = 0;
            for (int j = 0; j < pop_size; j++) {
                if (Population.get(j).delay > tmp.delay) pos = j;
            }
            table[pos] = null;
        }

        pos = 0;

        for (int i = 0; i < pop_size; i++) {

            if (table[i] == null) {
                pos = r.nextInt(pop_size);
                while (table[pos] == null) pos = r.nextInt(pop_size);
                table[i] = table[pos].copy();
            }
        }

        ArrayList<Permutation> newone = new ArrayList<Permutation>(Arrays.asList(table));

        return newone;
    }

    public static void taskGenerator(int numberOfTasks) {
        String fileName = "projekt" + numberOfTasks + ".txt";
        createFile(fileName);
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("nr_zadania\tczas_przybycia\tczas_wykonania\twymagany_czas_zakonczenia\n");

            for (int i = 0; i < numberOfTasks; i++) {
                myWriter.write(i + "\t" + "0\t" + (r.nextInt(9) + 1) + "\t" + (r.nextInt(39) + 1) + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}



