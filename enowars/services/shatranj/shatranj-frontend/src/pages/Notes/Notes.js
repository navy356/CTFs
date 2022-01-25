import './Notes.css';
import React, { Component } from 'react';
import MyNavbar from '../../components/MyNavbar';
import { ListGroup } from 'react-bootstrap';
import config from "../../config.json";
import Button from "react-bootstrap/Button";
import FormControl from "react-bootstrap/Form";
import InputGroup from 'react-bootstrap/InputGroup';

class Notes extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: sessionStorage.getItem('username'),
            password: sessionStorage.getItem('password'),
            notes: [],
            addedNote: "",
        };

        this.host = config["protocol"] + window.location.hostname + config["route"];

        if (this.state.username && this.state.password) {
            this.fetchNotes();
        }
    }

    fetchNotes() {
        let url = this.host + "/strategynotes";
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
                        throw new Error('Failed to fetch strategy notes!');
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
                    notes: response,
                    addedNote: this.state.addedNote
                });
            })
            .catch((err) => {
                console.log(err);
            });
    }

    getListItem(note, idx) {
        return (
            <ListGroup horizontal="lg" className="strategy-note" key={idx}>
                <ListGroup.Item id="note-idx">{idx}</ListGroup.Item>
                <ListGroup.Item id="note-message">{note["message"]}</ListGroup.Item>
            </ListGroup>
        );
    }

    postNote() {
        let url = this.host + "/strategynote";
        let header = {
            'Authorization': 'Basic ' + Buffer.from(this.state.username + ":" + this.state.password).toString('base64')
        };
        let options = {
            method: 'POST',
            headers: header,
            body: this.state.addedNote
        };

        fetch(url, options)
            .then((resp) => {
                console.log(resp)
                if (resp.status !== 200) {
                    throw new Error('Failed to post strategy note!');
                } else {
                    return resp.json();
                }
            })
            .then((response) => {
                console.log(response);
                this.fetchNotes();
            })
            .catch((err) => {
                console.log(err);
            });
    }

    setAddedNote(value) {
        this.setState({
            username: this.state.username,
            password: this.state.password,
            notes: this.state.notes,
            addedNote: value,
        });
    }

    render() {
        return (
            <div>
                <MyNavbar />
                <div className="note-content">
                    <ListGroup horizontal="lg" className="strategy-note">
                        <ListGroup.Item id="note-idx">Pos</ListGroup.Item>
                        <ListGroup.Item id="note-message">Note</ListGroup.Item>
                    </ListGroup>
                    {this.state.notes.map((note, idx) => this.getListItem(note, idx))}
                    <InputGroup className="add-note">
                        <FormControl
                            id="post-note-control"
                            placeholder="Add a note"
                            aria-label="Add a note"
                            aria-describedby="basic-addon2"
                            as="input"
                            value={this.addedNote}
                            onChange={(e) => this.setAddedNote(e.target.value)}
                        />
                        <InputGroup.Append
                            id="post-note-button">
                            <Button 
                                variant="outline-secondary"
                                onClick={() => this.postNote()}>
                                    Add
                            </Button>
                        </InputGroup.Append>
                    </InputGroup>
                </div>
            </div>
        )
    }
}

export default Notes;