package _20010310067_FST;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FST {
    private String file;
    private List<String> states;
    private int[] inputs;

    public FST(String file) {
        this.file = file;
        this.states = new ArrayList<>();
        this.inputs = new int[0];
        this.loadStatesAndInputs();
    }

    public void process() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.close();
            System.out.println(file + " dosyası okundu.");
        } catch (IOException e) {
            System.err.println("Dosya okunamadı " + e.getMessage());
        }

        System.out.println("Lütfen birinci input’u giriniz: ");
        int[] okus = getInput();
        List<String> durumlar = new ArrayList<String>(okus.length+1);
        durumlar = calculateStates(states,inputs,okus);
        List<Integer> outputs = new ArrayList<Integer>(okus.length);
        outputs = calculateOutputs(states,inputs,okus);
        System.out.println("Durumların sırası: " + durumlar);
        System.out.println("Çıktı: " + outputs);
        System.out.println("Lütfen ikinci input’u giriniz: ");
        int[] okus1 = getInput();
        List<String> durumlar1 = calculateStates(states,inputs,okus1);
        List<Integer> outputs1 = calculateOutputs(states,inputs,okus1);
        System.out.println("Durumların sırası:" + durumlar1);
        System.out.println("Çıktı:" + outputs1);
    }

    private void loadStatesAndInputs() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String sıra;
            while ((sıra = br.readLine()) != null) {
                if (sıra.contains("Q = {")) {
                    Pattern pattern = Pattern.compile("\\{(.+?)\\}");
                    Matcher matcher = pattern.matcher(sıra);
                    if (matcher.find()) {
                        String degerler = matcher.group(1);
                        String[] statesDegerler = degerler.split(",");
                        for (String statesDegeri : statesDegerler) {
                            states.add(statesDegeri.trim());
                        }
                    }
                } else if (sıra.contains("Σ = {")) {
                    Pattern pattern = Pattern.compile("\\{(.+?)\\}");
                    Matcher matcher = pattern.matcher(sıra);
                    if (matcher.find()) {
                        String icerik = matcher.group(1);
                        String[] elemanlar = icerik.split(",");
                        inputs = new int[elemanlar.length];
                        for (int i = 0; i < elemanlar.length; i++) {
                            inputs[i] = Integer.parseInt(elemanlar[i].trim());
                        }
                    }
                }
            }        
            br.close();
        } catch (IOException e) {
            System.err.println("Dosya okunamadı: " + e.getMessage());
        }
    }

    private int[] getInput() {
        Scanner sc = new Scanner(System.in);
        int oku = sc.nextInt();
        int basamaksayı = String.valueOf(oku).length();
        int[] okus = new int[basamaksayı];
        for (int i = basamaksayı - 1; i >= 0; i--) {
            okus[i] = oku % 10;
            oku /= 10;
        }
        boolean isOkuValid = true;
        for (int i = 0; i < okus.length; i++) {
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
        }
        if (!isOkuValid) {
            System.out.println("Girilen input değerleri geçerli değil. Programı tekrar başlatın.");
        }
        return okus;
    }

    private List<String> calculateStates(List<String> States,int [] inputs, int [] okus) {
        List<String> durumlar = new ArrayList<>();
        durumlar.add(0, states.get(0));
        for (int i = 0; i < okus.length; i++) {
            if (i < durumlar.size()) {
                if (okus[i] == inputs[0] && durumlar.get(i).equals(states.get(0))) {
                    durumlar.add(i + 1, states.get(0));
                } else if (okus[i] == inputs[1] && durumlar.get(i).equals(states.get(0))) {
                    durumlar.add(i + 1, states.get(0));
                } else if (okus[i] == inputs[2] && durumlar.get(i).equals(states.get(0))) {
                    durumlar.add(i + 1, states.get(1));
                } else if (okus[i] == inputs[0] && durumlar.get(i).equals(states.get(1))) {
                    durumlar.add(i + 1, states.get(0));
                } else if (okus[i] == inputs[1] && durumlar.get(i).equals(states.get(1))){
                    durumlar.add(i + 1, states.get(1));
                } else if (okus[i] == inputs[2] && durumlar.get(i).equals(states.get(1))) {
                    durumlar.add(i + 1, states.get(1));
                } else {
                    System.out.println("An error occurred");
                }
            } else {
                System.out.println("Index out of bounds: " + i);
            }
        }
        return durumlar;
    }

    private List<Integer> calculateOutputs(List<String> States,int [] inputs, int [] okus) {
        List<String> durumlar = calculateStates(states,inputs,okus);
        List<Integer> outputs = new ArrayList<>();


        for (int i = 0; i < okus.length; i++) {
           
        	if (okus[i] == inputs[0] && durumlar.get(i).equals(States.get(0)) ) {
            	
            	outputs.add(0);
            } 
        		else if (okus[i] == inputs[1] && durumlar.get(i) == (States.get(0))) {
            	
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
}
