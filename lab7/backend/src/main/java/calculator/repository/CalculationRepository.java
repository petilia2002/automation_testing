package calculator.repository;

import calculator.entity.Calculation;
import calculator.entity.NumberSystem;
import calculator.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {

    // Основной метод с фильтрацией
    List<Calculation> findByCalculationDateBetweenAndOperationTypeAndFirstNumberSystemAndSecondNumberSystem(
            LocalDateTime start,
            LocalDateTime end,
            OperationType operationType,
            NumberSystem firstSystem,
            NumberSystem secondSystem
    );

    // Метод для поиска по диапазону дат (без фильтров)
    List<Calculation> findByCalculationDateBetween(LocalDateTime start, LocalDateTime end);

    // Дополнительные методы для гибкости
    List<Calculation> findByOperationTypeAndCalculationDateBetween(
            OperationType operationType, LocalDateTime start, LocalDateTime end);

    List<Calculation> findByFirstNumberSystemAndCalculationDateBetween(
            NumberSystem firstNumberSystem, LocalDateTime start, LocalDateTime end);

    List<Calculation> findBySecondNumberSystemAndCalculationDateBetween(
            NumberSystem secondNumberSystem, LocalDateTime start, LocalDateTime end);

    // Кастомный запрос для сложной фильтрации
    // Для случая когда даты не указаны (все записи)
    @Query("SELECT c FROM Calculation c WHERE " +
            "(:operationType IS NULL OR c.operationType = :operationType) AND " +
            "(:firstSystem IS NULL OR c.firstNumberSystem = :firstSystem) AND " +
            "(:secondSystem IS NULL OR c.secondNumberSystem = :secondSystem)")
    List<Calculation> findAllWithOptionalFilters(
            @Param("operationType") OperationType operationType,
            @Param("firstSystem") NumberSystem firstSystem,
            @Param("secondSystem") NumberSystem secondSystem);

    // Для случая когда даты указаны
    @Query("SELECT c FROM Calculation c WHERE " +
            "c.calculationDate BETWEEN :start AND :end AND " +
            "(:operationType IS NULL OR c.operationType = :operationType) AND " +
            "(:firstSystem IS NULL OR c.firstNumberSystem = :firstSystem) AND " +
            "(:secondSystem IS NULL OR c.secondNumberSystem = :secondSystem)")
    List<Calculation> findByDateRangeWithOptionalFilters(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("operationType") OperationType operationType,
            @Param("firstSystem") NumberSystem firstSystem,
            @Param("secondSystem") NumberSystem secondSystem);
}