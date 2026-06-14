import { useState, useEffect } from "react";
import { useNavigate, useSearchParams, useLocation } from "react-router-dom";
import api from "../../services/api";

export default function ResetPassword() {

    const [searchParams] = useSearchParams();
    const location = useLocation();
    const navigate = useNavigate();

    // 🔥 token viene del state (después de limpiar URL)
    const tokenFromState = location.state?.token;

    // 🔥 token inicial viene de la URL
    const tokenFromUrl = searchParams.get("token");

    const token = tokenFromState || tokenFromUrl;

    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    // 🔥 limpiar URL inmediatamente (UX limpia)
    useEffect(() => {
        if (tokenFromUrl) {
            navigate("/reset-password", {
                replace: true,
                state: { token: tokenFromUrl }
            });
        }
    }, [tokenFromUrl, navigate]);

    function validate() {
        if (!password) return "Password is required";

        if (password.length < 8 || password.length > 50)
            return "Password must be between 8 and 50 characters";

        if (password !== confirmPassword)
            return "Passwords do not match";

        return null;
    }

    async function handleSubmit(e) {
        e.preventDefault();

        setMessage("");
        setError("");

        const validationError = validate();
        if (validationError) {
            setError(validationError);
            return;
        }

        try {
            await api.post("/auth/reset-password", {
                token,
                newPassword: password
            });

            setMessage("Password updated successfully");

            setTimeout(() => {
                navigate("/login");
            }, 2000);

        } catch (err) {
            const backendMessage =
                err.response?.data?.message ||
                err.response?.data?.error ||
                "Error resetting password";

            setError(backendMessage);
        }
    }

    return (
        <div style={containerStyle}>
            <h1>Reset Password</h1>

            <form onSubmit={handleSubmit} style={formStyle}>

                <input
                    type="password"
                    placeholder="New password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    style={inputStyle}
                />

                <input
                    type="password"
                    placeholder="Confirm password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    style={inputStyle}
                />

                <button type="submit" style={buttonStyle}>
                    Reset password
                </button>

            </form>

            {message && <p style={{ color: "green" }}>{message}</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}
        </div>
    );
}

const containerStyle = {
    maxWidth: "400px",
    margin: "80px auto",
    padding: "30px",
    border: "1px solid #e5e7eb",
    borderRadius: "12px",
    boxShadow: "0 4px 20px rgba(0,0,0,0.08)",
    background: "white"
};

const formStyle = {
    display: "flex",
    flexDirection: "column",
    gap: "12px"
};

const inputStyle = {
    padding: "10px",
    border: "1px solid #ccc",
    borderRadius: "6px"
};

const buttonStyle = {
    padding: "10px",
    background: "#2563eb",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer"
};