package FST;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class FST {
	public static void main(String[] args) {
		String file = "FST.txt";
		List<String> States = new ArrayList<>();
		assignments(file,States);
		try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.close();
            System.out.println(file+" dosyası okundu.");
        } catch (IOException e) {
            System.err.println("Dosya okunamadı " + e.getMessage());
        }
		 System.out.println("Lütfen birinci input’u giriniz: ");
	     
	     int[] inputs = inassi(file, States);	  
	     int[] okus = ins(file, States, inputs);
	     List<String> durumlar = Durumout(States, inputs, okus);
	     List<Integer> outputs = outpout(States, inputs, okus);
	        System.out.println("Durumların sırası: "+durumlar);
	        System.out.println("Çıktı: " +outputs);
	        System.out.println("Lütfen ikinci input’u giriniz: ");
	     int[] okus1 = ins(file, States, inputs);
	     List<String> durumlar1 = Durumout(States, inputs, okus1);
		 List<Integer> outputs1 = outpout(States, inputs, okus1);
		 System.out.println("Durumların sırası:"+ durumlar1);
		    System.out.println("Çıktı:"+ outputs1);
	}
	public static List<String> Durumout(List<String> States,int [] inputs, int [] okus) {
		List<String> durumlar = new ArrayList<>();
			durumlar.add(0,States.get(0));
	        for (int i = 0; i < okus.length; i++) {   
	            if (okus[i] == inputs[0] && durumlar.get(i).equals(States.get(0)) ) {
	            	durumlar.add(i+1,States.get(0));	
	            } else if (okus[i] == inputs[1] && durumlar.get(i).equals(States.get(0))) {
	            	durumlar.add(i+1,States.get(0));	
	            } else if (okus[i] == inputs[2] && durumlar.get(i).equals(States.get(0))) {
	            	durumlar.add(i+1,States.get(1)); 	            	
	            }
	            else if (okus[i] == inputs[0] && durumlar.get(i).equals(States.get(1))) {
	            	durumlar.add(i+1,States.get(0)); 	            	
	            }
	            else if (okus[i] == inputs[1] && durumlar.get(i).equals(States.get(1))) {
	            	durumlar.add(i+1,States.get(1)); 	            	
	            }
	            else if (okus[i] == inputs[2] && durumlar.get(i).equals(States.get(1))) {
	            	durumlar.add(i+1,States.get(1)); 	            	
	            }
	            else {
					System.out.println("An error accured");
				}	           
	        }	        
	        return durumlar;	       
	}
	public static List<Integer> outpout(List<String> States,int [] inputs, int [] okus) {
		  List<String> durumlar= Durumout(States, inputs, okus);
	     List<Integer> outputs = new ArrayList<>();
	        	
	     	
	        for (int i = 0; i < okus.length; i++) {
	           
	            if (okus[i] == inputs[0] && durumlar.get(i).equals(States.get(0)) ) {
	            	
	            	outputs.add(0);
	            } else if (okus[i] == inputs[1] && durumlar.get(i).equals(States.get(0))) {
	            	
	            	outputs.add(0);
	            } else if (okus[i] == inputs[2] && durumlar.get(i).equals(States.get(0))) {
	            	
	            	outputs.add(1);
	            }
	            else if (okus[i] == inputs[0] && durumlar.get(i).equals(States.get(1))) {
	            	
	            	outputs.add(0);
	            }
	            else if (okus[i] == inputs[1] && durumlar.get(i).equals(States.get(1))) {
	            	
	            	outputs.add(1);
	            }
	            else if (okus[i] == inputs[2] && durumlar.get(i).equals(States.get(1))) {
	            	
	            	outputs.add(1);
	            }
	            else {
					System.out.println("An error accured");
				}
	           
	        }
	        
	       return outputs;
	}
	
	public static int[] ins(String file,List<String> States ,int[] inputs) {		 
		 Scanner sc = new Scanner(System.in);
	     int oku = sc.nextInt();	     
	     int basamaksayı = String.valueOf(oku).length();
	     int[] okus = new int[basamaksayı];
	     for (int i = basamaksayı-1 ; i >= 0; i--) {
			okus[i] = oku %10;
			oku/=10;
		}	    
	     boolean isOkuValid = false; 
	     for (int i = 0; i < okus.length ; i++) {
	    	    boolean isilkValid = false;  
	    	    for (int j = 0; j < inputs.length; j++) {
	    	        if (okus[i] == inputs[j]) {
	    	            isilkValid = true;
	    	            break;
	    	        }
	    	    }
	    	    if (!isilkValid) {  
	    	        isOkuValid = false; 
	    	        break;
	    	    }
	    	    else {
	    	        isOkuValid = true;  
	    	    }
	    	}	     	     
	     if (isOkuValid) {		      
		     } else {
		         System.out.println("Girilen input değerleri geçerli değil. Programı tekrar başlatın.");		         
	     }		
	     return okus;
	}

	public static void assignments(String file , List<String> States ) {		
		try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String sıra ;                    
            while ((sıra =br.readLine())!= null)  {
				if (sıra.contains("Q = {")) {										
					Pattern pattern = Pattern.compile("\\{(.+?)\\}");
                    Matcher matcher = pattern.matcher(sıra);                   
                    if (matcher.find()) {
						String degerler = matcher.group(1);
						String[] statesDegerler = degerler.split(",");
						for (String statesDegeri : statesDegerler) {
							States.add(statesDegeri.trim());
						}}}}                        
            br.close();
        } catch (IOException e) {
            System.err.println("Dosya okunamadı: " + e.getMessage());
        }}
		public static void rstates(List<String> States) {
		 if (!States.isEmpty()) {
	            System.out.print("States dizisi: ");
	            for (String state : States) {
	                System.out.print(state + " ");
	            }
	            System.out.println();
	        } else {
	            System.out.println("States dizisi bulunamadı.");
	        }}	
		public static int[] inassi(String file, List<String> States) {
	    int[] inputs = null; 
	    try (FileReader SatırOkuyucu = new FileReader(file);
	         BufferedReader bufferedReader = new BufferedReader(SatırOkuyucu)) {
	        String satir;
	        while ((satir = bufferedReader.readLine()) != null) {
	            if (satir.contains("Σ = {")) {
	                Pattern pattern = Pattern.compile("\\{(.+?)\\}");
	                Matcher matcher = pattern.matcher(satir);
	                if (matcher.find()) {
	                    String icerik = matcher.group(1);
	                    String[] elemanlar = icerik.split(",");
	                    inputs = new int[elemanlar.length];
	                    for (int i = 0; i < elemanlar.length; i++) {
	                        inputs[i] = Integer.parseInt(elemanlar[i].trim());
	                    }
	                    break;
	                }}}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }	   
	    return inputs;
	}}