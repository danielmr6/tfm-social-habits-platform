import { useEffect, useState } from "react";
import { getUsers } from "../../services/usersService";
import { useNavigate } from "react-router-dom";
import styles from "./UsersList.module.css";

export default function UsersList() {

    const [users, setUsers] = useState([]);
    const [search, setSearch] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        loadUsers("");
    }, []);

    async function loadUsers(currentSearch = search) {
        const data = await getUsers(currentSearch, 0, 10);
        setUsers(data.content);
    }

    function getStatusClass(status) {
        if (status === "OK") return styles.ok;
        if (status === "WARNING") return styles.warning;
        return styles.critical;
    }

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>
                Summary of my users
            </h1>

            <p className={styles.subtitle}>
                Overview of activity, status and habit compliance
            </p>

            {/* SEARCH */}
            <div className={styles.searchWrapper}>
                <div className={styles.searchBox}>
                    <input
                        className={styles.input}
                        placeholder="Search users..."
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />

                    <button
                        className={styles.button}
                        onClick={() => loadUsers(search)}
                    >
                        Search
                    </button>
                </div>
            </div>

            {/* TABLE */}
            <div style={{ width: "100%", marginTop: "20px" }}>
                <table className={styles.table}>
                    <thead>
                    <tr className={styles.headerRow}>
                        <th className={styles.th}>First Name</th>
                        <th className={styles.th}>Last Name</th>
                        <th className={styles.th}>Age</th>
                        <th className={styles.th}>Status</th>
                        <th className={styles.th}>Actions</th>
                    </tr>
                    </thead>

                    <tbody>
                    {users.length > 0 ? (
                        users.map((user) => (
                            <tr key={user.id} className={styles.row}>

                                <td className={styles.td}>
                                    {user.firstName}
                                </td>

                                <td className={styles.td}>
                                    {user.lastName}
                                </td>

                                <td className={styles.td}>
                                    {user.age}
                                </td>

                                {/* STATUS */}
                                <td className={styles.td}>
                                        <span className={getStatusClass(user.habitStatus)}>
                                            {user.habitStatus}
                                        </span>
                                    {user.hasRiskyHabitsToday && (
                                        <div className={styles.alert}>
                                            ⚠ Negative habits today
                                        </div>
                                    )}

                                    {user.hasMissingTodayHabits && (
                                        <div className={styles.alert}>
                                            ❌ Today incomplete
                                        </div>
                                    )}
                                </td>

                                <td className={styles.td}>
                                    <button
                                        className={styles.detailsBtn}
                                        onClick={() => navigate(`/users/${user.id}`)}
                                    >
                                        View Details
                                    </button>
                                </td>

                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5" className={styles.empty}>
                                No users found
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}