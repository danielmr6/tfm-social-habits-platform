import { Routes, Route, Navigate } from "react-router-dom";

import MainLayout from "../components/layout/MainLayout";
import ProtectedRoute from "../components/shared/ProtectedRoute";

import Login from "../pages/auth/Login";
import ForgotPassword from "../pages/auth/ForgotPassword";

import UsersList from "../pages/users/UsersList";
import UserCreate from "../pages/users/UserCreate";

function RootRedirect() {
    const isAuth = !!localStorage.getItem("token");

    return isAuth ? (
        <Navigate to="/users" replace />
    ) : (
        <Navigate to="/login" replace />
    );
}

export default function AppRouter() {
    return (
        <Routes>
            <Route path="/" element={<RootRedirect />} />
            <Route path="/login" element={<Login />} />
            <Route path="/forgot-password" element={<ForgotPassword />} />

            <Route element={<ProtectedRoute />}>
                <Route element={<MainLayout />}>
                    <Route path="/users" element={<UsersList />} />
                    <Route path="/users/new" element={<UserCreate />} />
                </Route>
            </Route>

            {/* fallback */}
            <Route path="*" element={<Navigate to="/" replace />} />

        </Routes>
    );
}