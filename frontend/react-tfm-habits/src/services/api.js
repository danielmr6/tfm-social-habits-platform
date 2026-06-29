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

    const url = config.url || "";

    // normalize: remove query params
    const cleanPath = url.split("?")[0];

    const isPublic = [
        "/auth/login",
        "/auth/register",
        "/auth/forgot-password",
        "/auth/reset-password"
    ].some(endpoint => cleanPath.includes(endpoint));

    if (!isPublic) {

        const token = localStorage.getItem("token");

        if (token && token !== "null" && token !== "undefined") {
            config.headers = config.headers || {};
            config.headers.Authorization = `Bearer ${token}`;
        }
    }

    return config;
});

export default api;