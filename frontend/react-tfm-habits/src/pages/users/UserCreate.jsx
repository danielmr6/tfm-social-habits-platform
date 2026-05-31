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

    function updateField(e) {

        setForm({
            ...form,
            [e.target.name]: e.target.value
        });

    }

    async function submit(e) {

        e.preventDefault();

        await createUser(form);

        navigate("/users");

    }

    return (

        <div
            style={{
                maxWidth: "500px",
                margin: "40px auto",
                padding: "25px",
                border: "1px solid #ddd",
                borderRadius: "10px",
                boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
            }}
        >

            <h2
                style={{
                    marginBottom: "25px",
                    textAlign: "center"
                }}
            >
                Create User
            </h2>

            <form
                onSubmit={submit}
                style={{
                    display: "flex",
                    flexDirection: "column",
                    gap: "18px"
                }}
            >

                <div>
                    <label>First Name</label>

                    <input
                        name="firstName"
                        placeholder="Enter first name"
                        onChange={updateField}
                        style={inputStyle}
                    />
                </div>

                <div>
                    <label>Last Name</label>

                    <input
                        name="lastName"
                        placeholder="Enter last name"
                        onChange={updateField}
                        style={inputStyle}
                    />
                </div>

                <div>
                    <label>Age</label>

                    <input
                        name="age"
                        type="number"
                        placeholder="Age"
                        onChange={updateField}
                        style={inputStyle}
                    />
                </div>

                <div>
                    <label>Phone Number</label>

                    <input
                        name="phoneNumber"
                        placeholder="Phone number"
                        onChange={updateField}
                        style={inputStyle}
                    />
                </div>

                <div>
                    <label>General Observations</label>

                    <textarea
                        name="generalObservations"
                        placeholder="Write observations..."
                        onChange={updateField}
                        style={{
                            ...inputStyle,
                            minHeight: "100px",
                            resize: "vertical"
                        }}
                    />
                </div>

                <button
                    style={{
                        padding: "12px",
                        border: "none",
                        background: "#2563eb",
                        color: "white",
                        borderRadius: "6px",
                        cursor: "pointer",
                        fontSize: "16px"
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
    borderRadius: "6px",
    boxSizing: "border-box"

};