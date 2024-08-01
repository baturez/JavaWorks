import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
public class Cpu_Time {
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 10, 3, 0));
        processes.add(new Process(2, 5, 1, 2));
        processes.add(new Process(3, 8, 2, 4));
        processes.add(new Process(4, 6, 6, 6));
        processes.add(new Process(5, 7, 4, 8));
        RoundRobinScheduler rrScheduler = new RoundRobinScheduler(4);
        processes.forEach(rrScheduler::addProcess);
        System.out.println("Round Robin Scheduling:");
        rrScheduler.execute();
        printMetrics(processes);
        resetProcesses(processes);
        PriorityScheduler priorityScheduler = new PriorityScheduler();
        processes.forEach(priorityScheduler::addProcess);
        System.out.println("\nPriority Scheduling:");
        priorityScheduler.execute();
        printMetrics(processes);
        resetProcesses(processes);
        MultiLevelQueueScheduler mlqScheduler = new MultiLevelQueueScheduler(4);
        processes.forEach(mlqScheduler::addProcess);
        System.out.println("\nMulti-Level Queue Scheduling:");
        mlqScheduler.execute();
        printMetrics(processes);
    }
    private static void printMetrics(List<Process> processes) {
        double totalTurnaroundTime = 0;
        double totalWaitingTime = 0;
        for (Process process : processes) {
            System.out.println("Process " + process.getId() + ": Turnaround Time = " + process.getTurnaroundTime() + ", Waiting Time = " + process.getWaitingTime());
            totalTurnaroundTime += process.getTurnaroundTime();
            totalWaitingTime += process.getWaitingTime();
        }
        double avgTurnaroundTime = totalTurnaroundTime / processes.size();
        double avgWaitingTime = totalWaitingTime / processes.size();
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
        System.out.println("Average Waiting Time: " + avgWaitingTime);
    }
    private static void resetProcesses(List<Process> processes) {
        for (Process process : processes) {
            process.setRemainingTime(process.getBurstTime());
            process.setCompletionTime(0);
            process.setWaitingTime(0);
            process.setTurnaroundTime(0);
        }}}
class Process {
    private int id;
    private int burstTime;
    private int remainingTime;
    private int priority;
    private int arrivalTime;
    private int completionTime;
    private int waitingTime;
    private int turnaroundTime;
    public Process(int id, int burstTime, int priority, int arrivalTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
    }
    public int getId() {
        return id;
    }
    public int getBurstTime() {
        return burstTime;
    }
    public int getRemainingTime() {
        return remainingTime;
    }
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
    public int getPriority() {
        return priority;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getCompletionTime() {
        return completionTime;
    }
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
    public int getWaitingTime() {
        return waitingTime;
    }
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
    public int getTurnaroundTime() {
        return turnaroundTime;
    }
    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }}
class RoundRobinScheduler {
    private Queue<Process> queue;
    private int Q;
    public RoundRobinScheduler(int Q) {
        this.queue = new LinkedList<>();
        this.Q = Q;
    }
    public void addProcess(Process process) {
        queue.add(process);
    }
    public void execute() {
        int Anlık_Zaman = 0;
        while (!queue.isEmpty()) {
            Process process = queue.poll();
            int remainingTime = process.getRemainingTime();
            if (remainingTime > Q) {
                System.out.println("Process " + process.getId() + " executed for " + Q + " units.");
                process.setRemainingTime(remainingTime - Q);
                queue.add(process);
                Anlık_Zaman += Q;
            } else {
                System.out.println("Process " + process.getId() + " executed for " + remainingTime + " units and finished.");
                Anlık_Zaman += remainingTime;
                process.setCompletionTime(Anlık_Zaman);
                process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
                process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
            }}}}
class PriorityScheduler {
    private PriorityQueue<Process> queue;
    public PriorityScheduler() {
        this.queue = new PriorityQueue<>((p1, p2) -> p1.getPriority() - p2.getPriority());
    }
    public void addProcess(Process process) {
        queue.add(process);
    }
    public void execute() {
        int Anlık_Zaman = 0;
        while (!queue.isEmpty()) {
            Process process = queue.poll();
            System.out.println("Process " + process.getId() + " with priority " + process.getPriority() + " executed for " + process.getBurstTime() + " units and finished.");
            Anlık_Zaman += process.getBurstTime();
            process.setCompletionTime(Anlık_Zaman);
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
        }}}
class MultiLevelQueueScheduler {
    private Queue<Process> highPriorityQueue;
    private Queue<Process> lowPriorityQueue;
    private int Q;
    public MultiLevelQueueScheduler(int Q) {
        this.highPriorityQueue = new LinkedList<>();
        this.lowPriorityQueue = new LinkedList<>();
        this.Q = Q;
    }
    public void addProcess(Process process) {
        if (process.getPriority() <= 5) {
            highPriorityQueue.add(process);
        } else {
            lowPriorityQueue.add(process);
        }}
    public void execute() {
        int currentTime = 0;
        while (!highPriorityQueue.isEmpty() || !lowPriorityQueue.isEmpty()) {
            currentTime = executeQueue(highPriorityQueue, currentTime);
            if (highPriorityQueue.isEmpty()) {
                currentTime = executeQueue(lowPriorityQueue, currentTime);
            }}}
    private int executeQueue(Queue<Process> queue, int Anlık_Zaman) {
        if (!queue.isEmpty()) {
            Process process = queue.poll();
            int remainingTime = process.getRemainingTime();
            if (remainingTime > Q) {
                System.out.println("Process " + process.getId() + " executed for " + Q + " units.");
                process.setRemainingTime(remainingTime - Q);
                queue.add(process);
                Anlık_Zaman += Q;
            } else {
                System.out.println("Process " + process.getId() + " executed for " + remainingTime + " units and finished.");
                Anlık_Zaman += remainingTime;
                process.setCompletionTime(Anlık_Zaman);
                process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
                process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
            }}
        return Anlık_Zaman;
    }}