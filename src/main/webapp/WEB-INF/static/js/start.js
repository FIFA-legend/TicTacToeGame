let move = "E";
let opponentMove = "E";
let isMoveAllowed = 0;
let game = null;
let username = null;

let url = '/secured/room';
let sock = new SockJS(url);
let stomp = Stomp.over(sock);
stomp.connect({}, function (frame) {
    stomp.subscribe("/user/queue/opponent/recover", processInterruptedGame);
    stomp.subscribe("/user/queue/opponent/connect", processGameConnection);
    stomp.subscribe("/user/queue/opponent/create", processGameCreation);
    stomp.subscribe("/user/queue/opponent/name", processOpponentName);
    stomp.subscribe("/user/queue/opponent/move", processOpponentMove);
});

function processInterruptedGame(income) {
    if (income.body === "NONE") {
        stomp.send("/spring-security-mvc-socket/game/connect");
    } else {
        username = getUsername();
        game = JSON.parse(income.body);
        if (game["creatorUsername"] === username) {
            document.getElementById("opponent_id").innerHTML = game["opponentUsername"];
            move = "X";
            opponentMove = "O";
        } else {
            document.getElementById("opponent_id").innerHTML = game["creatorUsername"];
            move = "O";
            opponentMove = "X";
        }
        redrawCell(0, "first_cell");
        redrawCell(1, "second_cell");
        redrawCell(2, "third_cell");
        redrawCell(3, "forth_cell");
        redrawCell(4, "fifth_cell");
        redrawCell(5, "sixth_cell");
        redrawCell(6, "seventh_cell");
        redrawCell(7, "eight_cell");
        redrawCell(8, "ninth_cell");
        if (game["status"] === "MOVE") {
            isMoveAllowed = 1;
        }
    }
}

function redrawCell(index, elementId) {
    if (game["moves"][index] != null) {
        let element = document.getElementById(elementId);
        if (game["moves"][index] === move) {
            element.innerHTML = move;
            element.classList.add("blue");
        } else if (game["moves"][index] === opponentMove) {
            element.innerText = opponentMove;
            element.classList.add("red");
        }
    }
}

function getUsername() {
    let tag = document.getElementById("user_id");
    return tag.innerHTML;
}

function processGameConnection(income) {
    if (income.body === "NONE") {
        stomp.send("/spring-security-mvc-socket/game/create");
    } else {
        game = JSON.parse(income.body);
        game["opponentUsername"] = getUsername();
        move = "O";
        opponentMove = "X";
        document.getElementById("opponent_id").innerHTML = game["creatorUsername"];
        stomp.send("/spring-security-mvc-socket/game/opponent/update", {}, JSON.stringify(game));
    }
}

function processOpponentName(income) {
    game = JSON.parse(income.body);
    let opponentTag = document.getElementById("opponent_id");
    if (game["status"] === "MOVE") {
        isMoveAllowed = 1;
    }
    opponentTag.innerHTML = game["opponentUsername"];
}

function processGameCreation(income) {
    game = JSON.parse(income.body);
    move = "X";
    opponentMove = "O";
}

function processOpponentMove(income) {
    let g = JSON.parse(income.body);
    let moveIndex = -1;
    for (let i = 0; i < 9; i++) {
        if (g["moves"][i] !== game["moves"][i]) {
            moveIndex = i;
            break;
        }
    }
    game = g;
    let text = null;
    if (game["status"] === "MOVE") {
        isMoveAllowed = 1;
    } else if (game["status"] === "WIN") {
        text = "YOU WIN!!!";
    } else if (game["status"] === "LOSE") {
        text = "LOSER :(";
    } else if (game["status"] === "DRAW") {
        text = "DRAW";
    }
    if (text != null) {
        let span = document.getElementById("result_id");
        let div = document.getElementById("block_id");
        let button = document.getElementById("button");
        span.innerHTML = text;
        span.classList.add("green");
        div.classList.add("result_show");
        button.classList.remove("return_button");
    }
    let tag = null;
    switch (moveIndex) {
        case 0:
            tag = document.getElementById("first_cell");
            break;
        case 1:
            tag = document.getElementById("second_cell");
            break;
        case 2:
            tag = document.getElementById("third_cell");
            break;
        case 3:
            tag = document.getElementById("forth_cell");
            break;
        case 4:
            tag = document.getElementById("fifth_cell");
            break;
        case 5:
            tag = document.getElementById("sixth_cell");
            break;
        case 6:
            tag = document.getElementById("seventh_cell");
            break;
        case 7:
            tag = document.getElementById("eight_cell");
            break;
        case 8:
            tag = document.getElementById("ninth_cell");
            break;
    }
    if (tag != null) {
        tag.innerHTML = opponentMove;
        tag.classList.add("red");
    }
    console.log(income);
}

setTimeout(function () {
    let firstCell = document.getElementById("first_cell");
    firstCell.addEventListener("click", function () {
        if (isMoveAllowed && game["moves"][0] == null) {
            isMoveAllowed = 0;
            firstCell.innerHTML = move;
            firstCell.classList.add("blue");
            game["moves"][0] = move;
            stomp.send("/spring-security-mvc-socket/game/move", {}, JSON.stringify(game));
        }
    })

    let secondCell = document.getElementById("second_cell");
    secondCell.addEventListener("click", function () {
        if (isMoveAllowed && game["moves"][1] == null) {
            isMoveAllowed = 0;
            secondCell.innerHTML = move;
            secondCell.classList.add("blue");
            game["moves"][1] = move;
            stomp.send("/spring-security-mvc-socket/game/move", {}, JSON.stringify(game));
        }
    })

    let thirdCell = document.getElementById("third_cell");
    thirdCell.addEventListener("click", function () {
        if (isMoveAllowed && game["moves"][2] == null) {
            isMoveAllowed = 0;
            thirdCell.innerHTML = move;
            thirdCell.classList.add("blue");
            game["moves"][2] = move;
            stomp.send("/spring-security-mvc-socket/game/move", {}, JSON.stringify(game));
        }
    })

    let forthCell = document.getElementById("forth_cell");
    forthCell.addEventListener("click", function () {
        if (isMoveAllowed && game["moves"][3] == null) {
            isMoveAllowed = 0;
            forthCell.innerHTML = move;
            forthCell.classList.add("blue");
            game["moves"][3] = move;
            stomp.send("/spring-security-mvc-socket/game/move", {}, JSON.stringify(game));
        }
    })

    let fifthCell = document.getElementById("fifth_cell");
    fifthCell.addEventListener("click", function () {
        if (isMoveAllowed && game["moves"][4] == null) {
            isMoveAllowed = 0;
            fifthCell.innerHTML = move;
            fifthCell.classList.add("blue");
            game["moves"][4] = move;
            stomp.send("/spring-security-mvc-socket/game/move", {}, JSON.stringify(game));
        }
    })

    let sixthCell = document.getElementById("sixth_cell");
    sixthCell.addEventListener("click", function () {
        if (isMoveAllowed && game["moves"][5] == null) {
            isMoveAllowed = 0;
            sixthCell.innerHTML = move;
            sixthCell.classList.add("blue");
            game["moves"][5] = move;
            stomp.send("/spring-security-mvc-socket/game/move", {}, JSON.stringify(game));
        }
    })

    let seventhCell = document.getElementById("seventh_cell");
    seventhCell.addEventListener("click", function () {
        if (isMoveAllowed && game["moves"][6] == null) {
            isMoveAllowed = 0;
            seventhCell.innerHTML = move;
            seventhCell.classList.add("blue");
            game["moves"][6] = move;
            stomp.send("/spring-security-mvc-socket/game/move", {}, JSON.stringify(game));
        }
    })

    let eightCell = document.getElementById("eight_cell");
    eightCell.addEventListener("click", function () {
        if (isMoveAllowed && game["moves"][7] == null) {
            isMoveAllowed = 0;
            eightCell.innerHTML = move;
            eightCell.classList.add("blue");
            game["moves"][7] = move;
            stomp.send("/spring-security-mvc-socket/game/move", {}, JSON.stringify(game));
        }
    })

    let ninthCell = document.getElementById("ninth_cell");
    ninthCell.addEventListener("click", function () {
        if (isMoveAllowed && game["moves"][8] == null) {
            isMoveAllowed = 0;
            ninthCell.innerHTML = move;
            ninthCell.classList.add("blue");
            game["moves"][8] = move;
            stomp.send("/spring-security-mvc-socket/game/move", {}, JSON.stringify(game));
        }
    })

    stomp.send("/spring-security-mvc-socket/game/recover");

}, 500);