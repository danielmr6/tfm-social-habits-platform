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

                <div>

                    SocialHabits

                </div>

                <button
                    onClick={logout}
                    style={{
                        padding: "8px 14px",
                        cursor: "pointer"
                    }}
                >

                    Logout

                </button>

            </header>
            <div className={styles.body}>

                <aside className={styles.sidebar}>

                    <Link to="/users">

                        Users

                    </Link>

                </aside>

                <main className={styles.content}>

                    <Outlet />

                </main>

            </div>

            {!hideFab && (

                <Link
                    to="/users/new"
                    className={styles.fab}
                >

                    <span>+</span>

                    <span>

                        Add New User

                    </span>

                </Link>

            )}

        </div>

    );

}