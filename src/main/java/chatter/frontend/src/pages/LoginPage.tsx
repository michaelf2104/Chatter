import {useEffect, useState} from "react";
import {Box, TextField, Button, Typography, Grid} from "@mui/material";
import {useNavigate} from "react-router-dom";
import logo from "../content/logo.png";

function Login() {
    const [phoneNumber, setPhoneNumber] = useState("");
    const [code, setCode] = useState("");
    const [codeSent, setCodeSent] = useState(false);
    const [resendTimer, setResendTimer] = useState(30);
    const [canResend, setCanResend] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    async function handleRequestCode() {
        if (phoneNumber.length < 10) {
            alert("Please enter a valid phone number");
            return;
        }
        try {
            const response = await fetch("/api/auth/login", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({phoneNumber}),
            });

            if (response.ok) {
                console.log("SMS-Code sent to:", phoneNumber);
                setCodeSent(true);
                setCanResend(false);
                setResendTimer(30);
            } else {
                const data = await response.json();
                setErrorMessage(data.message || "Failed to send code.");
            }
        } catch (error) {
            setErrorMessage("Server not available.");
        }
    }

    async function handleResendCode() {
        try {
            const response = await fetch("/api/auth/resend-code", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({phoneNumber}),
            });

            if (response.ok) {
                console.log("New code sent to:", phoneNumber);
                setCanResend(false);
                setResendTimer(30);
            } else {
                setErrorMessage("Failed to resend code.");
            }
        } catch (error) {
            setErrorMessage("Server not available.");
        }
    }

    async function handleLogin() {
        if (code.length !== 5) {
            alert("Please insert the full code");
            return;
        }
        try {
            const response = await fetch("/api/auth/verify-code", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({phoneNumber, code}),
            });

            if (response.ok) {
                navigate("/dashboard");
            } else {
                setErrorMessage("Invalid verification code.");
            }
        } catch (error) {
            setErrorMessage("Server not available");
        }
    }

    // timer
    useEffect(() => {
        if (codeSent && resendTimer > 0) {
            const interval = setInterval(() => {
                setResendTimer((prev) => prev - 1);
            }, 1000);
            return () => clearInterval(interval);
        } else {
            setCanResend(true);
        }
    }, [codeSent, resendTimer]);

    return (
        <Grid container justifyContent="center" alignItems="center" sx={{height: "100vh", backgroundColor: "#abd3c9"}}>
            <Box sx={{backgroundColor: "#fff", p: 4, borderRadius: 2, boxShadow: 3, width: "320px"}}>
                <Box sx={{ display: "flex", justifyContent: "center", mb:2 }}>
                    <img src={logo} alt="Chatter Logo" style={{ height: 100 }} />
                </Box>
                <Typography variant="h5" fontWeight="bold" textAlign="center" gutterBottom>
                    Login
                </Typography>
                {!codeSent ? (
                    <>
                        <TextField
                            label="Phone number"
                            variant="outlined"
                            fullWidth
                            margin="normal"
                            value={phoneNumber}
                            onChange={(e) => setPhoneNumber(e.target.value)}
                        />
                        <Button variant="contained" color="primary" fullWidth onClick={handleRequestCode}>
                            Request code
                        </Button>
                    </>
                ) : (
                    <>
                        <Typography sx={{backgroundColor: "#E8F5E9", p: 1, borderRadius: 1, mb: 2, textAlign: "center"}}>
                            Code has been sent to {phoneNumber}.
                        </Typography>
                        <Box display="flex" justifyContent="center" gap={1}>
                            {Array.from({length: 5}).map((_, i) => (
                                <TextField
                                    key={i}
                                    variant="outlined"
                                    inputProps={{maxLength: 1, style: {textAlign: "center"}}}
                                    sx={{width: "40px"}}
                                    onChange={(e) => {
                                        const newCode = code.split("");
                                        newCode[i] = e.target.value;
                                        setCode(newCode.join(""));
                                    }}
                                />
                            ))}
                        </Box>
                        <Button variant="contained" color="primary" fullWidth sx={{mt: 2}} onClick={handleLogin}>
                            Login
                        </Button>
                        <Button
                            variant="text"
                            color="secondary"
                            fullWidth
                            sx={{mt: 1}}
                            onClick={handleResendCode}
                            disabled={!canResend}
                        >
                            {canResend ? "Resend code" : `wait ${resendTimer}s`}
                        </Button>
                    </>
                )}
                <Typography variant="body2" textAlign="center" sx={{mt: 2}}>
                    Not registered?{" "}
                    <span
                        style={{color: "#673AB7", cursor: "pointer", fontWeight: "bold"}}
                        onClick={() => navigate("/register")}
                    >
                        Register here
                    </span>
                </Typography>
            </Box>
        </Grid>
    );
}

export default Login;
