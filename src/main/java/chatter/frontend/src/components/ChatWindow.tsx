import { Box, Typography } from "@mui/material";
import logo from "../content/logo.png";

function ChatWindow() {
    return (
        <Box sx={{ flexGrow: 1, p: 4, position: "relative" }}>
            <Typography variant="h5" gutterBottom>
                Willkommen im Chat
            </Typography>
            <Typography>Wähle einen Chat aus, um zu beginnen.</Typography>

            <Box
                component="img"
                src={logo}
                alt="Chatter Logo"
                sx={{
                    position: "absolute",
                    bottom: 40,
                    right: 40,
                    height: 160,
                    opacity: 0.08,
                    filter: "grayscale(100%)",
                    pointerEvents: "none",
                }}
            />
        </Box>
    );
}

export default ChatWindow;
