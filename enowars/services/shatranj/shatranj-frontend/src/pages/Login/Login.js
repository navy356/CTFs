import React, { useState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import "./Login.css";
import { useHistory } from "react-router-dom";
import config from "../../config.json";

/**
 * Simple function component containing a login form
 */
export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const history = useHistory();

    /**
     * just check whether the input fields contain text,
     * email format is automatically validated by React Forms
     */
    function validateForm() {
        return username.length > 0 && password.length > 0;
    }

    /**
     * Send the login request and redirect to home page
     * @param {*} event The submit event
     */
    function handleSubmit(event) {
        // prevent refresh
        event.preventDefault();

        console.log(event);

        let host = config["protocol"] + window.location.hostname + config["route"];

        if (event.nativeEvent.submitter.name === "login") {
            let url = host + "/login";
            let options = {
                method: 'GET',
                headers: {
                    'Authorization': 'Basic ' + Buffer.from(username + ":" + password).toString('base64')
                }
            };

            fetch(url, options)
                .then((resp) => {
                    if (resp.status !== 200) {
                        throw new Error('Credentials not found!');
                    } else {
                        return resp.json();
                    }
                })
                .then((response) => {
                    console.log("success")
                    sessionStorage.setItem('username', username);
                    sessionStorage.setItem('password', password);
                    history.push({
                        pathname: "home" 
                    });
                })
                .catch((err) => {
                    alert(err);
                });
        } else if (event.nativeEvent.submitter.name === "register") {
            let url = host + "/register";
            let options = {
                method: 'POST',
                body: JSON.stringify({
                    username: username,
                    password: password
                }),
                headers: {
                    'Content-Type': 'application/json'
                }
            };

            fetch(url, options)
                .then((resp) => {
                    if (resp.status !== 200) {
                        throw new Error('Failed to register!');
                    } else {
                        return resp.json();
                    }
                })
                .then((response) => {
                    console.log("success");
                    alert("Successfully registered user " + username + ". Your user will expire after 30 minutes.");
                })
                .catch((err) => {
                    alert(err);
                });
        }

    }

    return (
        <div className="Login">
            <Form onSubmit={handleSubmit}>
                <Form.Group size="lg" controlId="username">
                    <Form.Label>Username</Form.Label>
                    <Form.Control
                        autoFocus
                        type="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </Form.Group>
                <Form.Group size="lg" controlId="password">
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </Form.Group>
                <Button block size="lg" type="submit" name="login" disabled={!validateForm()}>
                    Login
                </Button>
                <Button block size="lg" type="submit" name="register" disabled={!validateForm()}>
                    Register
                </Button>
            </Form>
        </div>
    );
}
