package com.example.demo.repo;

import com.example.demo.Model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("superhero/Trainer")
public interface demoRepository extends JpaRepository<Trainer,Long> {

}
