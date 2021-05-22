
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import static java.util.Collections.shuffle;


public class MinTaskDelay {


    public static void main(String[] args) {

        Random r = new Random();
        ArrayList<Task> input = Helper.readTasksFromFile("projekt8.txt");
        ArrayList<Double> probs =new ArrayList<Double>();
        ArrayList<Permutation> Population = new ArrayList<>();
        ArrayList<Permutation> PopulationAfterRoulette = new ArrayList<>();
        int N = 200; //rozmiar populacji
        Permutation BestPerm;
        Permutation tmpBestPerm;

        PrintWriter highest = null;
        PrintWriter temp = null;

         try{
        highest = new PrintWriter(new FileOutputStream("highest.txt"));
        temp = new PrintWriter(new FileOutputStream("tmp.txt"));
        } catch (FileNotFoundException e) {
        e.printStackTrace();
         }


        //wypelnianie populacji taskami w losowej kolejnosci
        Permutation tmpperm = new Permutation(input,0);
        for (int i = 0; i < N; i++) {
            shuffle(tmpperm.tasks);
            Population.add(tmpperm.copy());
            Population.get(i).countDelay();
            System.out.println(Population.get(i).delay);
        }

        probs = Helper.countProbs(Population,N);


//       // just print for test
//          for (int i = 0; i < N; i++) {
//              tmpperm=Population.get(i);
//              tmpperm.print();
//        }


    // wybieranie najlepszej permutacji (z najmniejszym opoznieniem)
    BestPerm = Population.get(0).copy();
    highest.println(BestPerm.delay);

    for (int i = 1; i < N; i++) {
        if(Population.get(i).delay < BestPerm.delay){
            BestPerm = Population.get(i).copy();
        }
    }
    double probability;

    for (int ev = 0; ev < 1000; ev ++) {

        //Mutowanie
        for(int i = 0; i < N; i++) {
            probability = r.nextDouble();
            if(probability<0.2){
                Population.get(i).mutate();
            }
        }


        //PMX todo




        //ruletka
        for (int i = 0; i < N; i++) {
            PopulationAfterRoulette.add(Population.get(Helper.posRoulette(probs,N)).copy());
        }


        Population = (ArrayList<Permutation>) PopulationAfterRoulette.clone();
        PopulationAfterRoulette.clear();

        for (int i = 0; i < N; i++) {
            tmpperm = Population.get(i).copy();
            tmpperm.countDelay();
            Population.set(i,tmpperm);

        }


        probs = Helper.countProbs(Population,N);


        tmpBestPerm = Population.get(0).copy();
        for (int i = 0; i < N; i++) {
            if(Population.get(i).delay < tmpBestPerm.delay){
                tmpBestPerm = Population.get(i).copy();
            }
        }


        if(tmpBestPerm.delay<BestPerm.delay) BestPerm = tmpBestPerm.copy();
        temp.println(tmpBestPerm.delay);
        highest.println(BestPerm.delay);
    }


        highest.close();
        temp.close();
        BestPerm.print();

    }
}
