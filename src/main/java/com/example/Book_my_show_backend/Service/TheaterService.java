package com.example.Book_my_show_backend.Service;


import com.example.Book_my_show_backend.Dtos.TheaterRequestDto;
import com.example.Book_my_show_backend.Enums.SeatType;
import com.example.Book_my_show_backend.Models.TheaterEntity;
import com.example.Book_my_show_backend.Models.TheaterSeatEntity;
import com.example.Book_my_show_backend.Repository.TheaterRepository;
import com.example.Book_my_show_backend.Repository.TheaterSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TheaterService {

    @Autowired
    TheaterSeatRepository theaterSeatRepository;

    @Autowired
    TheaterRepository theaterRepository;

    // Implementing addTheater function and Get API
    public String createTheater(TheaterRequestDto theaterRequestDto){

        TheaterEntity theater = TheaterEntity.builder().city(theaterRequestDto.getCity()).name(theaterRequestDto.getName()).address(theaterRequestDto.getAddress()).build();

        List<TheaterSeatEntity> theaterSeats = createTheaterSeats();


        theater.setTheaterSeatEntityList(theaterSeats); //Bidirectional mapping


        //For each theater Seat : we need to set the theaterEntity
        for(TheaterSeatEntity theaterSeat : theaterSeats){
            theaterSeat.setTheater(theater);
        }

        theaterRepository.save(theater);

        return "Theater added successfully";

    }

    private List<TheaterSeatEntity> createTheaterSeats(){


        List<TheaterSeatEntity> seats = new ArrayList<>();

        //Optimize by adding loop
        for(int i=0;i<5;i++){

            char ch = (char)('A'+i);

            String seatNo  = "1"+ ch;
            TheaterSeatEntity theaterSeat = new TheaterSeatEntity(seatNo,SeatType.CLASSIC,100);
            seats.add(theaterSeat);
        }
        for(int i=0;i<5;i++){
            char ch = (char)('A'+i);
            String seatNo  = "2"+ ch;
            TheaterSeatEntity theaterSeat = new TheaterSeatEntity(seatNo,SeatType.PLATINUM,200);
            seats.add(theaterSeat);
        }

        theaterSeatRepository.saveAll(seats);

        return seats;

    }



}