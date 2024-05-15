import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Classifier {
    private List<FlowerCategoricalRecord> training;
    private Set<String> possibleAnswers;


    public Classifier(List<FlowerCategoricalRecord> training) {
        this.training = training;
        possibleAnswers=getAllPossibleAnswers();
    }

    private Set<String> getAllPossibleAnswers() {
        return training.stream().map(FlowerCategoricalRecord::getFlowerKind).collect(Collectors.toSet());
    }

    public void processTestData(List<FlowerCategoricalRecord> test) {
        int classifiedCorrectlyCount=0;
        for (FlowerCategoricalRecord record : test) {
            String programResult = classifyRecord(record);
            if(programResult.equals(record.getFlowerKind())){
                classifiedCorrectlyCount++;
            }
        }
        System.out.println( (classifiedCorrectlyCount*1.0)/test.size() *100.0+"%");
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
