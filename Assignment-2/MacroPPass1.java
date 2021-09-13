import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
// import java.io.FileNotFoundException;

class MNT {
    String macroName;
    int MDTP;
    MNT(String s, int k) {
        this.macroName = s;
        this.MDTP = k;
    }
}

class ALA {
    String formArg;

    ALA(String f) {
        this.formArg = f;
    }
}

class MacroPPass1 {
    String MDT[] = new String[50];
    ALA ala[] = new ALA[50];
    MNT mnt[] = new MNT[50];  // HashMap<String, integer>mnt = new HashMap <String, Integer>();
    int mntp;
    int mdtp;
    int alap;

    MacroPPass1() {
        //Initialize pointers
        mntp = 0;
        mdtp = 0;
        alap = 0;
    }
    
    int searchALA(String s, int start) {
        for(int i=start; i<alap; i++) {
            if(ala[i].formArg.equals(s))
                return i;
        }
        return -1;
    }

    void printTables() throws Exception {
        FileWriter fw = new FileWriter("MNT.txt");
        System.out.println("\n***********\tMNT\t***********");
        System.out.println("Index\tMacro Name\tMDT index");
        fw.write("Index\tMacro Name\tMDT Index\n");
        for(int i=0;i<mntp;i++) {
            System.out.println(i+"\t"+mnt[i].macroName+"\t"+mnt[i].MDTP);
            fw.write(i+"\t"+mnt[i].macroName+"\t"+mnt[i].MDTP+"\n");
        }
        fw.close();  //Close MNT file

        fw = new FileWriter("MDT.txt");
        System.out.println("\n***********\tMDT\t***********");
        for(int i=0;i<mdtp;i++) {
            System.out.println(i+"\t"+MDT[i]);
            fw.write(i+"\t"+MDT[i]+"\n");
        }
        fw.close(); //close mdt file

        fw = new FileWriter("ALA.txt");
        System.out.println("\n***********\tALA\t***********");
        System.out.println("FormalArgs");
        fw.write("FormalArgs\n");
        for(int i=0;i<alap;i++) {
            System.out.println(ala[i].formArg+"\t");
            fw.write(ala[i].formArg+"\n");
        }
        fw.close();
    }

    void MPass1() throws Exception {
        String st = "";
        String macStr = "";
        File f = new File("mac1Input.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        FileWriter fw = new FileWriter("mac1Output.txt");

        while((st = br.readLine()) != null) {
            String token[] = st.split("\t"); 

            if(token[0].equals("MACRO")) {
                String tok[] = br.readLine().split(" "); //macro name statement
                String instr = tok[0];
                        
                //If there any formal arguments store them in ALA table
                int start = alap;
                for(int i=1;i<tok.length;i++) {
                    ala[alap] = new ALA(tok[i]);
                    instr = instr.concat(" #"+alap);
                    alap++;
                }
                
                mnt[mntp] = new MNT(tok[0], mdtp); //Store macro name in MNT table
                mntp++;
                MDT[mdtp] = new String(instr);  //Store macro definition in MDT table
                mdtp++;
                
                while(!(macStr = br.readLine()).equals("MEND")) {
                    tok = macStr.split("\t");
                    instr = "";
                    for(int i=0; i<tok.length;i++) {
                        //check if next token is formal argument
                        if(tok[i].charAt(0) == '&') {
                            int p = searchALA(tok[i], start);
                            if(p != -1)
                                instr = instr.concat("#"+p+" ");
                            else    
                                System.out.println("Unknown argument");                                
                        }

                        else {
                            instr += tok[i]+" ";
                        }
                    }
                    MDT[mdtp] = new String(instr);
                    mdtp++;
                }
                MDT[mdtp] = new String("MEND");
                mdtp++;
            }

            //STatement is not macro
            else{
                fw.write(st+"\n");
            }
        }
        fw.close();
        br.close();
    }
 
   public static void main(String args[]) throws Exception{
        MacroPPass1 obj = new MacroPPass1();
        obj.MPass1();
        obj.printTables();
    }

}
