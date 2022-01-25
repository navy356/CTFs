
import { Navbar, Nav } from 'react-bootstrap';

export default function MyNavbar() {
    return (
        <Navbar bg="light" expand="lg">
            <Navbar.Brand href="/home">Shatranj</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="mr-auto">
                    <Nav.Link href="/home">Home</Nav.Link>
                    <Nav.Link href="/history">History</Nav.Link>
                    <Nav.Link href="/notes">Strategy Notes</Nav.Link>
                </Nav>
            </Navbar.Collapse>
            <Navbar.Collapse className="justify-content-end">
                <Navbar.Text>
                    <Nav.Link href="/logout">Logout</Nav.Link>
                </Navbar.Text>
            </Navbar.Collapse>
        </Navbar>
    )
};