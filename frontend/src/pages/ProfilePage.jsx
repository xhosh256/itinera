import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import {Link, Navigate, useNavigate, useParams} from "react-router-dom";
import Header from "../components/Header.jsx";
import './ProfilePage.css'
import LoginPage from "./auth/LoginPage.jsx";
import EventPreview from "../components/EventPreview.jsx";
import Divider from "../components/Divider.jsx";
import {
    FaUser,
    FaGear,
    FaArrowRightFromBracket
} from 'react-icons/fa6';
import ItineraStamp from "../components/ItineraStamp.jsx";

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
    }, [userId, user]);

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
                return;
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
                    `/api/v1/events/created/${userId}?size=4` :
                    `/api/v1/events/joined/${userId}?size=4`;

                const response = await fetch(
                    `http://localhost:8080${url}`, {
                        credentials: "include"
                    }
                );

                if (response.status === 401) {
                    setUser(null)
                    return;
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
            return;
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

                    <div className="profile__top">
                        <div className="profile__card">
                            <p className="name"><FaUser size={20}/> {profile.firstname} {profile.lastname}</p>
                            <p className="username">@{profile.username}</p>
                            <ItineraStamp/>
                        </div>

                        {   (myProfile && user) && (Number(userId) !== user.id
                                && !myProfile.friendIds.includes(profile.id))
                            && <button onClick={handleSendFriendRequest}>Добавить в друзья</button>}

                        {friendRequestPending && <p>Запрос отправлен</p>}

                        {(Number(userId) === user.id) && (
                            <button className="logout-btn" onClick={handleLogout}>
                                <FaArrowRightFromBracket size={13}/>
                                <p>Выйти из аккаунта</p>
                            </button>
                        )}
                    </div>


                    <Divider/>

                    <div className="profile__info">
                        <div className="userId">
                            <p> User Id:</p>
                            <p> {userId}</p>
                        </div>

                        <div className="birthdate">
                            <p>Дата рождения: </p>
                            <p>{profile.birthDate}</p>
                        </div>

                        <div className="bio">
                            <p>Обо мне</p>
                            <p>{profile.bio}</p>
                        </div>

                        {(myProfile && user) && (Number(userId) === user.id) &&
                            <Link className="profile__edit__link" to={`/profile/edit/${profile.id}`}>
                                Редактировать профиль
                            </Link>}
                    </div>

                    <Divider/>

                    <div className="profile__friends">
                        <div className="friend__layout">
                            <p style={{fontWeight: "bold", fontSize: "18px"}}>Друзья</p>

                            {(Number(userId) === user.id) &&
                                <Link className="friend__requests" to={`/friends/requests`}>Заявки в друзья</Link>}

                            <div className="friend__list">
                                {myFriends.content.map((friend) => (
                                    <div key={friend.id}>
                                        <Link to={`/profile/${friend.userId}`}>
                                            <p className="name">{friend.firstname} {friend.lastname}</p>
                                            <p className="username">@{friend.username}</p>
                                        </Link>
                                    </div>
                                ))}
                            </div>
                        </div>


                        <div>
                            {Number(userId) === user.id ? (
                                <div className="profile__find__friends">
                                    <p style={{fontWeight: "bold", fontSize: "18px"}}>Найти друзей</p>
                                    <div className="user__searchbar">
                                        <div className="user__searchbar_input">
                                            <input placeholder="Введите UserId или @username" onChange={(e) => setSearchUserId(e.target.value)} />
                                            <button onClick={handleFindUser}>
                                                Найти
                                            </button>
                                        </div>

                                        {!tryFetch ? (
                                            <></>
                                        ) : (tryFetch && !searchUserProfile) ? (
                                            <h1>Пользователь не найден</h1>
                                        ) : (
                                            <Link className="found__profile" to={`/profile/${searchUserId}`}>{searchUserProfile.firstname} {searchUserProfile.lastname}</Link>
                                        )}
                                    </div>
                                </div>
                            ) : <></>}
                        </div>
                    </div>
                </div>

                <div className="content">
                    <div className="content__header">
                        <p>События</p>
                        <div className="tabs">
                            <button
                                className={`tab ${activeTab === "created" ? "active" : "disabled"}`}
                                onClick={() => setActiveTab("created")}
                            >Созданные</button>
                            <button
                                className={`tab ${activeTab === "joined" ? "active" : "disabled"}`}
                                onClick={() => setActiveTab("joined")}
                            >Присоединен(а)</button>
                        </div>
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