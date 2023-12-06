import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RAM {
	public static void main(String[] args) throws InterruptedException {
		String file = "RAM.txt";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			br.close();
			System.out.println(file + " dosyası okundu.");
		} catch (IOException e) {
			System.err.println("Dosya okunamadı " + e.getMessage());
		}
		İşleme count = new İşleme(file);

	}
}
