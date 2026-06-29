import axios from "axios";

const api = axios.create({
    baseURL: process.env.REACT_APP_API_URL,
});

// Endpoints públicos
const publicEndpoints = new Set([
    "/auth/login",
    "/auth/register",
    "/auth/forgot-password",
    "/auth/reset-password"
]);

api.interceptors.request.use((config) => {

    const path = config.url || "";

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