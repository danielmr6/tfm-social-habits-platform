import { useState } from "react";

import {
    registerProfessional
} from "../../services/authservice";

import {
    useNavigate
} from "react-router-dom";

export default function Register() {

    const navigate =
        useNavigate();

    const [form, setForm] = useState({

        name: "",

        email: "",

        password: ""

    });

    function update(e) {

        setForm({

            ...form,

            [e.target.name]:
            e.target.value

        });

    }

    async function submit(e) {

        e.preventDefault();

        try {

            await registerProfessional(
                form
            );

            alert(
                "Professional created"
            );

            navigate(
                "/login"
            );

        } catch (err) {

            console.log(err);

            alert(
                "Error creating account"
            );

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

                    <label>

                        Name

                    </label>

                    <input
                        name="name"
                        placeholder="Enter your name"
                        onChange={update}
                        style={inputStyle}
                    />

                </div>

                <div>

                    <label>

                        Email

                    </label>

                    <input
                        name="email"
                        type="email"
                        placeholder="Enter your email"
                        onChange={update}
                        style={inputStyle}
                    />

                </div>

                <div>

                    <label>

                        Password

                    </label>

                    <input
                        name="password"
                        type="password"
                        placeholder="Enter password"
                        onChange={update}
                        style={inputStyle}
                    />

                </div>

                <button
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