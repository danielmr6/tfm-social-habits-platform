import { useState } from "react";

import {
    registerProfessional
} from "../../services/authservice";

import {
    useNavigate
} from "react-router-dom";

export default function Register() {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        name: "",
        email: "",
        password: ""
    });

    const [errors, setErrors] = useState({});

    function update(e) {

        setForm({
            ...form,
            [e.target.name]: e.target.value
        });

        if (errors[e.target.name]) {

            setErrors({
                ...errors,
                [e.target.name]: null
            });

        }
    }

    function validate() {

        const newErrors = {};

        if (!form.name.trim()) {
            newErrors.name = "Name is required";
        }

        if (!form.email.trim()) {
            newErrors.email = "Email is required";
        } else if (!/^\S+@\S+\.\S+$/.test(form.email)) {
            newErrors.email = "Invalid email format";
        }

        if (!form.password) {
            newErrors.password = "Password is required";
        } else if (form.password.length < 8) {
            newErrors.password =
                "Password must contain at least 8 characters";
        }

        return newErrors;
    }

    async function submit(e) {

        e.preventDefault();

        const validationErrors = validate();

        if (Object.keys(validationErrors).length > 0) {

            setErrors(validationErrors);

            return;
        }

        setErrors({});

        try {

            await registerProfessional(form);

            navigate("/login");

        } catch (err) {

            const backendMessage =
                err?.response?.data?.message;

            setErrors({
                general:
                    backendMessage ||
                    "Unable to create account. Please try again."
            });

        }

    }

    return (

        <div
            style={{
                maxWidth: "450px",
                margin: "80px auto",
                padding: "30px",
                border: "1px solid #ddd",
                borderRadius: "12px",
                boxShadow:
                    "0 2px 10px rgba(0,0,0,0.1)"
            }}
        >

            <h1
                style={{
                    textAlign: "center",
                    marginBottom: "10px"
                }}
            >
                Register Professional
            </h1>

            <p
                style={{
                    textAlign: "center",
                    color: "#666",
                    marginBottom: "30px"
                }}
            >
                Create your professional account
            </p>

            <form
                onSubmit={submit}
                style={{
                    display: "flex",
                    flexDirection: "column",
                    gap: "20px"
                }}
            >

                <div>

                    <label>Name</label>

                    <input
                        name="name"
                        placeholder="Enter your name"
                        value={form.name}
                        onChange={update}
                        style={inputStyle}
                    />

                    {errors.name && (
                        <p style={errorStyle}>
                            {errors.name}
                        </p>
                    )}

                </div>

                <div>

                    <label>Email</label>

                    <input
                        name="email"
                        type="email"
                        placeholder="Enter your email"
                        value={form.email}
                        onChange={update}
                        style={inputStyle}
                    />

                    {errors.email && (
                        <p style={errorStyle}>
                            {errors.email}
                        </p>
                    )}

                </div>

                <div>

                    <label>Password</label>

                    <input
                        name="password"
                        type="password"
                        placeholder="Enter password"
                        value={form.password}
                        onChange={update}
                        style={inputStyle}
                    />

                    {errors.password && (
                        <p style={errorStyle}>
                            {errors.password}
                        </p>
                    )}

                </div>

                {errors.general && (

                    <p
                        style={{
                            ...errorStyle,
                            textAlign: "center"
                        }}
                    >
                        {errors.general}
                    </p>

                )}

                <button
                    type="submit"
                    style={{
                        padding: "12px",
                        border: "none",
                        borderRadius: "6px",
                        background: "#2563eb",
                        color: "white",
                        fontSize: "16px",
                        cursor: "pointer"
                    }}
                >
                    Create Account
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
    borderRadius: "6px",
    boxSizing: "border-box"

};

const errorStyle = {

    color: "red",
    fontSize: "12px",
    marginTop: "5px"

};