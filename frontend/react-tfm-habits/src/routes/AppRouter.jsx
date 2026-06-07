import { Routes, Route, Navigate } from "react-router-dom";

import MainLayout from "../components/layout/MainLayout";
import ProtectedRoute from "../components/shared/ProtectedRoute";

import Login from "../pages/auth/Login";
import ForgotPassword from "../pages/auth/ForgotPassword";
import ResetPassword from "../pages/auth/ResetPassword";
import Register from "../pages/auth/Register";

import UsersList from "../pages/users/UsersList";
import UserCreate from "../pages/users/UserCreate";
import UserDetail from "../pages/users/UserDetail";
import UserEdit from "../pages/users/UserEdit";

import CreateHabit from "../pages/habits/CreateHabit";

function RootRedirect() {

    const isAuth =
        !!localStorage.getItem("token");

    return isAuth
        ? <Navigate to="/users" replace />
        : <Navigate to="/login" replace />;

}

export default function AppRouter() {

    return (

        <Routes>

            <Route
                path="/"
                element={<RootRedirect />}
            />

            <Route
                path="/login"
                element={<Login />}
            />

            <Route
                path="/forgot-password"
                element={<ForgotPassword />}
            />

            <Route
                path="/reset-password"
                element={<ResetPassword />}
            />

            <Route
                path="/register"
                element={<Register />}
            />

            <Route element={<ProtectedRoute />}>

                <Route element={<MainLayout />}>

                    <Route
                        path="/users"
                        element={<UsersList />}
                    />

                    <Route
                        path="/users/new"
                        element={<UserCreate />}
                    />

                    <Route
                        path="/users/:id"
                        element={<UserDetail />}
                    />

                    <Route
                        path="/users/:id/edit"
                        element={<UserEdit />}
                    />

                    {/* NEW ROUTE */}

                    <Route
                        path="/users/:id/habits/new"
                        element={<CreateHabit />}
                    />

                </Route>

            </Route>

            <Route
                path="*"
                element={<Navigate to="/" replace />}
            />

        </Routes>

    );

}