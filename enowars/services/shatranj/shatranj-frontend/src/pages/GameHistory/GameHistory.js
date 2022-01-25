import './GameHistory.css';
import React, { Component } from 'react';
import { ListGroup } from 'react-bootstrap';
import config from "../../config.json";
import Button from "react-bootstrap/Button";
import MyNavbar from '../../components/MyNavbar';

class GameHistory extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: sessionStorage.getItem('username'),
            password: sessionStorage.getItem('password'),
            games: []
        };

        this.host = config["protocol"] + window.location.hostname + config["route"];

        if (this.state.username && this.state.password) {
            this.fetchGames();
        }
    }

    fetchGames() {
        let url = this.host +  "/pastgames";
        let header = {
            'Authorization': 'Basic ' + Buffer.from(this.state.username + ":" + this.state.password).toString('base64')
        };
        let options = {
            method: 'GET',
            headers: header
        };

        fetch(url, options)
            .then((resp) => {
                console.log(resp)
                if (resp.status !== 200) {
                    if (resp.status == 401) {
                        this.props.history.push('/logout');
                    } else {
                        throw new Error('Failed to fetch past games!');
                    }
                } else {
                    return resp.json();
                }
            })
            .then((response) => {
                console.log(response);
                this.setState({
                    username: this.state.username,
                    password: this.state.password,
                    games: response
                })
            })
            .catch((err) => {
                console.log(err);
            });
    }

    getListItem(game, idx) {
        return (
            <ListGroup horizontal="lg" className="game-entry" key={idx}>
                <ListGroup.Item id="entry-id">{game["id"]}</ListGroup.Item>
                <ListGroup.Item id="entry-name">{game["name"]}</ListGroup.Item>
                <ListGroup.Item id="entry-board">{game["boardState"]}</ListGroup.Item>
                <ListGroup.Item id="entry-winner">{game["winner"]}</ListGroup.Item>
                <ListGroup.Item id="entry-button">
                    <Button block
                        size="sm"
                        type="button"
                        onClick={() => this.viewReplay(game["id"])} >
                        View moves
                    </Button>
                </ListGroup.Item>
            </ListGroup>
        );
    }

    viewReplay(id) {
        this.props.history.push("replay/" + id);
    }

    render() {
        // if (!this.state.username || !this.state.password) {
        //     return (<Redirect to="/login" />)
        // }
        return (
            <div>
                <MyNavbar />
                <div class="game-entries">
                    <ListGroup horizontal="lg" className="game-entry">
                        <ListGroup.Item id="entry-id">Game ID</ListGroup.Item>
                        <ListGroup.Item id="entry-name">Name</ListGroup.Item>
                        <ListGroup.Item id="entry-board">Board State</ListGroup.Item>
                        <ListGroup.Item id="entry-winner">Winner</ListGroup.Item>
                        <ListGroup.Item id="entry-button" />
                    </ListGroup>
                    {this.state.games.map((game, idx) => this.getListItem(game, idx))}
                </div>
            </div>
        )
    }
}

export default GameHistory;