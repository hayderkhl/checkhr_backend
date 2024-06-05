package com.example.checkhrbackend_v2.repository;

import com.example.checkhrbackend_v2.model.Leaves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leaves, Long> {

    public List<Leaves> findByStatusIsNull();

    @Query("SELECT l FROM Leaves l WHERE l.user.id_user = :userId")
    List<Leaves> findByUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM Leaves l WHERE l.user.id_user = :userId AND YEAR(l.date) = :year")
    List<Leaves> findLeavesByUserIdAndYear(Long userId, int year);

//    @Query("SELECT l.user.FullName FROM Leaves l WHERE l.user.id_user = :userId")
//    String findUserFullNameByUserId(@Param("userId") Long userId);

    @Query("SELECT l.user.FullName FROM Leaves l WHERE l.user.id_user = :userId")
    List<String> findUserFullNamesByUserId(@Param("userId") Long userId);
}
