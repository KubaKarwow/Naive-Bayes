import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
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
        while(true){
            System.out.println("Do you want to, classify your own vector? type 'Yes' or 'No'");
            Scanner scanner = new Scanner(System.in);
            String userIN = scanner.next();
            if(userIN.equals("Yes")){
                int amountOfAtrributes = transformedTraining.get(0).getCategoricalAttributes().length;
                double[] ownAttributes= new double[amountOfAtrributes];
                for (int i = 0; i < amountOfAtrributes; i++) {
                    System.out.print("insert next attribute: ");
                    ownAttributes[i]=scanner.nextDouble();
                }
                FlowerNominalRecord flowerNominalRecord = new FlowerNominalRecord(ownAttributes, "");
                FlowerCategoricalRecord flowerCategoricalRecord = DataTransformer.transformSingleRecord(flowerNominalRecord,training);
                String result = classifier.classifyRecord(flowerCategoricalRecord);
                System.out.println("This flower is classified to: " + result);

            } else if (userIN.equals("No")){
                break;
            } else{
                System.out.println("wrong input");
            }
        }
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