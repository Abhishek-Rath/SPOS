//Abhishek Rath
//TECOC313

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;

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
    String actArg;

    ALA(String f) {
        this.formArg = f;
    }
}

class Macro {
    String MDT[] = new String[50];
    ALA ala[] = new ALA[50];
    MNT mnt[] = new MNT[50];  // HashMap<String, integer>mnt = new HashMap <String, Integer>();
    int mntp;
    int mdtp;
    int alap;

    Macro() {
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
                String tok[] = br.readLine().split("\t"); //macro name statement
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
                    MDT[mdtp] = new String(instr);  //store macro contents in MDT
                    mdtp++;
                }
                MDT[mdtp] = new String("MEND");  //Add mend statment at the end of macro in MDT
                mdtp++;
            }

            //Statement is not macro
            else{
                fw.write(st+"\n");
            }
        }
        fw.close();
        br.close();
    }// end of pass 1


    int searchMNT(String key) {
        for(int i=0;i<mntp; i++) {
            if(mnt[i].macroName.equals(key)) {
                return mnt[i].MDTP;
            }
        }
        return -1;
    }

    
    void MPass2(BufferedReader br) throws Exception {
        String st = "";
        int k;
        String arr[];
        
        FileWriter fw = new FileWriter("mac2Output.txt");

        while((st = br.readLine()) != null) {
            String token[] = st.trim().split("\t"); //pass1 op
            int j = searchMNT(token[0]);
            if(j != -1) {
                int mdtp = mnt[j].MDTP;
                arr = MDT[mdtp].trim().split(" "); //Read Line from MDT, this will be a prototype statement

                if(token.length != arr.length) {
                    System.out.println("Insufficient arguments");
                    break;
                }
                
                for(int i=1; i<arr.length;i++){
                    //Replace formal arguments with actual arguments 
                    k = Integer.parseInt(arr[i].substring(1));
                    ala[k].formArg = token[i];
                }
                while(true) {
                    mdtp++;
                    arr = MDT[mdtp].trim().split(" ");  //REad Line from MDT
                    String repString = "";
                    if(!arr[0].equals("MEND")) {
                        for(int i=0;i<arr.length;i++) {
                            if(arr[i].charAt(0) == '#') {
                                k = Integer.parseInt(arr[i].substring(1));
                                repString += ala[k].actArg;
                                repString += " ";
                            }
                            else {
                                repString += arr[i];
                                repString +=" ";
                            }
                        }

                        System.out.println(repString);
                        fw.write(repString+"\n");
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                System.out.println(st);
                fw.write(st+"\n");
            }
        }
        fw.close();
    }
 
   public static void main(String args[]) throws Exception{
        Macro obj = new Macro();
        File f = new File("mac1Output.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        System.out.println("*************\tPass 1\t*************");
        obj.MPass1();
        obj.printTables();
        System.out.println("***************************\tPass 2\t*****************************");
        obj.MPass2(br);
        br.close();
    }

}




/* 
    Input for Pass 1:

    MACRO
    INCR	&ARG1	&ARG2	&ARG3
    ADD	AREG	&ARG1
    ADD	BREG	&ARG2
    ADD	CREG	&ARG3
    MEND
    START
    MOVER	AREG	ONE
    SUB	BREG	ONE
    INCR	DATA1	DATA2	DATA3
    ONE	DC	1
    DATA1	DC	2
    DATA2	DC	3
    DATA3 	DC 	4
    END 

    Macro name table:
    Index	Macro Name	MDT Index
    0	INCR	0

    MAcro definition table
    0	INCR #0 #1 #2
    1    ADD AREG #0 
    2    ADD BREG #1 
    3    ADD CREG #2 
    4    MEND


    Argument List Array
    FormalArgs
    &ARG1
    &ARG2
    &ARG3


    Pass 1 Output
    START
    MOVER	AREG	ONE
    SUB	BREG	ONE
    INCR	DATA1	DATA2	DATA3
    ONE	DC	1
    DATA1	DC	2
    DATA2	DC	3
    DATA3 	DC 	4
    END


    Pass 2 output
    START
    MOVER	AREG	ONE
    SUB	BREG	ONE
    ADD AREG DATA1 
    ADD BREG DATA2 
    ADD CREG DATA3 
    ONE	DC	1
    DATA1	DC	2
    DATA2	DC	3
    DATA3 	DC 	4
    END
*/


