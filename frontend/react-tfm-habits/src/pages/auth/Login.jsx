import { useState, useContext } from "react";
import { login } from "../../services/authservice";
import { AuthContext } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Login() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const { loginUser } = useContext(AuthContext);

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {

            const data = await login(email, password);

            loginUser(data.token);

            navigate("/dashboard");

        } catch (error) {
            console.log(error);
            alert("Credenciales incorrectas");

        }
    };

    return (
        <div className="login-page">
            <h1>SocialHabits</h1>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Correo electrónico"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="Contraseña"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button type="submit">
                    Iniciar sesión
                </button>

            </form>
        </div>
    );
}