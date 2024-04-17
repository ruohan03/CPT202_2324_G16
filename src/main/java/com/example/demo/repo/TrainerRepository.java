package com.example.demo.repo;

import com.example.demo.Model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("fc_appointment_system/trainer")
public interface TrainerRepository extends JpaRepository<Trainer,String> {

    List<Trainer> findTrainersByCourseId(String courseId);

}
