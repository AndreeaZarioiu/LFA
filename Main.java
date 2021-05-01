public class Main {
    public static void main(String[] args)  {
        String fileName = args[0];
        int base = 10;

        if(args.length > 1) base = Integer.parseInt(args[1]);
        Reader reader = new Reader(fileName, base);
        Interpretor interpretor = new Interpretor(base);
        Instruction instruction;
        int count = 0;
        // search for errors
        if(reader.checkForErrors()){
            System.err.print("Error:" + reader.error);
            System.err.print("\n");
            System.exit(-1);
            return;
        }
        while(reader.isNotEmpty()){
            // get instruction from reader
            instruction = reader.getInstruction();
            // execute instruction
            interpretor.executeInstruction(instruction);
            // stop program if error
            if(interpretor.foundError()){
                if(!interpretor.onExecute){
                    if(interpretor.errorType.equals("b")) count--;
                    if(interpretor.errorType.equals("c")) count-=2;
                }

                System.err.print("Exception:" + count);
                System.err.print("\n");
                System.exit(-2);
                return;
            }
            count++;
        }

    }
}
