import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import {useNavigate, useParams} from "react-router-dom";
import Header from "../components/Header.jsx";
import './ProfilePage.css'

const ProfilePage = () => {

    const { setUser } = useContext(AuthContext);
    const navigate = useNavigate();

    const {userId} = useParams();

    const [profile, setProfile] = useState(null);

    useEffect(() => {
        const fetchProfile = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/profiles/${userId}`, {
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

    if(profile === null) {
        return (
            <p>Профиля не существует!</p>
        )
    }

    return (
        <div className="profile-page">
            <Header />
            <div className="profile-layout">
                <div className="sidebar">
                    <p className="user-name">{profile.firstname} {profile.lastname}</p>
                    <p className="user_id">User Id: {userId}</p>
                    <p className="user-bio">Обо мне: {profile.bio}</p>
                </div>

                <div className="content">
                    <div className="tabs">
                        <button className="tab">Присоединен(а)</button>
                        <button className="tab">Созданные</button>
                    </div>

                    <div className="events-grid">

                    </div>
                </div>
            </div>

            <button className="logout-btn" onClick={handleLogout}>
                Log out
            </button>
        </div>

    )
}

export default ProfilePage;