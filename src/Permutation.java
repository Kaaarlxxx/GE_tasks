import java.util.ArrayList;
import java.util.Random;


public class Permutation {
    ArrayList<Task> tasks = new ArrayList<Task>();

    int delay = 0;

    public Permutation(){}

    public Permutation(ArrayList list,int del){
        tasks = list;
        delay = del;
    }


    public void addTask(Task Obj) {
        tasks.add(Obj);
    }

    public void countDelay(){
        int donetime=0;

        Task tmp;
        for(int i = 0; i < tasks.size(); i++){
            tmp = tasks.get(i).copy();
            donetime+=tmp.getNeededTime();
            this.delay +=(donetime-tmp.getNeededTime());
        }
        this.delay = this.delay/tasks.size();
    }

    public void clear(){
        this.tasks.clear();
    }

    public Permutation copy(){
        ArrayList<Task> input = new ArrayList<Task>(this.tasks);
        int del = this.delay;
        Permutation cop = new Permutation(input,del);
        return cop;
    }
    public void print(){
        Task tmp;
        System.out.println("--------------------------------------------------------------------");
        for(int i = 0; i < tasks.size(); i++){
            tmp = tasks.get(i);
            tmp.print();
        }
        System.out.println("delay = " +this.delay );
        System.out.println("--------------------------------------------------------------------");

    }
    //zamienia ze soba miejscami taski w permutacji (na podstawie prawdopodobienstwa)
    public void mutate(){
        double prob = 0.2;
        Random r = new Random();
        Task tmp;
        int pos;
        for(int i = 0; i < tasks.size(); i++){
            if(r.nextDouble()<prob){
                tmp = tasks.get(i).copy();
                pos = r.nextInt(50);
                while(pos!=i) pos = r.nextInt(50);
                tasks.set(i,tasks.get(pos).copy());
                tasks.set(pos,tmp);
            }
        }
    }





    //PMX todo
}
