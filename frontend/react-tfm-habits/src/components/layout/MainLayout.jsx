import {
    Outlet,
    Link,
    useLocation,
    useNavigate
} from "react-router-dom";

import styles from "./MainLayout.module.css";

export default function MainLayout() {

    const location = useLocation();

    const navigate = useNavigate();

    const hideFab =
        location.pathname === "/users/new";

    function logout() {

        // Remove token/session data
        localStorage.removeItem("token");

        // Optional:
        sessionStorage.clear();

        // Redirect to login
        navigate("/login");

    }

    return (

        <div className={styles.container}>
            <header className={styles.header}>

                <Link
                    to="/users"
                    className={styles.brand}
                >
                    Social<span>Habits</span>
                </Link>

                <div className={styles.headerActions}>

                    <button
                        className={styles.headerBtn}
                        onClick={() => navigate("/users")}
                    >
                        Home
                    </button>

                    <button
                        className={styles.headerBtn}
                        onClick={logout}
                    >
                        Logout
                    </button>

                </div>

            </header>
            <div className={styles.body}>
                <aside className={styles.sidebar}>
                </aside>

                <main className={styles.content}>
                    <Outlet />
                </main>

            </div>

            {!hideFab && (
                <Link to="/users/new" className={styles.leftFab}>
                    <span>+</span>
                    <span>Add User</span>
                </Link>
            )}

        </div>

    );

}