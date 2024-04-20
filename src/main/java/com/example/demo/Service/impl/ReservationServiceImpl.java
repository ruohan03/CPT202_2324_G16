package com.example.demo.Service.impl;
import com.example.demo.Model.Reservation;
import com.example.demo.Service.ReservationService;
import com.example.demo.repo.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

    @Service
    public class ReservationServiceImpl implements ReservationService {
        private ReservationRepository reservationRepository;

        @Autowired
        public ReservationServiceImpl(ReservationRepository reservationRepository) {
            this.reservationRepository = reservationRepository;
        }
        @Override
        public List<Reservation> getAllReservations() {
            return reservationRepository.findAll();
        }

    }

