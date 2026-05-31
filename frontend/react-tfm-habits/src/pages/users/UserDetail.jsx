import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import {
    getUserById,
    deleteUser
} from "../../services/usersService";

import styles from "./UserDetail.module.css";

export default function UserDetail() {

    const { id } = useParams();
    const navigate = useNavigate();

    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [deleting, setDeleting] = useState(false);

    useEffect(() => {
        loadUser();
    }, [id]);

    async function loadUser() {
        try {
            setLoading(true);
            const data = await getUserById(id);
            setUser(data);
        } catch (err) {
            console.error(err);
            setUser(null);
        } finally {
            setLoading(false);
        }
    }

    async function handleDelete() {

        const confirmDelete = window.confirm(
            "Are you sure you want to delete this user?"
        );

        if (!confirmDelete) return;

        try {
            setDeleting(true);

            await deleteUser(id);

            navigate("/users");

        } catch (err) {
            alert("Error deleting user");
            console.error(err);
        } finally {
            setDeleting(false);
        }
    }

    if (loading) {
        return (
            <div className={styles.center}>
                <p>Loading user...</p>
            </div>
        );
    }

    if (!user) {
        return (
            <div className={styles.center}>
                <p className={styles.error}>User not found</p>
            </div>
        );
    }

    return (
        <div className={styles.container}>

            {/* HEADER */}
            <div className={styles.header}>

                <div>
                    <h1 className={styles.title}>
                        {user.firstName} {user.lastName}
                    </h1>

                    <p className={styles.subtitle}>
                        User details & activity overview
                    </p>
                </div>

                <div className={styles.actions}>

                    <button
                        className={styles.editBtn}
                        onClick={() => navigate(`/users/${id}/edit`)}
                    >
                        Edit
                    </button>

                    <button
                        className={styles.deleteBtn}
                        onClick={handleDelete}
                        disabled={deleting}
                    >
                        {deleting ? "Deleting..." : "Delete"}
                    </button>

                </div>
            </div>

            {/* INFO CARD */}
            <div className={styles.card}>

                <div className={styles.infoRow}>
                    <span>Age</span>
                    <strong>{user.age}</strong>
                </div>

                <div className={styles.infoRow}>
                    <span>Phone</span>
                    <strong>{user.phoneNumber || "-"}</strong>
                </div>

                <div className={styles.infoRow}>
                    <span>General notes</span>
                    <p>{user.generalObservations || "No observations"}</p>
                </div>

            </div>

            {/* HABITS */}
            <div className={styles.section}>
                <h3>Habits</h3>

                {user.habits?.length > 0 ? (
                    <div className={styles.grid}>

                        {user.habits.map((h) => (
                            <div key={h.id} className={styles.habitCard}>

                                <div className={styles.badge}>
                                    {h.type}
                                </div>

                                <div>
                                    <p><b>Status:</b> {h.status}</p>
                                    <p>{h.description}</p>
                                </div>

                            </div>
                        ))}

                    </div>
                ) : (
                    <p className={styles.empty}>
                        No habits registered
                    </p>
                )}
            </div>

            {/* OBSERVATIONS */}
            <div className={styles.section}>
                <h3>Observations</h3>

                {user.observations?.length > 0 ? (
                    <div className={styles.timeline}>

                        {user.observations.map((o) => (
                            <div key={o.id} className={styles.obsItem}>

                                <div className={styles.obsHeader}>
                                    <span>{o.professionalName}</span>
                                    <small>
                                        {new Date(o.createdAt).toLocaleDateString()}
                                    </small>
                                </div>

                                <p>{o.content}</p>

                            </div>
                        ))}

                    </div>
                ) : (
                    <p className={styles.empty}>
                        No observations
                    </p>
                )}
            </div>

        </div>
    );
}