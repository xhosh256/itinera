import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import {useNavigate, useParams} from "react-router-dom";

const ProfilePage = () => {

    const { setUser } = useContext(AuthContext);
    const navigate = useNavigate();

    const {userId} = useParams();

    const [profile, setProfile] = useState({});

    useEffect(() => {
        const fetchProfile = async () => {
            const response = await fetch(`http://localhost:8080/profiles/${userId}`, {
                credentials: "include",
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });

            if(response.status === 401) {
                setUser(null);
                return;
            }

            if(!response.ok) {
                throw new Error(response.statusText);
            }

            const profile = await response.json();
            setProfile(profile);
            console.log(profile);
        }

        fetchProfile();
    }, [userId]);

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
        <div>
            <p>{profile.firstname}</p>
            <button onClick={handleLogout}>
                Log out
            </button>
        </div>

    )
}

export default ProfilePage;