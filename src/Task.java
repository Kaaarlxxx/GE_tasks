public class Task {
    int id;
    int arrTime;
    int neededTime;
    int reqTime;
    public Task(){}

    public Task(int ID, int RI, int PI, int DI){
        this.id = ID;
        this.arrTime = RI;
        this.neededTime = PI;
        this.reqTime = DI;
    }
    public void set(int ID, int RI, int PI, int DI){
        this.id = ID;
        this.arrTime = RI;
        this.neededTime = PI;
        this.reqTime = DI;
    }

    public int getID(){
        return this.id;
    }
    public int getNeededTime(){
        return this.neededTime;
    }
    public int getReqTime(){
        return this.reqTime;
    }
    public int getArrTime(){
        return this.arrTime;
    }

    public void print(){
        System.out.println(this.id+"    "+this.arrTime+"    "+this.neededTime+"    "+this.reqTime);
    }
    public Task copy(){
        Task tmp = new Task(this.id,this.arrTime,this.neededTime,this.reqTime);
        return tmp;
    }
}
