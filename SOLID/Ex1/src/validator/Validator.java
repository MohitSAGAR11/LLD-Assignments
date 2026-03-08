package validator;

import dto.StudentData;

import java.util.List;

public interface Validator<T> {
    List<String> validate(T data);
}
