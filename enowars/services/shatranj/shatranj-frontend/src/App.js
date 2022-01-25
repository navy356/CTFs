
import './App.css';
import { Route, BrowserRouter, Redirect, Switch } from 'react-router-dom';
import Login from './pages/Login/Login';
import Logout from './pages/Logout/Logout';
import Home from './pages/Home/Home';
import GameHistory from './pages/GameHistory/GameHistory';
import Replay from './pages/Replay/Replay';
import Notes from './pages/Notes/Notes';
import PrivateRoute from './components/PrivateRoute';

function App() {
  return (
    <BrowserRouter>
      <Switch>
        <Route path="/login" component={Login} />
        <PrivateRoute path="/home" component={Home} />
        <PrivateRoute path="/history" component={GameHistory} />
        <PrivateRoute path="/replay/:id" component={Replay} />
        <PrivateRoute path="/notes" component={Notes} />
        <PrivateRoute path="/logout" component={Logout} />
        <Redirect from='*' to='/home' />
      </Switch>
    </BrowserRouter>
  );
}

export default App;
