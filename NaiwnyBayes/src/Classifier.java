import java.util.*;
import java.util.stream.Collectors;

public class Classifier {
    private List<FlowerCategoricalRecord> training;
    private Set<String> possibleAnswers;
    private List<ErrorMatrixRow> errorMatrix;

    public Classifier(List<FlowerCategoricalRecord> training) {
        this.training = training;
        possibleAnswers=getAllPossibleAnswers();
        errorMatrix = new ArrayList<>();
    }

    private Set<String> getAllPossibleAnswers() {
        return training.stream().map(FlowerCategoricalRecord::getFlowerKind).collect(Collectors.toSet());
    }
    private Map<String,Integer> initializeErrorMatrixRow(){
        HashMap<String, Integer> result = new HashMap<>();
        for (String possibleAnswer : possibleAnswers) {
            result.put(possibleAnswer,0);
        }
        return result;
    }
    private void putInTheRightPlaceInErrorMatrix(String programAnswer,String properAnswer){
        for (ErrorMatrixRow matrixRow : errorMatrix) {
            if(matrixRow.getKey().equals(properAnswer)){
                matrixRow.getOccurrences().put(programAnswer,matrixRow.getOccurrences().get(programAnswer)+1);
            }
        }
    }
    public void processTestData(List<FlowerCategoricalRecord> test) {
        for (String possibleAnswer : possibleAnswers) {
            Map<String, Integer> classesAndClasifications = initializeErrorMatrixRow();
            errorMatrix.add(new ErrorMatrixRow(possibleAnswer,classesAndClasifications));
        }
        int classifiedCorrectlyCount=0;
        for (FlowerCategoricalRecord record : test) {
            String programResult = classifyRecord(record);
            putInTheRightPlaceInErrorMatrix(programResult,record.getFlowerKind());
            if(programResult.equals(record.getFlowerKind())){
                classifiedCorrectlyCount++;
            }
        }
        showErrorMatrix();
        System.out.println( (classifiedCorrectlyCount*1.0)/test.size() *100.0+"%");
    }
    private void showErrorMatrix() {
        // Wyznacz długość maksymalną dla nazw klas, aby uzyskać równy odstęp
        int maxClassNameLength = 0;
        for (ErrorMatrixRow matrixRow : errorMatrix) {
            int length = matrixRow.getKey().length();
            if (length > maxClassNameLength) {
                maxClassNameLength = length;
            }
        }

        // Wyświetl nagłówek macierzy
        System.out.printf("%-" + (maxClassNameLength + 15) + "s", "classified as ->");
        for (ErrorMatrixRow matrixRow : errorMatrix) {
            System.out.printf("%-" + (maxClassNameLength + 3) + "s", matrixRow.getKey());
        }
        System.out.println();

        // Wyświetl zawartość macierzy
        for (ErrorMatrixRow matrixRow : errorMatrix) {
            System.out.printf("%-" + (maxClassNameLength + 15) + "s", matrixRow.getKey());
            for (String keyEntry : matrixRow.getOccurrences().keySet()) {
                System.out.printf("%-" + (maxClassNameLength + 3) + "d", matrixRow.getOccurrences().get(keyEntry));
            }
            System.out.println();
        }
    }

    private double getProbability(List<FlowerCategoricalRecord> recordsWithSuchResult,FlowerCategoricalRecord record) {
        double probability = 1.0;
        for (int i = 0; i < record.getCategoricalAttributes().length; i++) {
            int finalI = i;
            int count = (int) recordsWithSuchResult.
                    stream().
                    filter(r -> r.getCategoricalAttributes()[finalI] == record.getCategoricalAttributes()[finalI])
                    .count();
            double probabilityOfAttribute = (count * 1.0) / recordsWithSuchResult.size();
            if (probabilityOfAttribute == 0) {
                // toDo tu chyba idzie print jak wygladzamy not sure
                probabilityOfAttribute = smoothingIfZero(count, recordsWithSuchResult.size(), possibleAnswers.size());
            }
            probability *= probabilityOfAttribute;
        }
        probability*=(recordsWithSuchResult.size()*1.0)/training.size();
        return probability;
    }
    public String classifyRecord(FlowerCategoricalRecord record) {
        Map<String, Double> probabilitiesOfResults = initializeMap(possibleAnswers);
        for (String possibleAnswer : possibleAnswers) {
            List<FlowerCategoricalRecord> recordsWithSuchResult = training.
                    stream().
                    filter(r -> r.getFlowerKind().equals(possibleAnswer)).
                    toList();
            double probability = getProbability(recordsWithSuchResult, record);
            probabilitiesOfResults.put(possibleAnswer, probability);
        }
        return getMostProbable(probabilitiesOfResults);
    }
    private String getMostProbable(Map<String,Double> probabilitiesOfResults){
        double max=0;
        String result=null;
        for (String resultEntry : probabilitiesOfResults.keySet()) {
            if(max<probabilitiesOfResults.get(resultEntry)){
                max=probabilitiesOfResults.get(resultEntry);
                result=resultEntry;
            }
        }
        return result;
    }
    private double smoothingIfZero(int attributeCount, int decisiveAttributeCount, int amountOfPossibleAnswers){
        return 1.0/(decisiveAttributeCount+amountOfPossibleAnswers);
    }

    private Map<String, Double> initializeMap(Set<String> possibleAnswers) {
        Map<String, Double> probabilitiesOfResults = new HashMap<>();
        for (String possibleAnswer : possibleAnswers) {
            probabilitiesOfResults.put(possibleAnswer, 0.0);
        }
        return probabilitiesOfResults;
    }

}
