import java.text.ParseException;
 
class FCFS {
 
    static void find_waiting_time(int processes[], int n, int bt[], int wt[]) {
        wt[0] = 0; 
        
        for (int i = 1; i < n; i++) {
            wt[i] = bt[i - 1] + wt[i - 1];
        }
    }
 
    
    static void find_turn_around_time(int processes[], int n,
            int bt[], int wt[], int tat[]) {
        // calculating turnaround time by adding
        // bt[i] + wt[i]
        for (int i = 0; i < n; i++) {
            tat[i] = bt[i] + wt[i];
        }
    }
 
    static void find_avg_time(int processes[], int n, int bt[]) {
        int wt[] = new int[n], tat[] = new int[n];
        int total_wt = 0, total_tat = 0;
 
        //Function to find waiting time of all processes
        find_waiting_time(processes, n, bt, wt);
 
        //Function to find turn around time for all processes
        find_turn_around_time(processes, n, bt, wt, tat);
 
        //Display processes along with all details
        System.out.printf("Processes Burst time Waiting"
                       +" time Turn around time\n");
 
        // Calculate total waiting time and total turn
        // around time
        for (int i = 0; i < n; i++) {
            total_wt = total_wt + wt[i];
            total_tat = total_tat + tat[i];
            System.out.printf(" %d ", (i + 1));
            System.out.printf("     %d ", bt[i]);
            System.out.printf("     %d", wt[i]);
            System.out.printf("     %d\n", tat[i]);
        }
        float s = (float)total_wt /(float) n;
        int t = total_tat / n;
        System.out.printf("Average waiting time = %f", s);
        System.out.printf("\n");
        System.out.printf("Average turn around time = %d ", t);
    }
 
    public static void main(String[] args) throws ParseException {
        int processes[] = {1, 2, 3};
        int n = processes.length;
 
        int burst_time[] = {10, 5, 8};
 
        find_avg_time(processes, n, burst_time);
 
    }
}