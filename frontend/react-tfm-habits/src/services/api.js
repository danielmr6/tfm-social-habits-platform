import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
});

const publicEndpoints = [
    "/auth/login",
    "/auth/register",
    "/auth/forgot-password",
    "/auth/reset-password"
];

api.interceptors.request.use((config) => {

    const isPublic =
        publicEndpoints.some(
            endpoint =>
                config.url?.startsWith(endpoint)
        );

    if (!isPublic) {

        const token =
            localStorage.getItem("token");

        if (token) {

            config.headers.Authorization =
                `Bearer ${token}`;

        }

    }

    return config;

});

export default api;