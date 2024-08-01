import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class Thread_management {
	public static void main(String[] args) {
		Hotel hotel = new Hotel();
		// 	performans analizi için üretici sayısını ve tüketici sayısını değiş
		Thread müs1 = new Thread(new Müsteri(hotel, "Müşteri 1", 1000));
		Thread müs2 = new Thread(new Müsteri(hotel, "Müşteri 2", 2000));
		Thread müs3 = new Thread(new Müsteri(hotel, "Müşteri 3", 3000));
		Thread calısan1 = new Thread(new Calısan(hotel, "Çalışan 1", 1500));
		Thread calısan2 = new Thread(new Calısan(hotel, "Çalışan 2", 2500));
		müs1.start();
		müs2.start();
		müs3.start();
		calısan1.start();
		calısan2.start();
}}
class Hotel {
	private Queue<String> rezervasayonlar = new LinkedList<>();
	//performans analizi için kuyruk boyutunu değiştir
	private final int Kapasite = 10;
	private final Semaphore semaphore = new Semaphore(Kapasite);
	private final Lock lock = new ReentrantLock();
	public void rez(String rezervarsyon) throws InterruptedException {
        long start = System.nanoTime();
		semaphore.acquire();
		lock.lock();
		try {
			while (rezervasayonlar.size() == Kapasite) {
			}
			rezervasayonlar.add(rezervarsyon);
            long end = System.nanoTime();
			System.out.println("Rezervasyon yapıldı: " + rezervarsyon + " (Süre: " + (end - start) + " ns)");
		} finally {
			lock.unlock();
			semaphore.release();
		}}
	public String process() throws InterruptedException {
        long start = System.nanoTime();
		semaphore.acquire();
		lock.lock();
		try {
			while (rezervasayonlar.isEmpty()) {
			}
			String rezervasyon = rezervasayonlar.poll();
            long end = System.nanoTime();
			System.out.println("Rezervasyon işlendi: " + rezervasyon + " (Süre: " + (end - start) + " ns)");
			return rezervasyon;
		} finally {
			lock.unlock();
			semaphore.release();
		}}}
class Müsteri implements Runnable {
	private final Hotel hotel;
	private final String müs_isim;
	private final int rez_süre;
	public Müsteri(Hotel hotel, String müs_isim, int rez_süre) {
		this.hotel = hotel;
		this.müs_isim = müs_isim;
		this.rez_süre = rez_süre;
	}
	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(rez_süre);
				hotel.rez(müs_isim + " rezervasyonu");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}}
class Calısan implements Runnable {
	private final Hotel hotel;
	private final String müs_isim;
	private final int isleme_süre;
	public Calısan(Hotel hotel, String müs_isim, int isleme_süre) {
		this.hotel = hotel;
		this.müs_isim = müs_isim;
		this.isleme_süre = isleme_süre;
	}
	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(isleme_süre);
				hotel.process();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}}