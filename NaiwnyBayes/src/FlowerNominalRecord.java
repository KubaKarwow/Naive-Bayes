import java.util.Arrays;

public class FlowerNominalRecord {
    private double[] numericAttributes;
    private String flowerKind;

    public FlowerNominalRecord(double[] numericAttributes, String flowerKind) {
        this.numericAttributes = numericAttributes;
        this.flowerKind = flowerKind;
    }

    public double[] getNumericAttributes() {
        return numericAttributes;
    }

    public void setNumericAttributes(double[] numericAttributes) {
        this.numericAttributes = numericAttributes;
    }

    public String getFlowerKind() {
        return flowerKind;
    }

    public void setFlowerKind(String flowerKind) {
        this.flowerKind = flowerKind;
    }

    @Override
    public String toString() {
        return "FlowerRecord{" +
                "numericAttributes=" + Arrays.toString(numericAttributes) +
                ", flowerKind='" + flowerKind + '\'' +
                '}';
    }
}
