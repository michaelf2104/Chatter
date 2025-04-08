import {Box, Button, Typography, Paper} from "@mui/material";
import {useState} from "react";

function ChatList(){
    const [chats] = useState(["Max Mustermann", "Maxime Musterfrau"]);

    return (
        <Box sx={{width: "30%", height: "100vh", borderRight: "1px solid #ccc", p: 2, backgroundColor: "#f5f5f5"}}>
            <Button variant="contained" fullWidth sx={{mb: 2}}>
                + Neuer Chat
            </Button>
            <Typography variant="h6" gutterBottom>
                Chats
            </Typography>
            {chats.map((chat, index) => (
                <Paper key={index} sx={{p: 1.5, mb: 2, cursor: "pointer", backgroundColor: "#eee", "&:hover": {backgroundColor: "#ddd"},}}>
                    {chat}
                </Paper>
            ))}
        </Box>
    );
}

export default ChatList;
