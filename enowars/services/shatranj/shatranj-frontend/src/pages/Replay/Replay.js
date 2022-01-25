import './Replay.css';
import React, { Component } from 'react';
import { ListGroup } from 'react-bootstrap';
import config from "../../config.json";
import MyNavbar from '../../components/MyNavbar';

class Replay extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: sessionStorage.getItem('username'),
            password: sessionStorage.getItem('password'),
            moves: []
        };

        this.host = config["protocol"] + window.location.hostname + config["route"];

        if (this.state.username && this.state.password) {
            this.fetchMoves();
        }
    }

    fetchMoves() {
        let url = this.host + "/replay/" + this.props.match.params.id;
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
                        throw new Error('Failed to fetch moves for that game!');
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
                    moves: response
                })
            })
            .catch((err) => {
                console.log(err);
            });
    }

    getListItem(move, idx) {
        return (
            <ListGroup horizontal="lg" className="game-move" key={idx}>
                <ListGroup.Item id="move-state-before">{move["boardStateBefore"]}</ListGroup.Item>
                <ListGroup.Item id="move-white">{move["whiteMove"]}</ListGroup.Item>
                <ListGroup.Item id="move-black">{move["blackMove"]}</ListGroup.Item>
            </ListGroup>
        )
    }

    viewReplay(id) {
        window.history.push({
            pathname: "replay/" + id
        });
    }

    render() {
        // if (!this.state.username || !this.state.password) {
        //     return (<Redirect to="/login" />)
        // }
        return (
            <div>
                <MyNavbar />
                <div class="move-entries">
                    <ListGroup horizontal="lg" className="game-move">
                        <ListGroup.Item id="move-state-before">Board before move:</ListGroup.Item>
                        <ListGroup.Item id="move-white">White move:</ListGroup.Item>
                        <ListGroup.Item id="move-black">Black move:</ListGroup.Item>
                    </ListGroup>
                    {this.state.moves.map((move, idx) => this.getListItem(move, idx))}
                </div>
            </div>
        )
    }
}

export default Replay;