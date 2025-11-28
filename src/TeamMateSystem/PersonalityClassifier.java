package TeamMateSystem;

import java.util.ArrayList;
import java.util.List;

public class PersonalityClassifier {
    List<String> result;
    int total;

    public PersonalityClassifier(ArrayList<String> result) {
        this.result = result;
    }

    public int calculateScore(){
        total = 0;
        for (int i = 0; i < 5; i++) {
            total += Integer.parseInt(result.get(i));
        }
        total *= 4;
        return total;
    }

    public String classify(){
        if (total >= 90){
            return "Leader";
        }
        else if (total >= 70){
            return "Balanced";
        }
        else if (total >= 50){
            return "Thinker";
        }
        else{
            return null;
        }
    }
}
