import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
});

// Endpoints públicos
const publicEndpoints = new Set([
    "/auth/login",
    "/auth/register",
    "/auth/forgot-password",
    "/auth/reset-password"
]);

api.interceptors.request.use((config) => {

    const url = config.url || "";

    // normalizar URL (quita dominio si viene completo)
    const path = url.replace("http://localhost:8080", "");

    const isPublic = Array.from(publicEndpoints).some(endpoint =>
        path.startsWith(endpoint)
    );

    if (!isPublic) {

        const token = localStorage.getItem("token");

        if (token) {
            config.headers = config.headers || {};
            config.headers.Authorization = `Bearer ${token}`;
        }

    }

    return config;
});

export default api;