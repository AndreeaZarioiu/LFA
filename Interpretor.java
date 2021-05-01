import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;


public class Interpretor {
    private final static LinkedList<BigInteger> mem= new LinkedList();
    private StackOfOperations loop = new StackOfOperations();
    private Hashtable<String, Instruction> enumMap = new Hashtable();
    private BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));
    private boolean errorFound = false;
    public String errorType ="";
    public boolean onExecute = false;
    private int base = 10;
    public Interpretor(int b){
        this.base = b;
        enumMap.put("0000", Instruction.NOP);
        enumMap.put("0001", Instruction.INPUT);
        enumMap.put("0010", Instruction.ROT);
        enumMap.put("0011", Instruction.SWAP);
        enumMap.put("0012", Instruction.PUSH);
        enumMap.put("0100", Instruction.RROT);
        enumMap.put("0101", Instruction.DUP);
        enumMap.put("0102", Instruction.ADD);
        enumMap.put("0110", Instruction.LBRACE);
        enumMap.put("0111", Instruction.OUTPUT);
        enumMap.put("0112", Instruction.MULTIPLY);
        enumMap.put("0120", Instruction.EXECUTE);
        enumMap.put("0121", Instruction.NEGATE);
        enumMap.put("0122", Instruction.POP);
        enumMap.put("0123", Instruction.RBRACE);
    }
    public void executeInstruction(Instruction i)  {
        //  System.out.println(i);
        if(i == Instruction.RBRACE){
            if((loop.getItertions()).equals(new BigInteger("0"))) {
                loop.addOperation(i);
                loop.setItertions(mem.getFirst());
            }
        }
        if(loop.inLoop()){
            loop.addOperation(i);
            return;
        }
        if(i == Instruction.INPUT){
            String name = null;
            try {
                name = reader.readLine();
            } catch (IOException e) {
                errorFound = true;
                return;
            }
            if(name == null) {
                errorFound = true;
                return;
            }
            if(base == 10){
                try {
                    BigInteger d = new BigInteger(name);
                } catch (NumberFormatException nfe) {
                    errorFound = true;
                    return;
                }
                mem.addFirst(new BigInteger(name));
            } else {
                try {
                    BigInteger d = new BigInteger(name, base);
                } catch (NumberFormatException nfe) {
                    errorFound = true;
                    return;
                }
                BigInteger num = new BigInteger(name, base);
                mem.addFirst(new BigInteger(num.toString(10)));
            }

        } else if(i == Instruction.OUTPUT){
            if(mem.isEmpty()) {

                errorFound = true;
                errorType = "b";
                return;
            }
            if(base == 10){
                System.out.println(mem.getFirst());
            } else {
                System.out.println(((mem.getFirst()).toString(base)).toUpperCase());
            }

            mem.removeFirst();
        } else if(i == Instruction.ADD){
            if(mem.size() < 2) {
                errorFound = true;
                return;
            }
            BigInteger a = mem.getFirst();
            mem.removeFirst();
            BigInteger b = mem.getFirst();
            mem.removeFirst();
            mem.addFirst(a.add(b));
        } else if(i == Instruction.MULTIPLY){
            if(mem.size() < 2) {
                errorFound = true;
                return;
            }
            BigInteger a = mem.getFirst();
            mem.removeFirst();
            BigInteger b = mem.getFirst();
            mem.removeFirst();
            mem.addFirst(a.multiply(b));
        }else if(i == Instruction.ROT){
            if(mem.isEmpty()) {
                errorFound = true;
                return;
            }
            BigInteger num = mem.getFirst();
            mem.removeFirst();
            mem.addLast(num);
        } else if(i == Instruction.SWAP){
            if(mem.size() < 2) {
                errorFound = true;
                return;
            }
            BigInteger num1 = mem.getFirst();
            mem.removeFirst();
            BigInteger num2 = mem.getFirst();
            mem.removeFirst();
            mem.addFirst(num1);
            mem.addFirst(num2);
        } else if(i == Instruction.PUSH){
            mem.addFirst(new BigInteger("1"));
        } else if(i == Instruction.RROT){
            if(mem.isEmpty()) {
                errorFound = true;
                return;
            }
            BigInteger num = mem.getLast();
            mem.removeLast();
            mem.addFirst(num);
        }else if(i == Instruction.DUP){
            if(mem.isEmpty()) {
                errorFound = true;
                return;
            }
            BigInteger num = mem.getFirst();
            mem.addFirst(num);
        } else if(i == Instruction.NEGATE){
            if(mem.isEmpty()) {
                errorFound = true;
                return;
            }
            BigInteger num = mem.getFirst();
            mem.removeFirst();
            mem.addFirst(num.negate());
        } else if(i == Instruction.POP){
            if(mem.isEmpty()) {
                errorFound = true;
                errorType = "b";
                return;
            }
            mem.removeFirst();
        } else if(i == Instruction.EXECUTE){
            onExecute = true;
            if(mem.size() < 4) {
                errorFound = true;
                return;
            }
            BigInteger num1 = mem.getFirst();  mem.removeFirst();
            BigInteger num2 = mem.getFirst();  mem.removeFirst();
            BigInteger num3 = mem.getFirst();  mem.removeFirst();
            BigInteger num4 = mem.getFirst();  mem.removeFirst();
            String op = "";

            int count = 0;
            Map<BigInteger, Integer> values = new Hashtable();
            for(int j = 0; j < 4;j++) {
                BigInteger num = (j == 0) ? num1 : ((j == 1) ? num2 : ((j == 2) ? num3 : num4));
                if (!values.containsKey(num)) {
                    values.put(num, count);
                    op += count;
                    count++;
                } else {
                    op += values.get(num);
                }
            }
            if(enumMap.getOrDefault(op, Instruction.ERROR) == Instruction.LBRACE
                    || enumMap.getOrDefault(op, Instruction.ERROR) == Instruction.RBRACE ) {
                errorFound = true;
                return;
            }
            executeInstruction(enumMap.getOrDefault(op, Instruction.ERROR));
            if(!errorFound) onExecute = false;
        } else if(i == Instruction.LBRACE){
            if(mem.isEmpty()) {
                errorFound = true;
                return;
            }

            loop.cleanStack();
            loop.addOperation(i);

        }

        if((loop.getItertions()).compareTo(new BigInteger("0")) == 1){
            Instruction x = loop.getOperation();
            if(x == Instruction.END){
                if(mem.isEmpty()) {
                    errorFound = true;
                    if(loop.getOperation() == Instruction.OUTPUT
                            ||
                            loop.getOperation() == Instruction.POP) errorType = "c";
                    return;
                }

                loop.setItertions(mem.getFirst());
                if(!(mem.getFirst()).equals(new BigInteger("0"))) {

                    executeInstruction(loop.getOperation());}
            } else {
                executeInstruction(x);
            }

        } else {
            loop.cleanStack();
        }

    }
    public boolean foundError(){
        return errorFound;
    }
}
