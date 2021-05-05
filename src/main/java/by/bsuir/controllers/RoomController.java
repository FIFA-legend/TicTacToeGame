package by.bsuir.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {

    @GetMapping("/my_room")
    public String myRoom() {
        return "my_room";
    }

}
