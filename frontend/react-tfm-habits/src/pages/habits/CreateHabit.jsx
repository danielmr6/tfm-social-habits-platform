import { useState } from "react";

import {
    useParams,
    useNavigate
} from "react-router-dom";

import {
    createHabit
} from "../../services/habitservice";

export default function CreateHabit(){

    const { id } = useParams();

    const navigate =
        useNavigate();

    const [habit, setHabit] = useState({
        type: "FOOD",
        status: "CORRECT",
        description: "",
        date: ""
    });

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    async function submit(e) {
        e.preventDefault();

        setError("");
        setSuccess("");

        try {

            const habitToSend = {
                ...habit,
                date: habit.date || new Date().toISOString().split("T")[0]
            };

            await createHabit(id, habitToSend);

            setSuccess("Habit created successfully");

            setTimeout(() => {
                navigate(`/users/${id}`);
            }, 800);

            setHabit({
                type: "FOOD",
                status: "CORRECT",
                description: "",
                date: ""
            });

        } catch (err) {
            setError("Error creating habit");
        }
    }

    return (
        <div style={{ maxWidth: "500px", margin: "40px auto" }}>

            <h1>Add a new habit</h1>

            {error && (
                <div style={{ color: "red", marginBottom: "10px" }}>
                    {error}
                </div>
            )}

            {success && (
                <div style={{ color: "green", marginBottom: "10px" }}>
                    {success}
                </div>
            )}

            <form
                onSubmit={submit}
                style={{
                    display:"flex",
                    flexDirection:"column",
                    gap:"15px"
                }}
            >

                <select
                    value={habit.type}
                    onChange={e =>
                        setHabit({ ...habit, type: e.target.value })
                    }
                >
                    <option value="FOOD">Food</option>
                    <option value="SLEEP">Sleep</option>
                    <option value="ACTIVITY">Activity</option>
                    <option value="OTHER">Other</option>
                    <option value="HYDRATION">Hydration</option>
                </select>

                <select
                    value={habit.status}
                    onChange={e =>
                        setHabit({
                            ...habit,
                            status: e.target.value
                        })
                    }
                >
                    <option value="CORRECT">Correct</option>
                    <option value="IRREGULAR">Irregular</option>
                    <option value="NEGATIVE">Negative</option>
                </select>

                <input

                    type="date"

                    value={habit.date}

                    onChange={e=>

                        setHabit({

                            ...habit,

                            date:e.target.value

                        })

                    }

                />

                <textarea

                    placeholder="Description"

                    value={habit.description}

                    onChange={e=>

                        setHabit({

                            ...habit,

                            description:e.target.value

                        })

                    }

                />

                <button>

                    Save Habit

                </button>

            </form>

        </div>
    );

}