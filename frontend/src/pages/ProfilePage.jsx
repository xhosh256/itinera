import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import {Link, Navigate, useNavigate, useParams} from "react-router-dom";
import Header from "../components/Header.jsx";
import './ProfilePage.css'
import LoginPage from "./LoginPage.jsx";
import EventPreview from "../components/EventPreview.jsx";

const ProfilePage = () => {

    const { user, setUser } = useContext(AuthContext);

    const navigate = useNavigate();

    const {userId} = useParams();
    const [searchUserId, setSearchUserId] = useState(null);
    const [searchUserProfile, setSearchUserProfile] = useState(null);
    const [tryFetch, setTryFetch] = useState(false);
    const [friendRequestPending, setFriendRequestPending] = useState(false);

    const [myProfile, setMyProfile] = useState(null);
    const [myFriends, setMyFriends] = useState(null);
    const [profile, setProfile] = useState(null);
    const [events, setEvents] = useState([]);
    const [activeTab, setActiveTab] = useState("created");

    useEffect(() => {
        if(!user) return;

        const fetchFriends = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/friendships/friends/${userId}`, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if(response.status === 401) {
                setUser(null);
                return;
            }

            if(!response.ok) {
                throw new Error(response.statusText);
            }

            const data = await response.json();
            setMyFriends(data);
        }

        fetchFriends()
    }, [profile]);

    useEffect(() => {
        if(!user) return;

        const fetchMyProfile = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/profiles/${user.id}`, {
                method: "GET",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                }
            });

            if (response.status === 401) {
                setUser(null)
                return <LoginPage />
            }

            if (!response.ok) {
                throw new Error(response.statusText);
            }

            const data = await response.json();
            setMyProfile(data);

        }

        fetchMyProfile();
    }, [user]);

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
    }, [activeTab, setUser, userId]);

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

        setUser(null);
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

    if(!user) {
        return <LoginPage/>
    }

    if(profile === null) {
        return (
            <p>Профиля не существует!</p>
        )
    }

    if(myFriends === null) {
        return <h1>Загрузка...</h1>
    }

    const handleSendFriendRequest = async () => {
        const response = await fetch(`http://localhost:8080/api/v1/friendships/${userId}`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            }
        });

        if(response.status === 401) {
            setUser(null);
            return <LoginPage/>
        }

        if(!response.ok) {
            throw new Error(response.statusText);
        }

        setFriendRequestPending(true);
    }

    return (
        <div className="profile-page">
            <Header />
            <div className="profile-layout">
                <div className="sidebar">
                    {(myProfile && user) && (Number(userId) === user.id) && <Link to={`/profile/edit/${profile.id}`}>Редактировать профиль</Link>}
                    {   (myProfile && user) && (Number(userId) !== user.id
                            && !myProfile.friendIds.includes(profile.id))
                        && <button onClick={handleSendFriendRequest}>Добавить в друзья</button>}
                    {friendRequestPending && <p style={{ color: "green" }}>Запрос отправлен</p>}
                    <p>{profile.firstname} {profile.lastname} User Id: {userId}</p>
                    <p>Дата рождения: {profile.birthDate}</p>
                    <p>Обо мне: {profile.bio}</p>

                    <div>
                        <h1>Друзья {(Number(userId) === user.id) && <Link to={`/friends/requests`}>Заявки</Link>}</h1>

                        {Number(userId) === user.id ? (
                            <>
                                <p>Найти друзей</p>
                                <div className="user__searchbar">
                                    <input placeholder="Поиск" onChange={(e) => setSearchUserId(e.target.value)} />
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
                            </>
                        ) : <></>}
                    </div>

                    <div>
                        {myFriends.map((friend) => (
                            <div key={friend.id}>
                                <p>{friend.firstname} {friend.lastname}</p>
                            </div>
                        ))}
                    </div>

                    {(Number(userId) === user.id) && (
                        <button className="logout-btn" onClick={handleLogout}>
                            Log out
                        </button>
                    )}
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
        </div>

    )
}

export default ProfilePage;