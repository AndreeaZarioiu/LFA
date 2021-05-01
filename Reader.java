import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

public class Reader {
    private ArrayList<Instruction> ops = new ArrayList<>();
    private Hashtable<String, Instruction> enumMap = new Hashtable();
    private String fileName;
    private int base;
    public int error;
    private Scanner scanner;
    private String line = "";
    private int opened = 0;
    public Reader(String fileName, int base) {
        this.fileName = fileName;
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.base = base;
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
    public boolean checkForErrors(){
        Scanner s = null;
        try {
            s = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(line.isEmpty()){
            line = s.nextLine();
            if(line.length() % 4 != 0) {
                error = line.length()/4;
                return true;
            }
        }
        int num = 0;
        while(!line.isEmpty()){
            String op = "";
            Map<Character, Integer> values = new Hashtable();
            int count = 0;
            int toRemove = 0;
            for(char c : line.toCharArray()) {
                if(op.length() == 4) break;
                toRemove++;
                if(c < 33 || c > 126) continue;
                if(!values.containsKey(Character.valueOf(c))) {
                    values.put(Character.valueOf(c), count);
                    op += count;
                    count++;
                } else {
                    op += values.get(Character.valueOf(c));
                }
            }

            line = line.substring(toRemove);
            ops.add(enumMap.getOrDefault(op, Instruction.ERROR));
            if(enumMap.getOrDefault(op, Instruction.ERROR) ==Instruction.LBRACE){
                opened++;
            }
            if(enumMap.getOrDefault(op, Instruction.ERROR) ==Instruction.RBRACE){
                opened--;
            }
            if(opened < 0) {
                error = num;
                return true;
            }
            num++;
        }
        if(opened > 0) {
            error = num;
            return true;
        }

        return false;
    }
    public Instruction getInstruction(){
      Instruction op = ops.get(0);
      ops.remove(0);
      return op;
    }
    public boolean isNotEmpty(){
        return (!ops.isEmpty());
    }
}
