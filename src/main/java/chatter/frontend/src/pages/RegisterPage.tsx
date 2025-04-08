import{useState} from "react";
import{Box, TextField, Button, Typography, Grid} from "@mui/material";
import{useNavigate} from "react-router-dom";
import logo from "../content/logo.png"

function Register() {
    const [phoneNumber, setPhoneNumber] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    async function handleRegister() {
        if (!firstName || !lastName || password.length < 6 || phoneNumber.length < 10) {
            if (phoneNumber.length < 10) {
                setErrorMessage("Please insert valid phone number!");
                return;
            } else if (phoneNumber.length >= 10) {
                setErrorMessage("Please fill all fields!");
                return;
            }
        }

        if (password !== confirmPassword) {
            setErrorMessage("Password do not match!");
            return;
        }

        // api calls to the backend
        try {
            const response = await fetch("/api/auth/register", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({firstName, lastName, phoneNumber, password}),
            });

            if (response.ok) {
                navigate("/login");
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.message || "Registration failed");
            }
        } catch (error) {
            setErrorMessage("Server not available");
        }
    }

    return (
        <Grid container justifyContent="center" alignItems="center" sx={{height: "100vh", backgroundColor: "#abd3c9"}}>
            <Box sx={{backgroundColor: "#fff", p: 4, borderRadius: 2, boxShadow: 3, width: "320px"}}>
                <Box sx={{ display: "flex", justifyContent: "center", mb:2 }}>
                    <img src={logo} alt="Chatter Logo" style={{ height: 100 }} />
                </Box>
                <Typography variant="h5" fontWeight="bold" textAlign="center" gutterBottom>
                    Register
                </Typography>
                {errorMessage && <Typography color="error" textAlign="center">{errorMessage}</Typography>}
                <TextField
                    label="First name"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                />
                <TextField
                    label="Last name"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                />
                <TextField
                    label="Phone number"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={phoneNumber}
                    onChange={(e) => setPhoneNumber(e.target.value)}
                />
                <TextField
                    label="Password"
                    type="password"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <TextField
                    label="Verify password"
                    type="password"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                />
                <Button variant="contained" color="primary" fullWidth sx={{mt: 2}} onClick={handleRegister}>
                    Register
                </Button>
                <Typography variant="body2" textAlign="center" sx={{mt: 2}}>
                    Already have an account?{" "}
                    <span
                        style={{color: "#673AB7", cursor: "pointer", fontWeight: "bold"}}
                        onClick={() => navigate("/login")}
                    >
                        Login here
                    </span>
                </Typography>
            </Box>
        </Grid>
    );
}

export default Register;