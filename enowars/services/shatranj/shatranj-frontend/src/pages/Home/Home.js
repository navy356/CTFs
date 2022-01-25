import './Home.css';
import React, { Component } from 'react';
import Chessboard from 'chessboardjsx';
import config from "../../config.json";
import Button from "react-bootstrap/Button";
import MyNavbar from '../../components/MyNavbar';
import maharajah from './maharajah.png';
import king from './king.png';
import queen from './queen.png';
import bishop from './bishop.png';
import knight from './knight.png';
import rook from './rook.png';
import pawn from './pawn.png';

class Home extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: sessionStorage.getItem('username'),
            password: sessionStorage.getItem('password'),
            position: "rnbqkbnr/pppppppp/8/8/8/8/8/4K3 w kq - 0 1"
        };

        this.host = config["protocol"] + window.location.hostname + config["route"];

        if (this.state.username && this.state.password) {
            this.initializeBoard();
        }
    }

    makeid(length) {
        var result = [];
        var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        var charactersLength = characters.length;
        for (var i = 0; i < length; i++) {
            result.push(characters.charAt(Math.floor(Math.random() *
                charactersLength)));
        }
        return result.join('');
    }

    initializeBoard() {
        let url = this.host + "/initialize";
        let header = {
            'Authorization': 'Basic ' + Buffer.from(this.state.username + ":" + this.state.password).toString('base64')
        };
        let options = {
            method: 'POST',
            headers: header,
            body: this.makeid(5)
        };

        fetch(url, options)
            .then((resp) => {
                console.log(resp)
                if (resp.status !== 200) {
                    if (resp.status == 401) {
                        this.props.history.push('/logout');
                    } else {
                        throw new Error('Failed to initialize game!');
                    }
                } else {
                    return resp.json()
                }
            })
            .then((response) => {
                console.log(response);
                console.log("Board state: " + response["board_state"])
                this.setState({
                    username: this.state.username,
                    password: this.state.password,
                    position: response["board_state"]
                })
            })
            .catch((err) => {
                console.log(err);
            });
    }

    onDrop(move) {
        let url = this.host + "/move";
        let header = {
            'Authorization': 'Basic ' + Buffer.from(this.state.username + ":" + this.state.password).toString('base64')
        };
        let options = {
            method: 'POST',
            headers: header,
            body: "\"move\": \"" + move.sourceSquare + move.targetSquare + "\""
        };

        fetch(url, options)
            .then((resp) => {
                console.log(resp)
                if (resp.status !== 200) {
                    if (resp.status == 401) {
                        this.props.history.push('/logout');
                    } else {
                        throw new Error('Failed to make move!');
                    }
                } else {
                    return resp.json();
                }
            })
            .then((response) => {
                if (!response["message"].includes("successfully")) {
                    throw new Error('Invalid chess move!');
                }

                this.setState({
                    username: this.state.username,
                    password: this.state.password,
                    position: response["board_state"]
                });

                if (response["message"].includes("mate")) {
                    alert(response["message"]);
                }
            })
            .catch((err) => {
                alert(err);
            });
    }

    allowDrag(piece) {
        if (piece.piece.substring(0, 1) == "w") {
            return true;
        } else {
            return false;
        }
    }

    render() {
        console.log("Current state position: " + this.state.position);
        // if (!this.state.username || !this.state.password) {
        //     return (<Redirect to="/login" />)
        // }
        return (
            <div>
                <MyNavbar />
                <div class="explanation">
                    <div id="explanation-content">
                        Welcome to Shatranj! The pandemic has already reached into the highest ranks 
                        of society. Neither the average layman nor the rich and powerful seem to be 
                        safe from the effects of the virus. The kingdom has laid out an extensive plan 
                        to vaccinate each and every citizen in an effort to stop the dangerous virus.
                        However, the king is desperately trying to avoid getting vaccinated himself, as he 
                        thinks a vaccinated population will provide enough protection around him. You are 
                        playing as the corona virus, and as such you want to teach this guy a lesson to 
                        stop hiding behind his people!
                        <br /><br />
                    </div>
                    <h5 id="explanation-header">Rules</h5>
                    <div id="explanation-content">
                        The rules of this game are taken from a chess variant called "Maharajah and the sepoys"
                        (or "Shatranj Diwana Shah"). The white side has a full set of normal chess pieces that 
                        abide by normal rules. However, there is no pawn promotion.
                        <br /><br />
                        The corona virus ("maharajah") can move either like a queen or like a knight. As it is 
                        the only piece of its side, it cannot perform a move that leaves the virus in check.
                        <br /><br />
                        The game ends either when the corona virus manages to capture the king or when the 
                        white side manages to contain the virus by checkmating it. Start the game by dragging 
                        the virus to make a move.
                    </div>
                </div>
                <div class="content">
                    <Chessboard 
                        boardStyle={{
                            margin: ".5em",
                            alignSelf: "center"
                        }}
                        position={this.state.position}
                        draggable={true}
                        allowDrag={this.allowDrag.bind(this)}
                        undo={false}
                        onDrop={this.onDrop.bind(this)}
                        pieces={{
                            wK: ({ squareWidth, isDragging}) => (
                                <img
                                    style={{
                                        width: squareWidth,
                                        height: squareWidth
                                    }}
                                    src={maharajah}
                                    alt={"maharajah"}
                                />
                            ),
                            bK: ({ squareWidth, isDragging }) => (
                                <img
                                    style={{
                                        width: squareWidth,
                                        height: squareWidth
                                    }}
                                    src={king}
                                    alt={"king"}
                                />
                            ),
                            bQ: ({ squareWidth, isDragging }) => (
                                <img
                                    style={{
                                        width: squareWidth,
                                        height: squareWidth
                                    }}
                                    src={queen}
                                    alt={"queen"}
                                />
                            ),
                            bB: ({ squareWidth, isDragging }) => (
                                <img
                                    style={{
                                        width: squareWidth,
                                        height: squareWidth
                                    }}
                                    src={bishop}
                                    alt={"bishop"}
                                />
                            ),
                            bN: ({ squareWidth, isDragging }) => (
                                <img
                                    style={{
                                        width: squareWidth,
                                        height: squareWidth
                                    }}
                                    src={knight}
                                    alt={"knight"}
                                />
                            ),
                            bR: ({ squareWidth, isDragging }) => (
                                <img
                                    style={{
                                        width: squareWidth,
                                        height: squareWidth
                                    }}
                                    src={rook}
                                    alt={"rook"}
                                />
                            ),
                            bP: ({ squareWidth, isDragging }) => (
                                <img
                                    style={{
                                        width: squareWidth,
                                        height: squareWidth
                                    }}
                                    src={pawn}
                                    alt={"pawn"}
                                />
                            )
                        }} />
                    <Button block
                        size="sm"
                        type="button"
                        onClick={this.initializeBoard.bind(this)}
                        id="newgamebutton" >
                        New Game
                    </Button>
                </div>
            </div>
        )
    }
}

export default Home;