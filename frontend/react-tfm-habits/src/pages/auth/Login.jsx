import { useState, useContext } from "react";

import { login } from "../../services/authservice";

import { AuthContext } from "../../context/AuthContext";

import { useNavigate } from "react-router-dom";

export default function Login() {

    const [email, setEmail] = useState("");

    const [password, setPassword] = useState("");

    const { loginUser } = useContext(
        AuthContext
    );

    const navigate = useNavigate();

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            const data = await login(

                email,

                password

            );

            loginUser(
                data.token
            );

            navigate(
                "/users"
            );

        } catch (error) {

            console.log(
                error
            );

            alert(
                "Incorrect credentials"
            );

        }

    };

    return (

        <div

            style={{

                maxWidth:"450px",

                margin:"80px auto",

                padding:"30px",

                border:"1px solid #ddd",

                borderRadius:"12px",

                boxShadow:
                    "0 2px 10px rgba(0,0,0,.1)"

            }}

        >

            <h1

                style={{

                    textAlign:"center",

                    marginBottom:"10px"

                }}

            >

                SocialHabits

            </h1>

            <p

                style={{

                    textAlign:"center",

                    color:"#666",

                    marginBottom:"30px"

                }}

            >

                Sign in to continue

            </p>

            <form

                onSubmit={handleSubmit}

                style={{

                    display:"flex",

                    flexDirection:"column",

                    gap:"20px"

                }}

            >

                <div>

                    <label>

                        Email

                    </label>

                    <input

                        type="email"

                        placeholder="Enter email"

                        value={email}

                        onChange={

                            e=>setEmail(

                                e.target.value

                            )

                        }

                        style={inputStyle}

                    />

                </div>

                <div>

                    <label>

                        Password

                    </label>

                    <input

                        type="password"

                        placeholder="Enter password"

                        value={password}

                        onChange={

                            e=>setPassword(

                                e.target.value

                            )

                        }

                        style={inputStyle}

                    />

                </div>

                <button

                    type="submit"

                    style={loginButton}

                >

                    Login

                </button>

                <button

                    type="button"

                    onClick={()=>{

                        navigate(
                            "/register"
                        )

                    }}

                    style={registerButton}

                >

                    Register Professional

                </button>

            </form>
            <button

                type="button"

                onClick={() =>
                    navigate("/forgot-password")
                }

                style={registerButton}

            >

                Forgot Password

            </button>
        </div>

    );

}

const inputStyle = {

    width:"100%",

    padding:"12px",

    marginTop:"6px",

    border:"1px solid #ccc",

    borderRadius:"6px",

    boxSizing:"border-box"

};

const loginButton = {

    padding:"12px",

    border:"none",

    borderRadius:"6px",

    background:"#2563eb",

    color:"white",

    fontSize:"16px",

    cursor:"pointer"

};

const registerButton = {

    padding:"12px",

    border:"1px solid #2563eb",

    borderRadius:"6px",

    background:"white",

    color:"#2563eb",

    fontSize:"16px",

    cursor:"pointer"

};