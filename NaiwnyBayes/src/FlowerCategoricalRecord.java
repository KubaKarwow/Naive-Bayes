import java.util.Arrays;

public class FlowerCategoricalRecord {

    private int[] categoricalAttributes;
    private String flowerKind;

    public FlowerCategoricalRecord(int[] categoricalAttributes, String flowerKind) {
        this.categoricalAttributes = categoricalAttributes;
        this.flowerKind = flowerKind;
    }
    public FlowerCategoricalRecord(int amountOfAttributes, String flowerKind){
        this.flowerKind=flowerKind;
        categoricalAttributes = new int[amountOfAttributes];
    }

    public int[] getCategoricalAttributes() {
        return categoricalAttributes;
    }

    public void setCategoricalAttributes(int[] categoricalAttributes) {
        this.categoricalAttributes = categoricalAttributes;
    }

    public String getFlowerKind() {
        return flowerKind;
    }

    public void setFlowerKind(String flowerKind) {
        this.flowerKind = flowerKind;
    }

    @Override
    public String toString() {
        return "FlowerCategoricalRecord{" +
                "categoricalAttributes=" + Arrays.toString(categoricalAttributes) +
                ", flowerKind='" + flowerKind + '\'' +
                '}';
    }
}
