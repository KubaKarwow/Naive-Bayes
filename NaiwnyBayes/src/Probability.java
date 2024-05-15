public class Probability {
    private int divided;
    private int divider;

    public Probability(int divided, int divider) {
        this.divided = divided;
        this.divider = divider;
    }

    public int getDivided() {
        return divided;
    }

    public void setDivided(int divided) {
        this.divided = divided;
    }

    public int getDivider() {
        return divider;
    }

    public void setDivider(int divider) {
        this.divider = divider;
    }

    @Override
    public String toString() {
        return divided+"/"+divider;
    }
}
