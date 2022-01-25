import React, { Component } from 'react';
import MyNavbar from '../../components/MyNavbar';
import AuthService from '../../services/AuthService';
import { Redirect } from 'react-router-dom';

class Logout extends Component {
    constructor(props) {
        super(props);

        AuthService.logout();
        this.state = {
            isLoggedOut: true
        };
    }

    render() {
        return this.state.isLoggedOut ? 
            (<Redirect to='/login' />) :
            (<div>
                <MyNavbar />
            </div>);
    }
}

export default Logout;