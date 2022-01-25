class AuthService {
    static isLoggedIn() {
        return sessionStorage.getItem('username') && sessionStorage.getItem('password');
    }

    static login(username, password) {
        sessionStorage.setItem('username', username);
        sessionStorage.setItem('password', password);
    }
    
    static logout() {
        sessionStorage.removeItem('username');
        sessionStorage.removeItem('password');
    }
}

export default AuthService;