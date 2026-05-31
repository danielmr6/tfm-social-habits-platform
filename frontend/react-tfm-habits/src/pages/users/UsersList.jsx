import { useEffect, useState } from "react";
import { getUsers } from "../../services/usersService";

export default function UsersList() {
    const [users, setUsers] = useState([]);
    const [search, setSearch] = useState("");

    useEffect(() => {
        loadUsers();
    }, []);

    async function loadUsers() {
        const data = await getUsers(
            search,
            0,
            10
        );

        setUsers(data.content);
    }

    return (
        <div style={{ padding: "20px" }}>
            <h1>Users list</h1>

            <div
                style={{
                    display: "flex",
                    gap: "10px",
                    marginBottom: "20px"
                }}
            >
                <input
                    placeholder="Buscar"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    style={{
                        padding: "8px",
                        width: "250px"
                    }}
                />

                <button
                    onClick={loadUsers}
                    style={{
                        padding: "8px 16px",
                        cursor: "pointer"
                    }}
                >
                    Buscar
                </button>
            </div>

            <table
                style={{
                    width: "100%",
                    borderCollapse: "collapse",
                    boxShadow: "0 0 5px rgba(0,0,0,0.1)"
                }}
            >
                <thead>
                <tr
                    style={{
                        background: "#f3f3f3"
                    }}
                >
                    <th style={thStyle}>First Name</th>
                    <th style={thStyle}>Last Name</th>
                    <th style={thStyle}>Age</th>
                </tr>
                </thead>

                <tbody>
                {users.length > 0 ? (
                    users.map((user) => (
                        <tr key={user.id}>
                            <td style={tdStyle}>{user.firstName}</td>
                            <td style={tdStyle}>{user.lastName}</td>
                            <td style={tdStyle}>{user.age}</td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td
                            colSpan="3"
                            style={{
                                padding: "20px",
                                textAlign: "center"
                            }}
                        >
                            No users found
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}

const thStyle = {
    border: "1px solid #ddd",
    padding: "12px",
    textAlign: "left"
};

const tdStyle = {
    border: "1px solid #ddd",
    padding: "10px"
};