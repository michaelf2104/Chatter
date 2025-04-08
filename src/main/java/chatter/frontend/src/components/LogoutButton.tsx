import {Button} from "@mui/material"
import {useNavigate} from "react-router-dom";

function LogoutButton () {
    const navigate = useNavigate();

    function handleLogout() {
        localStorage.removeItem("token");
        navigate("/login");
    }

    return (
        <Button variant="contained" color="secondary" onClick={handleLogout}>
            Logout
        </Button>
    );
}

export default LogoutButton;