import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import {
    getUserById,
    updateUser
} from "../../services/usersService";

import styles from "./UserEdit.module.css";

export default function UserEdit() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [form, setForm] = useState({
        firstName: "",
        lastName: "",
        age: ""
    });

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        loadUser();
    }, [id]);

    async function loadUser() {
        try {
            setLoading(true);

            const data = await getUserById(id);

            setForm({
                firstName: data.firstName || "",
                lastName: data.lastName || "",
                age: data.age || ""
            });

        } catch (e) {
            setError("Error loading user");
        } finally {
            setLoading(false);
        }
    }

    function handleChange(e) {
        const { name, value } = e.target;

        setForm((prev) => ({
            ...prev,
            [name]: value
        }));
    }

    function validate() {
        if (!form.firstName.trim()) return "First name is required";
        if (!form.lastName.trim()) return "Last name is required";

        if (!form.age || isNaN(form.age)) {
            return "Age must be a number";
        }

        if (form.age < 0 || form.age > 120) {
            return "Age is not valid";
        }

        return null;
    }

    async function handleSubmit(e) {
        e.preventDefault();

        const validationError = validate();

        if (validationError) {
            setError(validationError);
            return;
        }

        try {
            setSaving(true);
            setError("");

            await updateUser(
                id,
                form
            );

            navigate(`/users/${id}`);

        } catch (e) {
            setError("Error saving user");
        } finally {
            setSaving(false);
        }
    }

    if (loading) return <p>Loading...</p>;

    return (
        <div className={styles.container}>
            <h1>Edit user</h1>

            {error && (
                <p className={styles.error}>{error}</p>
            )}

            <form onSubmit={handleSubmit} className={styles.form}>

                <input
                    name="firstName"
                    value={form.firstName}
                    onChange={handleChange}
                    placeholder="First name"
                />

                <input
                    name="lastName"
                    value={form.lastName}
                    onChange={handleChange}
                    placeholder="Last name"
                />

                <input
                    name="age"
                    value={form.age}
                    onChange={handleChange}
                    placeholder="Age"
                    type="number"
                />

                <button
                    type="submit"
                    disabled={saving}
                >
                    {saving ? "Saving..." : "Save changes"}
                </button>

            </form>
        </div>
    );
}