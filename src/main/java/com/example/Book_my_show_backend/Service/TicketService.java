package com.example.Book_my_show_backend.Service;

import com.example.Book_my_show_backend.Dtos.BookTicketRequestDto;
import com.example.Book_my_show_backend.Models.ShowEntity;
import com.example.Book_my_show_backend.Models.ShowSeatEntity;
import com.example.Book_my_show_backend.Models.TicketEntity;
import com.example.Book_my_show_backend.Models.UserEntity;
import com.example.Book_my_show_backend.Repository.ShowRepository;
import com.example.Book_my_show_backend.Repository.TicketRepository;
import com.example.Book_my_show_backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    ShowRepository showRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    // Implementing the book ticket function
    public String bookTicket(BookTicketRequestDto bookTicketRequestDto) throws Exception{

        // Get the requested seats
        List<String> requestedSeats = bookTicketRequestDto.getRequestSeats();

        ShowEntity showEntity = showRepository.findById(bookTicketRequestDto.getShowId()).get();

        UserEntity userEntity = userRepository.findById(bookTicketRequestDto.getUserId()).get();

        //Get the Show Seats
        List<ShowSeatEntity> showSeats = showEntity.getListOfSeats();

        // Validate whether it is possible to allocate the seats
        List<ShowSeatEntity> bookedSeats = new ArrayList<>();
        for(ShowSeatEntity showSeat : showSeats){
            String seatNo = showSeat.getSeatNo();

            if(showSeat.isBooked() == false && requestedSeats.contains(seatNo)){
                bookedSeats.add(showSeat);
            }
        }

        if(bookedSeats.size()!=requestedSeats.size()){
            // Some user requested seats are not available
            throw new Exception("Requested seats are not available");
        }

        // BookedSeats are successful
        TicketEntity ticketEntity = new TicketEntity();

        double totalAmount = 0;
        double multiplier = showEntity.getMultiplier();
        int rate = 0;
        String allotedSeats = "";

        // Calculating the amount
        for(ShowSeatEntity bookedSeat: bookedSeats){

            bookedSeat.setBooked(true);
            bookedSeat.setBookedAt(new Date());
            bookedSeat.setTicket(ticketEntity);
            bookedSeat.setShow(showEntity);

            String seatNo = bookedSeat.getSeatNo();

            allotedSeats = allotedSeats + seatNo + ",";
            if(seatNo.charAt(0)=='1'){
                rate = 100;
            }else{
                rate = 200;
            }

            totalAmount = totalAmount + multiplier*rate;


        }

        ticketEntity.setBooked_at(new Date());
        ticketEntity.setAmount((int)totalAmount);
        ticketEntity.setShow(showEntity);
        ticketEntity.setUser(userEntity);
        ticketEntity.setBookedSeats(bookedSeats);
        ticketEntity.setAlloted_seats(allotedSeats);

        ticketRepository.save(ticketEntity);
        return "Successfully created ticket";
    }
}
