import { useState } from "react";
import api from "../../services/api";

export default function ForgotPassword() {

    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    async function handleSubmit(e) {

        e.preventDefault();

        setMessage("");
        setError("");

        try {

            await api.post("/auth/forgot-password", {
                email
            });

            setMessage("Recovery email sent successfully");

        } catch (err) {

            console.log("FULL ERROR:", err);
            console.log("STATUS:", err.response?.status);
            console.log("DATA:", err.response?.data);

            const backendMessage =
                err.response?.data?.message ||
                err.response?.data?.error ||
                err.response?.data ||
                null;

            if (err.response?.status === 404) {

                setError("Email not registered");

            } else if (backendMessage) {

                setError(backendMessage);

            } else {

                setError("Cannot connect to server");
            }
        }
    }

    return (

        <div style={{ maxWidth: "400px", margin: "40px auto" }}>

            <h1>Recover Password</h1>

            <form onSubmit={handleSubmit} style={{ display: "flex", gap: "10px" }}>

                <input
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Email"
                    style={{ flex: 1, padding: "8px" }}
                />

                <button type="submit">
                    Send link
                </button>

            </form>

            {message && (
                <p style={{ color: "green" }}>
                    {message}
                </p>
            )}

            {error && (
                <p style={{ color: "red" }}>
                    {error}
                </p>
            )}

        </div>
    );
}