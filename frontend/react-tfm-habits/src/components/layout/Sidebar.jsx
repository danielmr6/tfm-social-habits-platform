import { NavLink } from "react-router-dom";

export default function Sidebar() {
    return (
        <aside className="w-64 bg-white border-r p-4">

            <nav className="flex flex-col gap-2">

                <NavLink to="/dashboard">Dashboard</NavLink>

                <p className="text-xs text-gray-400 mt-4">USUARIOS</p>
                <NavLink to="/users">Listado</NavLink>
                <NavLink to="/users/new">Nuevo usuario</NavLink>

                <p className="text-xs text-gray-400 mt-4">SEGUIMIENTO</p>
                <NavLink to="/habits/register">Registrar hábitos</NavLink>
                <NavLink to="/habits/history">Historial</NavLink>

                <p className="text-xs text-gray-400 mt-4">ANÁLISIS</p>
                <NavLink to="/alerts">Alertas</NavLink>
                <NavLink to="/reports">Informes</NavLink>

            </nav>
        </aside>
    );
}