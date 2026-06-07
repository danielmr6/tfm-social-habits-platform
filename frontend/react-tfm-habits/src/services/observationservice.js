import api from "./api";

export async function createObservation(
    userId,
    content
){

    const response =
        await api.post(

            `/observations/${userId}`,

            {
                content
            }

        );

    return response.data;

}