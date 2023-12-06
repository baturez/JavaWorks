public class kontrol_sinyalleri{
	    private boolean sinyal1;
	    private boolean sinyal2;
	    private boolean sinyal3;

	    public kontrol_sinyalleri() {
	        this.sinyal1 = false;
	        this.sinyal2 = false;
	        this.sinyal3 = false;
	    }

	    public boolean isSinyal1() {
	        return sinyal1;
	    }

	    public boolean isSinyal2() {
	        return sinyal2;
	    }

	    public boolean isSinyal3() {
	        return sinyal3;
	    }

	    public void setSinyal1(boolean value) {
	        this.sinyal1 = value;
	    }

	    public void setSinyal2(boolean value) {
	        this.sinyal2 = value;
	    }

	    public void setSinyal3(boolean value) {
	        this.sinyal3 = value;
	    }}
	