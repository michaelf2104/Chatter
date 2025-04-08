import {Box, Button, Typography, Grid, Paper} from "@mui/material";
import {useNavigate} from "react-router-dom";
import ChatList from "../components/ChatList";
import ChatWindow from "../components/ChatWindow";

function Dashboard() {
    const navigate = useNavigate();

    function handleLogout() {
        navigate("/login");
    }

    return (
        <Box sx={{display: "flex", height: "100vh"}}>
            <ChatList />
            <Box sx={{position: "absolute", top: 10, right: 10}}>
                <Button variant="contained" color="secondary" onClick={handleLogout}>
                    Logout
                </Button>
            </Box>
            <ChatWindow />
        </Box>
    );
}

export default Dashboard;
