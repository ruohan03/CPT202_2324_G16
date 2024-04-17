package com.example.demo.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.Model.Trainer;
import com.example.demo.Service.impl.TrainerServiceImpl;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repo.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    public void testFindTrainersByCourseIdReturnsTrainers() {
        // Arrange
        String courseId = "C001";
        Trainer trainer1 = new Trainer("T001", "Soonfei", 17792972, "Male", 3, 1000, courseId);
        Trainer trainer2 = new Trainer("T002", "John", 222782782, "Male", 4, 10000, courseId);
        when(trainerRepository.findByCourseId(courseId)).thenReturn(Arrays.asList(trainer1, trainer2));

        // Act
        List<Trainer> result = trainerService.findTrainersByCourseId(courseId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(trainer1));
        assertTrue(result.contains(trainer2));
        verify(trainerRepository).findByCourseId(courseId);
    }

    @Test
    void testFindTrainersByCourseIdThrowsExceptionWhenNoTrainersFound() {
        // Arrange
        String courseId = "C999";
        when(trainerRepository.findByCourseId(courseId)).thenReturn(Collections.emptyList());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> trainerService.findTrainersByCourseId(courseId),
                "Expected findTrainersByCourseId to throw ResourceNotFoundException when no trainers found");
    }
}
