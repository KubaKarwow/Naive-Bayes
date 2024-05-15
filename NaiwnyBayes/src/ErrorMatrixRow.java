import java.util.List;
import java.util.Map;

public class ErrorMatrixRow {
    private String key;
    private Map<String,Integer> occurrences;

    public ErrorMatrixRow(String key, Map<String, Integer> occurrences) {
        this.key = key;
        this.occurrences = occurrences;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setOccurrences(Map<String, Integer> occurrences) {
        this.occurrences = occurrences;
    }

    public Map<String, Integer> getOccurrences() {
        return occurrences;
    }



}
