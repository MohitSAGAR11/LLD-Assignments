package repository;

import entities.StudentRecord;

public interface StudentRepository {
    void save(StudentRecord record);

    int count();
}
