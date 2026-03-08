package parsers;

public interface Parser <T>{
    T parse(String raw);
}
