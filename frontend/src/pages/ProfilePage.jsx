import {useContext} from "react";
import AuthContext from "../context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";

const ProfilePage = () => {

    const { setUser } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogout = async () => {
        setUser(null);

        const response = await fetch(
            "http://localhost:8080/api/v1/auth/logout", {
                credentials: "include",
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                }
            }
        )

        if (!response.ok) {
            console.log("Something went wrong", response.statusText);
        }

        navigate("/");
    }

    return (
        <button onClick={handleLogout}>
            Log out
        </button>
    )
}

export default ProfilePage;