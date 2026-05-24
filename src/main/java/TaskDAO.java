import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TaskDAO {
    void save(Task task);

    List<Task> findAll();

    Optional<Task> findById(int id);

    void delete(int id);

    void update(Task task);

    void updateStatus(int id, Status status);

}
