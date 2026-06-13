import { useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import api from "../../services/api";

export default function ResetPassword() {

    const [searchParams] = useSearchParams();
    const token = searchParams.get("token");

    const navigate = useNavigate();

    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

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
        <div style={{ maxWidth: "400px", margin: "40px auto" }}>

            <h1>Reset Password</h1>

            <form
                onSubmit={handleSubmit}
                style={{
                    display: "flex",
                    flexDirection: "column",
                    gap: "10px"
                }}
            >

                <input
                    type="password"
                    placeholder="New password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    style={{ padding: "8px" }}
                />

                <input
                    type="password"
                    placeholder="Confirm password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    style={{ padding: "8px" }}
                />

                <button type="submit">
                    Reset password
                </button>

            </form>

            {message && <p style={{ color: "green" }}>{message}</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}

        </div>
    );
}