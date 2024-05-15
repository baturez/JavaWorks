package _20010310067_Batuhan_Kabasakaloglu;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class _20010310067_PCB {
	static List<_20010310067_Process> prosesler = new ArrayList<>();
    public static void main(String[] args) {
        String file = "girdi.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sıra;
            while ((sıra = br.readLine()) != null) {
                String[] parca = sıra.split(" ");
                String parca_isim = parca[0];
                int startTime = Integer.parseInt(parca[1]);
                int endTime = Integer.parseInt(parca[2]);
                int dataSize = Integer.parseInt(parca[3]);
                int codeSize = Integer.parseInt(parca[4]);
                int stackSize = Integer.parseInt(parca[5]);
                int heapSize = Integer.parseInt(parca[6]);
                prosesler.add(new _20010310067_Process(parca_isim, startTime, endTime, dataSize, codeSize, stackSize, heapSize));
            }
            System.out.println(file + " dosyası okundu.");
        } catch (IOException e) {
            System.err.println("Dosya okunamadı " + e.getMessage());
        }
        _20010310067_RAM ram = new _20010310067_RAM(16000000);
        for (_20010310067_Process process : prosesler) {
            ram.allocate(process);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Lütfen RAM’in durumunu görmek istediğiniz saniyeyi giriniz.");
        int zaman = sc.nextInt();
        ram.printState(zaman);
        ram.printPCBs(zaman);
        System.out.println(zaman + ". saniyedeki PCB’sini görüntülemek istediğiniz proses ismini giriniz:");
        String proses_ismi = sc.next();
        for (_20010310067_Process process : prosesler) {
            if (process.getName().equals(proses_ismi) && process.getStartTime() <= zaman && process.getEndTime() >= zaman) {
                System.out.println(proses_ismi + " isimli prosesin " + zaman + ". Saniyedeki PCB bilgileri şu şekildedir:");
                System.out.println("Proses numarası: " + process.getPid());
                System.out.println("Prosesin yaratılma zamanı: " + process.getStartTime() + ". saniye");
                System.out.println("Prosesin toplam büyüklüğü: " + process.getTotalSize() + " KB");
                break;
            }
        }
    }
}
class _20010310067_RAM {
    private int size;
    private List<_20010310067_MemoryBlock> memoryBlocks;
    public _20010310067_RAM(int size) {
        this.size = size;
        this.memoryBlocks = new ArrayList<>();
        this.memoryBlocks.add(new _20010310067_MemoryBlock(0, 1023999, null));
    }
    public boolean allocate(_20010310067_Process process) {
        int requiredSize = process.getTotalSize() * 1024;
        int lastEnd = memoryBlocks.get(memoryBlocks.size() - 1).end;
        int startAddress = lastEnd + 1;
        int endAddress = startAddress + requiredSize - 1;
        if (endAddress < size) {
            memoryBlocks.add(new _20010310067_MemoryBlock(startAddress, endAddress, process));
            return true;
        }
        return false;
    }
    public void deallocate(_20010310067_Process process) {
        for (_20010310067_MemoryBlock block : memoryBlocks) {
            if (block.process != null && block.process.getPid() == process.getPid()) {
                block.process = null;
                return;
            }
        }
    }
    public void printState(int zaman) {
        System.out.println(zaman + ". Saniyede RAM’in dolu olan kısımları:");
        for (_20010310067_MemoryBlock block : memoryBlocks) {
            if (block.process != null && block.process.getStartTime() <= zaman && block.process.getEndTime() >= zaman) {
                System.out.println(block.start + ". Ve " + block.end + ". Adresler arasında " + block.process.getName() + " programı bulunmaktadır.");
            } else if (block.process == null) {
                System.out.println(block.start + ". Ve " + block.end + ". Adresler arasında işletim sistemi bulunmaktadır.");
            }
        }
    }
    public void printPCBs(int zaman) {
        System.out.print("PCB’si bulunan Prosesler: ");
        for (_20010310067_MemoryBlock block : memoryBlocks) {
            if (block.process != null && block.process.getStartTime() <= zaman && block.process.getEndTime() >= zaman) {
                System.out.print(block.process.getName() + " ");
            }
        }
        System.out.println();
    }
}
class _20010310067_MemoryBlock {
    int start;
    int end;
    _20010310067_Process process;
    public _20010310067_MemoryBlock(int start, int end, _20010310067_Process process) {
        this.start = start;
        this.end = end;
        this.process = process;
    }
}
class _20010310067_Process {
    private static int pidCounter = 1002;
    private int pid;
    private String name;
    private int startTime;
    private int endTime;
    private int dataSize;
    private int codeSize;
    private int stackSize;
    private int heapSize;
    private int totalSize;
    public _20010310067_Process(String name, int startTime, int endTime, int dataSize, int codeSize, int stackSize, int heapSize) {
        this.pid = pidCounter++;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dataSize = dataSize;
        this.codeSize = codeSize;
        this.stackSize = stackSize;
        this.heapSize = heapSize;
        this.totalSize = dataSize + codeSize + stackSize + heapSize;
    }
    public int getPid() {
        return pid;
    }
    public String getName() {
        return name;
    }
    public int getStartTime() {
        return startTime;
    }
    public int getEndTime() {
        return endTime;
    }
    public int getTotalSize() {
        return totalSize;
    }
}