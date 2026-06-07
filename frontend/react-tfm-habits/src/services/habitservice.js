import api from "./api";

export async function createHabit(userId, habit) {
    const response = await api.post(`/habits/${userId}`, habit);
    return response.data;
}

export async function deleteHabit(habitId) {
    await api.delete(`/habits/${habitId}`);
}