//Abhishek Rath TECOC313

import java.util.Scanner;

class Process {
    int AT, WT, FT;   // Arrival Time, Waiting Time, Finished Time
    int BT;  // Burst Time
    int RBT; // Remaining Burst time
    int TAT; // Turn Around Time
    int pr; // Priority
    int flag;
    String pName;  // Process Name

    Process(int ar, int burst) {
        AT = ar;
        BT = burst;
        RBT = BT;
        flag = 0;
    }

    Process(int ar, int burst, int p) {
        AT = ar;
        BT = burst;
        RBT = BT;
        pr = p;
        flag = 0;
    }
}

public class CPUScheduling {
    Process[] pro;
    int n;

    int gIndex;   // Gant chart Index
    int GT[] = new int[100];  // Gant for process
    int GT_Time[] = new int[100];  // To indicate time
    Scanner s;
    float avg_wt = 0;
    float avg_tat = 0;

    CPUScheduling() {
        s = new Scanner(System.in);
        System.out.println("Enter the number of processes: ");
        n = s.nextInt();
        pro = new Process[n];

        for (int i = 0; i < n; i++) {
            System.err.println("Enter Arival Time, Burst Time, Priority: ");
            int ar = s.nextInt();
            int bur = s.nextInt();
            int pr = s.nextInt();
            pro[i] = new Process(ar, bur, pr);
            pro[i].pName = "P" + i;
        }
    }

    void display() {
        System.out.println("P Name\tArrival Time\tBurst Time\tFinish Time\tTAT\tTWT");
        System.out.println("------------------------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.println(pro[i].pName + "\t\t" + pro[i].AT + "\t\t" + pro[i].BT + "\t\t" + pro[i].FT + "\t"
                    + pro[i].TAT + "\t" + pro[i].WT);
        }
    }

    void gantt_display(int total_time) {
        System.out.println("--------------------------------------------\n");
        for (int i = 0; i < gIndex; i++) {
            System.out.println("| " + pro[GT[i]].pName + " ");
        }
        System.out.println("|");
        System.out.println("--------------------------------------------\n");
        for (int i = 0; i < gIndex; i++) {
            System.out.println(GT_Time[i] + "  ");
        }
        System.out.println(total_time);
    }

    void FCFS() {
        Process temp;
        gIndex = 0;

        // Sort according to arrival time
        // i.e The process which came first will get served first
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n - i - 1; j++) {
                if (pro[j].AT > pro[j + 1].AT) {
                    temp = pro[j];
                    pro[j] = pro[j + 1];
                    pro[j + 1] = temp;
                }
            }
        // Scheduling
        int x = pro[0].AT;  // get arrival time for 1st process
        avg_wt = 0;
        avg_tat = 0;

        for (int i = 0; i < n; i++) {
            GT_Time[gIndex] = x;
            GT[gIndex] = i;
            gIndex++;
            x = x + pro[i].BT;  // Arrival time + burst time = completion time
            pro[i].FT = x;
            pro[i].TAT = pro[i].FT - pro[i].AT;
            pro[i].WT = pro[i].TAT - pro[i].BT;
            avg_wt += pro[i].WT;
            avg_tat += pro[i].TAT;
        }
        display();
        System.out.println("Total WT: " + avg_wt + "ms" + "\nAverage Waiting Time: " + avg_wt / n + "ms");
        System.out.println("Total TAT: " + avg_tat + "ms" + "\nAverage Turnaround Time: " + avg_tat / n + "ms");
        gantt_display(x);
    }

    void SJF_P() {
        gIndex = 0;

        int ct = 0;   // current time
        int min = 999;
        int ind = -1;
        int fg = 0;
        avg_wt = 0;
        avg_tat = 0;
        int total_completed_process = 0;
        while (total_completed_process < n) {
            min = 10000;
            ind = -1;
            for (int i = 0; i < n; i++) {

                // Find the process with minimum remianing time
                // among the processes that arrives till current time
                if (pro[i].AT <= ct && pro[i].RBT != 0 && min > pro[i].RBT) {
                    ind = i;
                    min = pro[i].RBT;
                    fg = 1;
                }
            }

            if (fg == 1) {
                pro[ind].RBT--;
                // Gantt Chart
                if (gIndex >= 0) {
                    if (GT[gIndex - 1] != ind) {
                        GT_Time[gIndex] = ct;
                        GT[gIndex] = ind;
                        gIndex++;
                    }
                } else {
                    GT_Time[gIndex] = ct;
                    GT[gIndex] = ind;
                    gIndex++;
                }
                ct++;

                // If process gets completely executed
                if (pro[ind].RBT == 0) {
                    total_completed_process++;
                    pro[ind].FT = ct;
                    pro[ind].TAT = pro[ind].FT - pro[ind].AT;
                    pro[ind].WT = pro[ind].TAT - pro[ind].BT;
                    avg_wt += pro[ind].WT;
                    avg_tat += pro[ind].TAT;
                }

            } else
                ct++;
        }
        display();
        System.out.println("Total WT: " + avg_wt + "ms" + "\nAverage Waiting Time: " + avg_wt / n + "ms");
        System.out.println("Total TAT: " + avg_tat + "ms" + "\nAverage Turnaround Time: " + avg_tat / n + "ms");
        gantt_display(ct);

    }

    void Priority_NP() {
        Process temp;
        gIndex = 0;

        int min = 999;
        int ind;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n - i - 1; j++) {
                if (pro[j + 1].AT <= pro[j].AT && pro[j].pr > pro[j + 1].pr) {
                    temp = pro[j];
                    pro[j] = pro[j + 1];
                    pro[j + 1] = temp;
                }
            }

        // Scheduling
        int x = pro[0].AT;
        avg_wt = 0;
        avg_tat = 0;
        GT_Time[gIndex] = x;
        GT[gIndex] = 0;
        gIndex++;
        x = x + pro[0].BT;
        pro[0].FT = x;
        pro[0].TAT = pro[0].FT - pro[0].AT;
        pro[0].WT = pro[0].TAT - pro[0].BT;
        avg_wt += pro[0].WT;
        avg_tat += pro[0].TAT;
        int total_completed_pro = 1;
        while (total_completed_pro < n) {
            min = 999;
            ind = 0;
            for (int i = 1; i < n; i++) {
                if (pro[i].AT <= x && pro[i].flag == 0 && min > pro[i].pr) {
                    min = pro[i].pr;
                    ind = i;
                }

            }
            GT_Time[gIndex] = x;
            GT[gIndex] = ind;
            gIndex++;
            pro[ind].flag = 1;
            x = x + pro[ind].BT;
            pro[ind].FT = x;
            pro[ind].TAT = pro[ind].FT - pro[ind].AT;
            pro[ind].WT = pro[ind].TAT - pro[ind].BT;
            avg_wt += pro[ind].WT;
            avg_tat += pro[ind].TAT;
            total_completed_pro++;
        }
        display();
        System.out.println("Total WT: " + avg_wt + "ms" + "\nAverage Waiting Time: " + avg_wt / n + "ms");
        System.out.println("Total TAT: " + avg_tat + "ms" + "\nAverage Turnaround Time: " + avg_tat / n + "ms");
        gantt_display(x);
    }

    void RR_P() {
        Process temp;
        gIndex = 0;

        System.out.println("Enter the Time Quantum: ");
        int tq = s.nextInt();
        for (int i = 0; i < n; i++) // Sort process as per Arrival Time
            for (int j = 0; j < n - i - 1; j++) {
                if (pro[j].AT > pro[j + 1].AT) {
                    temp = pro[j];
                    pro[j] = pro[j + 1];
                    pro[j + 1] = temp;
                }
            }
        // Scheduling
        int x = pro[0].AT;
        avg_wt = 0;
        avg_tat = 0;
        int total_completed_pro = 0;

        while (total_completed_pro < n) {
            for (int i = 0; i < n; i++) {
                if (pro[i].RBT > 0) {
                    GT_Time[gIndex] = x;
                    GT[gIndex] = i;
                    gIndex++;
                    if (pro[i].RBT < tq) {
                        x = x + pro[i].RBT;
                        pro[i].FT = x;
                        pro[i].RBT = 0;
                        pro[i].TAT = pro[i].FT - pro[i].AT;
                        pro[i].WT = pro[i].TAT - pro[i].BT;
                        avg_wt += pro[i].WT;
                        avg_tat += pro[i].TAT;
                        total_completed_pro++;

                    } else {
                        x = x + tq;
                        pro[i].RBT -= tq;
                        if (pro[i].RBT == 0) {
                            pro[i].FT = x;
                            pro[i].TAT = pro[i].FT - pro[i].AT;
                            pro[i].WT = pro[i].TAT - pro[i].BT;
                            avg_wt += pro[i].WT;
                            avg_tat += pro[i].TAT;
                            total_completed_pro++;
                        }
                    }
                }

            }

        }
        display();
        System.out.println("Total WT: " + avg_wt + "ms" + "\nAverage Waiting Time: " + avg_wt / n + "ms");
        System.out.println("Total TAT: " + avg_tat + "ms" + "\nAverage Turnaround Time: " + avg_tat / n + "ms");
        gantt_display(x);
    }

    void init() {
        for (int i = 0; i < n; i++) {
            pro[i].FT = 0;
            pro[i].RBT = pro[i].BT;
            pro[i].WT = 0;
        }
    }

    public static void main(String[] args) {
        CPUScheduling c = new CPUScheduling();
        Scanner s = new Scanner(System.in);

        try {

            int choice;
            do {
                System.out.println("1. FCFS");
                System.out.println("2. Priority");
                System.out.println("3. SJF-Preemptive");
                System.out.println("4. RR");
                System.out.println("5. Exit");
                System.out.println("Enter your choice");
                choice = s.nextInt();
                switch (choice) {
                    case 1:
                        c.FCFS();
                        break;
                    case 2:
                        c.Priority_NP();
                        break;
                    case 3:
                        c.SJF_P();
                        break;
                    case 4:
                        c.RR_P();
                        break;
                    case 5:
                        System.out.println("Exiting");
                        break;
                    default:
                        System.out.println("Invalid Choice");
                }
                c.init();
            } while (choice != 5);

        } finally {
            s.close();
        }

    }
}



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/* Enter the number of processes:
5
Enter Arival Time, Burst Time, Priority:
0
4
1
Enter Arival Time, Burst Time, Priority:
1
5
3
Enter Arival Time, Burst Time, Priority:
2
2
2
Enter Arival Time, Burst Time, Priority:
3
1
4
Enter Arival Time, Burst Time, Priority:
5
6
5
1. FCFS
2. Priority
3. SJF-Preemptive
4. RR
5. Exit
Enter your choice
1
P Name  Arrival Time    Burst Time      Finish Time     TAT     TWT
------------------------------------------------------
P0              0               4               4       4       0
P1              1               5               9       8       3
P2              2               2               11      9       7
P3              3               1               12      9       8
P4              5               6               18      13      7
Total WT: 25.0ms
Average Waiting Time: 5.0ms
Total TAT: 43.0ms
Average Turnaround Time: 8.6ms
--------------------------------------------------------------------------------------------
 P0
| P1
| P2
| P3
| P4
|
--------------------------------------------

0
4
9
11
12
18

Enter your choice
3
P Name  Arrival Time    Burst Time      Finish Time     TAT     TWT
------------------------------------------------------
P0              0               4               5       5       1
P1              1               5               13      12      7
P2              2               2               8       6       4
P3              3               1               6       3       2
P4              5               6               19      14      8
Total WT: 22.0ms
Average Waiting Time: 4.4ms
Total TAT: 40.0ms
Average Turnaround Time: 8.0ms
--------------------------------------------

| P0
| P3
| P2
| P1
| P4
|
--------------------------------------------

1
5
6
8
13
19
1. FCFS
2. Priority
3. SJF-Preemptive
4. RR

----------------------------------------------

2
P Name  Arrival Time    Burst Time      Finish Time     TAT     TWT
------------------------------------------------------
P0              0               4               4       4       0
P1              1               5               11      10      5
P2              2               2               6       4       2
P3              3               1               12      9       8
P4              5               6               18      13      7
Total WT: 22.0ms
Average Waiting Time: 4.4ms
Total TAT: 40.0ms
Average Turnaround Time: 8.0ms
--------------------------------------------

| P0
| P2
| P1
| P3
| P4
|
--------------------------------------------

0
4
6
11
12
18
1. FCFS
2. Priority
3. SJF-Preemptive
4. RR
5. Exit
Enter your choice
4
Enter the Time Quantum:
2
P Name  Arrival Time    Burst Time      Finish Time     TAT     TWT
------------------------------------------------------
P0              0               4               11      11      7
P1              1               5               16      15      10
P2              2               2               6       4       2
P3              3               1               7       4       3
P4              5               6               18      13      7
Total WT: 29.0ms
Average Waiting Time: 5.8ms
Total TAT: 47.0ms
Average Turnaround Time: 9.4ms
--------------------------------------------

| P0
| P1
| P2
| P3
| P4
| P0
| P1
| P4
| P1
| P4
|
--------------------------------------------

0
2
4
6
7
9
11
13
15
16
18
1. FCFS
2. Priority
3. SJF-Preemptive
4. RR
5. Exit
Enter your choice
5
Exiting

*/