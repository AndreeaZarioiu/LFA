import java.math.BigInteger;
import java.util.ArrayList;

public class StackOfOperations {
    private ArrayList<Instruction> ops;
    private int opened = 0;
    private int lastExecuted = -1;
    private BigInteger iterations = new BigInteger("0");
    public StackOfOperations(){
        ops = new ArrayList<Instruction>();
    }
    public void addOperation(Instruction i){
       ops.add(i);
       if(i == Instruction.LBRACE){
           opened++;
       }
        if(i == Instruction.RBRACE){
            opened--;
        }
    }
    public boolean inLoop(){
        return opened != 0;
    }
    public Instruction getOperation(){

        if(lastExecuted == -1 || ops.get(lastExecuted) == Instruction.RBRACE){
            lastExecuted = ops.indexOf(Instruction.LBRACE);
        }
        lastExecuted++;
        if(ops.get(lastExecuted) == Instruction.RBRACE) {
            lastExecuted = -1;
            return Instruction.END;
        }
        return ops.get(lastExecuted);
    }
    public void setItertions(BigInteger n){
        iterations = n;

    }
    public BigInteger getItertions(){
        return iterations;
    }
    public void cleanStack(){
        ops.clear();
        lastExecuted = -1;
        iterations = new BigInteger("0");
    }
}
