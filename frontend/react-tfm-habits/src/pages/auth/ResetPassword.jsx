import { useSearchParams } from "react-router-dom";

import { useState } from "react";

import api from "../../services/api";

export default function ResetPassword(){

    const [params]=useSearchParams();

    const token =
        params.get("token");

    const [password,setPassword]=
        useState("");

    async function submit(e){

        e.preventDefault();

        await api.post(

            "/auth/reset-password",

            null,

            {

                params:{

                    token,

                    newPassword:password

                }

            }

        );

        alert(
            "Password updated"
        );

    }

    return(

        <form onSubmit={submit}>

            <input

                type="password"

                value={password}

                onChange={
                    e=>setPassword(
                        e.target.value
                    )
                }

            />

            <button>

                Change Password

            </button>

        </form>

    );

}