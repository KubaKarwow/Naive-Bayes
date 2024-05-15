import java.util.ArrayList;
import java.util.List;

public class DataTransformer {
    private static List<FlowerNominalRecord> nominalRecords;
    public static final int AMOUNT_OF_CLASSES=3;

    public static List<FlowerCategoricalRecord> transform(List<FlowerNominalRecord> nominalRecords){
        List<FlowerCategoricalRecord> result = createResultList( nominalRecords);

        int length = nominalRecords.get(0).getNumericAttributes().length;
        for (int i = 0; i < length; i++) {
            double minFromColumn = getMinFromColumn(i,nominalRecords);
            double maxFromColumn = getMaxFromColumn(i,nominalRecords);
            double differenceBetweenClasses=(maxFromColumn-minFromColumn)/AMOUNT_OF_CLASSES;
            for (int j = 0; j < nominalRecords.size(); j++) {
                int categoricalValue = classifyValue(minFromColumn, differenceBetweenClasses, nominalRecords.get(j).getNumericAttributes()[i]);
                result.get(j).getCategoricalAttributes()[i]=categoricalValue;
            }

        }
        return result;
    }
    public static FlowerCategoricalRecord transformSingleRecord(FlowerNominalRecord nominalRecord,List<FlowerNominalRecord> training){
        int length = nominalRecord.getNumericAttributes().length;
        FlowerCategoricalRecord result = new FlowerCategoricalRecord(length,"");
        for (int i = 0; i < length; i++) {
            double minFromColumn = getMinFromColumn(i,training);
            double maxFromColumn = getMaxFromColumn(i,training);
            double differenceBetweenClasses=(maxFromColumn-minFromColumn)/AMOUNT_OF_CLASSES;
            int categoricalValue = classifyValue(minFromColumn, differenceBetweenClasses, nominalRecord.getNumericAttributes()[i]);
            result.getCategoricalAttributes()[i]=categoricalValue;

        }
        return result;
    }
    private static List<FlowerCategoricalRecord> createResultList(List<FlowerNominalRecord> nominalRecords){
        List<FlowerCategoricalRecord> result = new ArrayList<>();
        for (FlowerNominalRecord nominalRecord : nominalRecords) {
            result.add(new FlowerCategoricalRecord(nominalRecord.getNumericAttributes().length,nominalRecord.getFlowerKind()));
        }
        return result;
    }
    private static int classifyValue(double min, double differenceBetweenClasses, double value){
        for (int i = 0; i < AMOUNT_OF_CLASSES; i++) {
            if(value>=min && value <= min+differenceBetweenClasses){
                return i+1;
            }
            min+=differenceBetweenClasses;
        }
        // lines 26 should always return the value, -1 here is just for possible debug purposes
        if(value<min){
            return 0;
        } else{
            return AMOUNT_OF_CLASSES;
        }
    }
    private static double getMinFromColumn(int columnNumber, List<FlowerNominalRecord> nominalRecords){
        return nominalRecords.stream().min((r1, r2) ->
        {
            return Double.compare(r1.getNumericAttributes()[columnNumber],
                    r2.getNumericAttributes()[columnNumber]);
        }).get().getNumericAttributes()[columnNumber];
    }
    private static double getMaxFromColumn(int columnNumber,List<FlowerNominalRecord> nominalRecords){
        return nominalRecords.stream().max((r1, r2) ->
        {
            return Double.compare(r1.getNumericAttributes()[columnNumber],
                    r2.getNumericAttributes()[columnNumber]);
        }).get().getNumericAttributes()[columnNumber];
    }

}
