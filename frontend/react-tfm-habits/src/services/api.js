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

    const url = config.url?.split("?")[0] || "";

    const isAuthEndpoint = url.startsWith("/auth/");

    if (isAuthEndpoint) {
        delete config.headers?.Authorization;
        return config;
    }

    const token = localStorage.getItem("token");

    if (token && token !== "null" && token !== "undefined") {
        config.headers = config.headers || {};
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

export default api;