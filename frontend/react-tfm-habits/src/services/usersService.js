import api from "./api";

export async function getUsers(
    search="",
    page=0,
    size=10
){

    const response = await api.get(
        "/users",
        {
            params:{
                search,
                page,
                size
            }
        }
    );

    return response.data;

}

export async function createUser(user){

    const response = await api.post(
        "/users",
        user
    );

    return response.data;

}