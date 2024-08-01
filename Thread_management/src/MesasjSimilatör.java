import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
public class MesasjSimilatör {
    public static void main(String[] args) {
        BlockingQueue<Mesaj> mesaj_sıra = new LinkedBlockingQueue<>();
        int kullanıcı_kapasite = 3; 
        Thread[] userThreads = new Thread[kullanıcı_kapasite];
        for (int i = 0; i < kullanıcı_kapasite; i++) {
            userThreads[i] = new Thread(new Kullanıcı(i, mesaj_sıra));
            userThreads[i].start();
        }
        for (Thread thread : userThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }}}}
class Kullanıcı implements Runnable {
    private int kullanıcı_id;
    private BlockingQueue<Mesaj> mesaj_sıra;
    public Kullanıcı(int kullanıcı_id, BlockingQueue<Mesaj> mesaj_sıra) {
        this.kullanıcı_id = kullanıcı_id;
        this.mesaj_sıra = mesaj_sıra;
    }
    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(1, 3));
                String mesaj_içerik = "Kullanıcı " + kullanıcı_id + " merhaba :  " + System.currentTimeMillis();
                Mesaj mesaj = new Mesaj(kullanıcı_id, mesaj_içerik);
                mesaj_sıra.put(mesaj);
                Mesaj gelen_mesaj;
                while ((gelen_mesaj = mesaj_sıra.poll(100, TimeUnit.MILLISECONDS)) != null) {
                    if (gelen_mesaj.mesajAl() != kullanıcı_id) {
                        System.out.println("Kullanıcı " + kullanıcı_id + " adlı kullanıcıdan gelen mesaj " + gelen_mesaj.mesajAl() + ": " + gelen_mesaj.icerikAl());
                    }}
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }}}}
class Mesaj {
    private int kullanıcıId;
    private String icerik;
    public Mesaj(int kullanıcıId, String icerik) {
        this.kullanıcıId = kullanıcıId;
        this.icerik = icerik;
    }
    public int mesajAl() {
        return kullanıcıId;
    }
    public String icerikAl() {
        return icerik;
    }
}