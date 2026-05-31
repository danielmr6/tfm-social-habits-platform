import { useState } from "react";

export default function ForgotPassword() {

    const [email, setEmail] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();

        // llamar API
        console.log("Send recovery mail to:", email);
    };

    return (
        <div className="auth-page">

            <h2>Recuperar contraseña</h2>

            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Tu correo"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <button type="submit">
                    Enviar enlace
                </button>
            </form>

        </div>
    );
}