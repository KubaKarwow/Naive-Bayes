import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<FlowerNominalRecord> training = getDataFromFile("Dane/iris_training.txt");
        List<FlowerNominalRecord> test = getDataFromFile("Dane/iris_test.txt");
        List<FlowerCategoricalRecord> transformedTraining = DataTransformer.transform(training);
        List<FlowerCategoricalRecord> transformedTest = DataTransformer.transform(test);
     //   transformedTraining.forEach(System.out::println);
        Classifier classifier = new Classifier(transformedTraining);
        classifier.processTestData(transformedTest);
    }
    public static List<FlowerNominalRecord> getDataFromFile(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(fileName));
        return lines.stream().map(l -> {
            String[] split = l.trim().split("\\s+");
            double[] parsedAttributes = new double[split.length - 1];
            for (int i = 0; i < split.length - 1; i++) {
                parsedAttributes[i] = Double.valueOf(split[i].replaceAll(",", "."));
            }
            return new FlowerNominalRecord(parsedAttributes, split[split.length - 1]);
        }).collect(Collectors.toList());
    }
}