import api from "./api";

export async function login(
    email,
    password
){

    const response = await api.post(

        "/auth/login",

        {

            email,

            password

        }

    );

    return response.data;

}

export async function registerProfessional(
    data
){

    const response = await api.post(

        "/auth/register",

        data

    );

    return response.data;

}