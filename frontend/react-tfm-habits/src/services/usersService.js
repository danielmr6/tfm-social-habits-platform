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

export async function getUserById(id) {
    const res = await api.get(`/users/${id}`);
    return res.data;
}

export async function deleteUser(id) {
    const res = await api.delete(`/users/${id}`);
    return res.data;
}

export async function updateUser(
    id,
    user
) {

    const response = await api.put(
        `/users/${id}`,
        user
    );

    return response.data;
}

export async function downloadUserReport(userId) {

    const response = await api.get(
        `/api/reports/user/${userId}`,
        {
            responseType: "blob"
        }
    );

    return response.data;
}