import { Redirect } from 'react-router-dom';

export default function Default() {
    return (
        <Redirect to="/login" />
    );
}