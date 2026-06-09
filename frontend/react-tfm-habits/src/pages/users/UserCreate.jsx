import { useState } from "react";
import { createUser } from "../../services/usersService";
import { useNavigate } from "react-router-dom";

export default function UserCreate() {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        firstName: "",
        lastName: "",
        age: "",
        phoneNumber: "",
        generalObservations: ""
    });

    const [errors, setErrors] = useState({});

    function validate(values) {
        const newErrors = {};

        // FIRST NAME
        if (!values.firstName.trim()) {
            newErrors.firstName = "First name is required";
        } else if (values.firstName.length > 100) {
            newErrors.firstName = "Max 100 characters";
        }

        // LAST NAME
        if (!values.lastName.trim()) {
            newErrors.lastName = "Last name is required";
        } else if (values.lastName.length > 100) {
            newErrors.lastName = "Max 100 characters";
        }

        // AGE
        const ageNum = Number(values.age);

        if (!values.age) {
            newErrors.age = "Age is required";
        } else if (isNaN(ageNum) || ageNum < 1 || ageNum > 120) {
            newErrors.age = "Age must be between 1 and 120";
        }

        // PHONE (OPTIONAL)
        const phone = values.phoneNumber.trim();

        if (phone.length > 0) {
            const regex = /^[0-9+ ]{6,20}$/;

            if (!regex.test(phone)) {
                newErrors.phoneNumber = "Invalid phone number format";
            }
        }

        // OBSERVATIONS
        if (values.generalObservations.length > 1000) {
            newErrors.generalObservations = "Max 1000 characters";
        }

        return newErrors;
    }

    function updateField(e) {
        const { name, value } = e.target;

        const updated = {
            ...form,
            [name]: value
        };

        setForm(updated);
        setErrors(validate(updated));
    }

    async function submit(e) {
        e.preventDefault();

        const validationErrors = validate(form);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            return;
        }

        try {
            await createUser({
                ...form,
                age: Number(form.age),
                phoneNumber: form.phoneNumber.trim() || null
            });

            navigate("/users");

        } catch (error) {
            console.log(error);
            alert("Error creating user");
        }
    }

    const hasErrors = Object.keys(errors).length > 0;

    return (
        <div style={{
            maxWidth: "500px",
            margin: "40px auto",
            padding: "25px",
            border: "1px solid #ddd",
            borderRadius: "10px",
            boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
        }}>

            <h2 style={{ textAlign: "center" }}>
                Create User
            </h2>

            <form
                onSubmit={submit}
                style={{ display: "flex", flexDirection: "column", gap: "18px" }}
            >

                <div>
                    <label>First Name</label>
                    <input
                        name="firstName"
                        value={form.firstName}
                        onChange={updateField}
                        style={inputStyle}
                    />
                    {errors.firstName && <span style={errorStyle}>{errors.firstName}</span>}
                </div>

                <div>
                    <label>Last Name</label>
                    <input
                        name="lastName"
                        value={form.lastName}
                        onChange={updateField}
                        style={inputStyle}
                    />
                    {errors.lastName && <span style={errorStyle}>{errors.lastName}</span>}
                </div>

                <div>
                    <label>Age</label>
                    <input
                        name="age"
                        type="number"
                        value={form.age}
                        onChange={updateField}
                        style={inputStyle}
                    />
                    {errors.age && <span style={errorStyle}>{errors.age}</span>}
                </div>

                <div>
                    <label>Phone Number (optional)</label>
                    <input
                        name="phoneNumber"
                        value={form.phoneNumber}
                        onChange={updateField}
                        style={inputStyle}
                    />
                    {errors.phoneNumber && <span style={errorStyle}>{errors.phoneNumber}</span>}
                </div>

                <div>
                    <label>General Observations</label>
                    <textarea
                        name="generalObservations"
                        value={form.generalObservations}
                        onChange={updateField}
                        style={{ ...inputStyle, minHeight: "100px" }}
                    />
                    {errors.generalObservations && (
                        <span style={errorStyle}>{errors.generalObservations}</span>
                    )}
                </div>

                <button
                    type="submit"
                    disabled={hasErrors}
                    style={{
                        padding: "12px",
                        background: hasErrors ? "#94a3b8" : "#2563eb",
                        color: "white",
                        border: "none",
                        borderRadius: "6px",
                        cursor: hasErrors ? "not-allowed" : "pointer"
                    }}
                >
                    Save User
                </button>

            </form>
        </div>
    );
}

const inputStyle = {
    width: "100%",
    padding: "10px",
    marginTop: "6px",
    border: "1px solid #ccc",
    borderRadius: "6px"
};

const errorStyle = {
    color: "red",
    fontSize: "12px"
};