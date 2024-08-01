import java.util.ArrayList;
import java.util.List;
public class VFSSystem {
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        Dosya_Yonetimi dm = new Dosya_Yonetimi(1000);
        System.out.println("1. Dosya ve dizin oluşturma:");
        fs.createDirectory("/", "home");
        fs.createDirectory("/home", "user");
        if (dm.allocateSpace(100)) {
            fs.createFile("/home/user", "file1.txt", 100, "Hello World!");
            System.out.println("File created: /home/user/file1.txt");
        } else {
            System.out.println("Failed to allocate space for file1.txt");
        }
        System.out.println("\n2. Dosya ve dizin listeleme:");
        listDirectory(fs.getRoot(), 0);
        System.out.println("\n3. Dosya silme:");
        fs.deleteFile("/home/user", "file1.txt");
        dm.deallocateSpace(100);
        System.out.println("File deleted: /home/user/file1.txt");
        System.out.println("\n4. Dosya ve dizin listeleme (silme sonrası):");
        listDirectory(fs.getRoot(), 0);
        System.out.println("\n5. Disk alanı durumu:");
        System.out.println("Total Space: " + dm.topBoslukVer());
        System.out.println("Used Space: " + dm.KullAlanVer());
        System.out.println("Free Space: " + dm.BosAlanVer());
    }
    public static void listDirectory(Kütüph dir, int giris) {
        for (int i = 0; i < giris; i++) {
            System.out.print("  ");
        }
        System.out.println("Directory: " + dir.getName());
        for (Dosya dosya : dir.DosyaVer()) {
            for (int i = 0; i < giris + 1; i++) {
                System.out.print("  ");
            }
            System.out.println("File: " + dosya.getName() + ", Boyut: " + dosya.BoyutVer());
        }
        for (Kütüph subDir : dir.getSubDirectories()) {
            listDirectory(subDir, giris + 1);
        }}}
class Dosya {
    private String isim;
    private int boyut;
    private String icerik;
    public Dosya(String isim, int boyut, String icerik) {
        this.isim = isim;
        this.boyut = boyut;
        this.icerik = icerik;
    }
    public String getName() {
        return isim;
    }
    public int BoyutVer() {
        return boyut;
    }
    public String getContent() {
        return icerik;
    }
    public void setContent(String icerik) {
        this.icerik = icerik;
    }}
class Kütüph {
    private String isim;
    private List<Dosya> dosyalar;
    private List<Kütüph> dizinler;
    public Kütüph(String isim) {
        this.isim = isim;
        this.dosyalar = new ArrayList<>();
        this.dizinler = new ArrayList<>();
    }
    public void addFile(Dosya dosya) {
        dosyalar.add(dosya);
    }
    public void removeFile(Dosya dosya) {
        dosyalar.remove(dosya);
    }
    public void addSubDirectory(Kütüph dir) {
        dizinler.add(dir);
    }
    public void removeSubDirectory(Kütüph dir) {
        dizinler.remove(dir);
    }
    public String getName() {
        return isim;
    }
    public List<Dosya> DosyaVer() {
        return dosyalar;
    }
    public List<Kütüph> getSubDirectories() {
        return dizinler;
    }
}
class FileSystem {
    private Kütüph root;
    public FileSystem() {
        root = new Kütüph("/");
    }
    public Kütüph getRoot() {
        return root;
    }
    public void createFile(String yol, String isim, int boyut, String icerik) {
        Kütüph dir = navigateToDirectory(yol);
        if (dir != null) {
            dir.addFile(new Dosya(isim, boyut, icerik));
        }}
    public void createDirectory(String yol, String isim) {
        Kütüph dir = navigateToDirectory(yol);
        if (dir != null) {
            dir.addSubDirectory(new Kütüph(isim));
        }}
    public void deleteFile(String yol, String isim) {
        Kütüph dir = navigateToDirectory(yol);
        if (dir != null) {
            Dosya dosya = findFile(dir, isim);
            if (dosya != null) {
                dir.removeFile(dosya);
            }}}
    public void deleteDirectory(String yol, String isim) {
        Kütüph dir = navigateToDirectory(yol);
        if (dir != null) {
            Kütüph subDir = findDirectory(dir, isim);
            if (subDir != null) {
                dir.removeSubDirectory(subDir);
            }}}
    private Kütüph navigateToDirectory(String yol) {
        String[] parca = yol.split("/");
        Kütüph current = root;
        for (String kısım : parca) {
            if (!kısım.isEmpty()) {
                boolean found = false;
                for (Kütüph subDir : current.getSubDirectories()) {
                    if (subDir.getName().equals(kısım)) {
                        current = subDir;
                        found = true;
                        break;
                    }}
                if (!found) {
                    return null;
                }}}
        return current;
    }
    private Dosya findFile(Kütüph dir, String isim) {
        for (Dosya dosya : dir.DosyaVer()) {
            if (dosya.getName().equals(isim)) {
                return dosya;
            }}
        return null;
    }
    private Kütüph findDirectory(Kütüph dir, String isim) {
        for (Kütüph subDir : dir.getSubDirectories()) {
            if (subDir.getName().equals(isim)) {
                return subDir;
            }}
        return null;
    }}
class Dosya_Yonetimi {
    private int top_bosluk;
    private int kull_bosluk;
    public Dosya_Yonetimi(int top_bosluk) {
        this.top_bosluk = top_bosluk;
        this.kull_bosluk = 0;
    }
    public boolean allocateSpace(int boyut) {
        if (kull_bosluk + boyut <= top_bosluk) {
            kull_bosluk += boyut;
            return true;
        }
        return false;
    }
    public void deallocateSpace(int boyut) {
        if (kull_bosluk >= boyut) {
            kull_bosluk -= boyut;
        }}
    public int topBoslukVer() {
        return top_bosluk;
    }
    public int KullAlanVer() {
        return kull_bosluk;
    }
    public int BosAlanVer() {
        return top_bosluk - kull_bosluk;
    }}