package by.bsuir.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Game {

    private static long counter = 0;

    private long id;

    private String creatorUsername;

    private String opponentUsername;

    private Move[] moves;

    private Status status;

    public Game(String creatorUsername) {
        this.id = ++counter;
        this.creatorUsername = creatorUsername;
        this.moves = new Move[9];
        this.status = Status.WAIT;
    }
}
