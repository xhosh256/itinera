import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import {Link, useNavigate, useParams} from "react-router-dom";
import Header from "../components/Header.jsx";
import './ProfilePage.css'
import LoginPage from "./LoginPage.jsx";
import EventPreview from "../components/EventPreview.jsx";

const ProfilePage = () => {

    const { setUser } = useContext(AuthContext);
    const navigate = useNavigate();

    const {userId} = useParams();
    const [searchUserId, setSearchUserId] = useState(null);
    const [searchUserProfile, setSearchUserProfile] = useState(null);
    const [tryFetch, setTryFetch] = useState(false);

    const [profile, setProfile] = useState(null);
    const [events, setEvents] = useState([]);
    const [activeTab, setActiveTab] = useState("created");

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const url = activeTab === "created" ?
                    `/api/v1/events/created/${userId}` :
                    `/api/v1/events/joined/${userId}`;

                const response = await fetch(
                    `http://localhost:8080${url}`, {
                        credentials: "include"
                    }
                );

                if (response.status === 401) {
                    setUser(null)
                    return <LoginPage />
                }

                if (!response.ok) {
                    throw new Error(response.statusText);
                }

                const data = await response.json();
                setEvents(data.content);
            } catch (error) {
                console.error(error);
            }
        }
        fetchEvents();
    }, [activeTab, setUser]);

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

    const handleFindUser = async () => {
        const response = await fetch(`http://localhost:8080/api/v1/profiles/${searchUserId}`, {
            credentials: "include",
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            }

        });

        setTryFetch(true);
        if(response.status === 401) {
            setUser(null);
            return;
        }

        if(response.status === 404) {
            setSearchUserProfile(null);
            return;
        }

        if(!response.ok) {
            throw new Error(response.statusText);
        }

        const data = await response.json();
        setSearchUserProfile(data);
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

                    <div>
                        <p>Друзья</p>
                        <div className="user__searchbar">
                            <input placeholder="Найти друзей" onChange={(e) => setSearchUserId(e.target.value)} />
                            <button onClick={handleFindUser}>Найти</button>
                            <div>
                                {!tryFetch ? (
                                    <></>
                                ) : (tryFetch && !searchUserProfile) ? (
                                    <h1>Пользователь не найден</h1>
                                ) : (
                                    <Link to={`/profile/${searchUserId}`}>{searchUserProfile.firstname} {searchUserProfile.lastname}</Link>
                                )}
                            </div>
                        </div>
                    </div>
                </div>

                <div className="content">
                    <div className="tabs">
                        <button
                            className="tab"
                            onClick={() => setActiveTab("created")}
                        >Созданные</button>
                        <button
                            className="tab"
                            onClick={() => setActiveTab("joined")}
                        >Присоединен(а)</button>
                    </div>

                    <div className="events-grid">
                        {events.length > 0 ? (
                            <>
                                {events.map(({id, name, capacity, joined, visibility}) => (
                                    <EventPreview
                                        key={id}
                                        id={id}
                                        name={name}
                                        capacity={capacity}
                                        joined={joined}
                                        visibility={visibility}
                                    />))}
                            </>
                        ) : (
                            <>
                                <p>Событий нет(</p>
                            </>
                        )}
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