import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import {
    getUserById,
    deleteUser,
    downloadUserReport
} from "../../services/usersService";

import {
    deleteHabit
} from "../../services/habitservice";

import {
    createObservation,
    deleteObservation
} from "../../services/observationservice";

import styles from "./UserDetail.module.css";

export default function UserDetail() {

    const navigate = useNavigate();
    const { id } = useParams();
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    const [deleting, setDeleting] = useState(false);

    const [showDeleteModal, setShowDeleteModal] =
        useState(false);

    const [selectedHabit, setSelectedHabit] =
        useState(null);

    const [selectedObservation, setSelectedObservation] =
        useState(null);

    const [observationText, setObservationText] =
        useState("");

    useEffect(() => {
        loadUser();
    }, [id]);

    async function handleDownloadReport() {

        try {

            const blob = await downloadUserReport(id);

            // IMPORTANT: verify it's really a PDF
            const url = window.URL.createObjectURL(
                new Blob([blob], { type: "application/pdf" })
            );

            const a = document.createElement("a");
            a.href = url;
            a.download = `user-report-${id}.pdf`;

            document.body.appendChild(a);
            a.click();
            a.remove();

            window.URL.revokeObjectURL(url);

        } catch (err) {

            console.error(err);

            alert("Error generating report");

        }
    }

    async function loadUser() {

        try {

            setLoading(true);

            const data =
                await getUserById(id);

            setUser(data);

        } catch {

            setUser(null);

        } finally {

            setLoading(false);

        }

    }

    function getStatusClass(status) {
        switch (status) {
            case "OK":
                return styles.ok;
            case "WARNING":
                return styles.warning;
            case "CRITICAL":
                return styles.critical;
            default:
                return "";
        }
    }

    function handleDeleteUser() {
        setSelectedHabit(null);
        setShowDeleteModal(true);
    }

    async function handleCreateObservation(){

        if(!observationText.trim())
            return;

        try{

            await createObservation(
                id,
                observationText
            );

            setObservationText("");

            await loadUser();

        }catch{

            alert(
                "Error creating observation"
            );

        }

    }

    function handleDeleteObservation(
        observationId
    ){

        setSelectedHabit(null);

        setSelectedObservation(
            observationId
        );

        setShowDeleteModal(true);

    }

    async function confirmDelete() {

        try {

            setDeleting(true);

            if(selectedHabit){

                await deleteHabit(
                    selectedHabit
                );

                await loadUser();

            }else if(selectedObservation){

                await deleteObservation(
                    selectedObservation
                );

                await loadUser();

            }else{

                await deleteUser(id);

                navigate("/users");

            }

        } catch(err){

            console.error(err);

            alert(
                "Delete failed"
            );

        } finally {

            setDeleting(false);

            setShowDeleteModal(false);

            setSelectedHabit(null);

            setSelectedObservation(null);
        }
    }

    function handleDeleteHabit(habitId) {
        setSelectedHabit(habitId);
        setShowDeleteModal(true);
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
            <button

                className={styles.editBtn}

                onClick={
                    handleDownloadReport
                }

            >

                Generate Report

            </button>
            {/* MODAL */}
            {showDeleteModal && (
                <div className={styles.modalOverlay}>
                    <div className={styles.confirmCard}>

                        <h2>
                            {
                                selectedHabit
                                    ? "Delete Habit"
                                    : selectedObservation
                                        ? "Delete Observation"
                                        : "Delete User"
                            }
                        </h2>

                        <p>
                            {
                                selectedHabit
                                    ? (
                                        "Are you sure you want to delete this habit?"
                                    )

                                    : selectedObservation

                                        ? (
                                            "Are you sure you want to delete this observation?"
                                        )

                                        : (

                                            <>
                                                Are you sure you want to delete{" "}
                                                <strong>

                                                    {user.firstName}
                                                    {" "}
                                                    {user.lastName}

                                                </strong>

                                                ?

                                            </>

                                        )
                            }
                        </p>

                        <div className={styles.modalActions}>

                            <button
                                onClick={() => {

                                    setShowDeleteModal(false);

                                    setSelectedHabit(null);

                                    setSelectedObservation(null);

                                }}
                                className={styles.cancelBtn}
                            >
                                Cancel
                            </button>

                            <button
                                className={styles.deleteBtnDanger}
                                onClick={confirmDelete}
                                disabled={deleting}
                            >
                                {deleting ? "Deleting..." : "Delete"}
                            </button>

                        </div>
                    </div>
                </div>
            )}

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
                        Edit User
                    </button>

                    <button
                        className={styles.deleteBtn}
                        onClick={handleDeleteUser}
                    >
                        Delete User
                    </button>

                    <button
                        className={styles.editBtn}
                        onClick={() => navigate(`/users/${id}/habits/new`)}
                    >
                        Add Habit
                    </button>

                </div>
            </div>

            {/* STATUS */}
            {user.habitStatus && (
                <div className={`${styles.statusBanner} ${getStatusClass(user.habitStatus)}`}>

                    <div>
                        {user.habitStatus === "OK" && "🟢 Good habit compliance"}
                        {user.habitStatus === "WARNING" && "🟡 Irregular habits detected"}
                        {user.habitStatus === "CRITICAL" && "🔴 Attention: missing habits"}
                    </div>

                    {user.hasRiskyHabitsToday && (
                        <div className={styles.alert}>
                            ⚠ Negative habits today
                        </div>
                    )}

                    {user.hasMissingTodayHabits && (
                        <div className={styles.alert}>
                            ❌ Today habits not completed
                        </div>
                    )}

                </div>
            )}

            {/* INFO */}
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

                <div className={styles.sectionHeader}>
                    <h3>Habits</h3>

                    <span className={styles.globalStatus}>
                        Status: {user.habitStatus}
                    </span>
                </div>

                {user.habits?.length > 0 ? (
                    <div className={styles.grid}>

                        {user.habits.map((h) => (
                            <div key={h.id} className={styles.habitCard}>

                                <div className={styles.badge}>
                                    {h.type}
                                </div>

                                <p><b>Status:</b> {h.status}</p>

                                <p>
                                    <b>Date:</b>{" "}
                                    {h.date
                                        ? new Date(h.date).toLocaleDateString()
                                        : "-"}
                                </p>

                                <p>{h.description}</p>

                                <button
                                    className={styles.deleteHabitBtn}
                                    onClick={() => handleDeleteHabit(h.id)}
                                >
                                    Delete Habit
                                </button>

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

                <div className={styles.observationForm}>

                    <textarea
                        value={observationText}
                        placeholder="Add observation..."
                        onChange={(e) =>
                            setObservationText(
                                e.target.value
                            )
                        }
                    />

                    <button
                        className={styles.addObservationBtn}
                        onClick={handleCreateObservation}
                    >
                        Add Observation
                    </button>

                </div>

                {user.observations?.length > 0 ? (

                    <div className={styles.timeline}>

                        {user.observations.map(o=>(

                            <div
                                key={o.id}
                                className={styles.obsItem}
                            >

                                <div className={styles.obsHeader}>

                                    <span>

                                        {
                                            o.professionalName
                                        }

                                    </span>

                                    <small>

                                        {
                                            new Date(
                                                o.createdAt
                                            ).toLocaleDateString()
                                        }

                                    </small>

                                </div>

                                <p>

                                    {o.content}

                                </p>

                                <button

                                    className={styles.deleteObservationBtn}

                                    onClick={() => handleDeleteObservation(
                                        o.id
                                    )}

                                >

                                    Remove Observation

                                </button>

                            </div>

                        ))}

                    </div>

                ) : (

                    <p>

                        No observations

                    </p>

                )}

            </div>

        </div>
    );
}