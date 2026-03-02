package parsers;

import dto.StudentData;

import java.util.LinkedHashMap;
import java.util.Map;

public class StudentParser implements Parser<StudentData> {
    @Override
    public StudentData parse(String raw){
        Map<String , String> kv = new LinkedHashMap<>();
        String[] parts = raw.split(";");
        for (String part : parts) {
            String[] t = part.split("=" , 2);
            if (t.length == 2) kv.put(t[0].trim(), t[1].trim());
        }

        String name = kv.getOrDefault("name","");
        String email = kv.getOrDefault("email","");
        String phone = kv.getOrDefault("phone","");
        String program = kv.getOrDefault("program","");

        return new StudentData(name, email, phone, program);
    }
}
