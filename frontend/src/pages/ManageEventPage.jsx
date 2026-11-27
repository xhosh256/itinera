import {Link, useNavigate, useParams} from "react-router-dom";
import Header from "../components/Header.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import LoginPage from "./auth/LoginPage.jsx";
import './ManageEventPage.css'
import EventComponent from "../components/EventComponent.jsx";
import InviteFriendsModalWindow from "../components/InviteFriendsModalWindow.jsx";
import Divider from "../components/Divider.jsx";
import {FaCircleCheck , FaUserGroup} from "react-icons/fa6";
import { BsBox } from "react-icons/bs";
import { BsFillPencilFill } from "react-icons/bs";
import { BsFillTrashFill } from "react-icons/bs";
import { FiSend } from "react-icons/fi";

const ManageEventPage = () => {
    const { id } = useParams();
    const {user, setUser} = useContext(AuthContext);

    const [event, setEvent] = useState(null);
    const [isInviteModalOpen, setIsInviteModalOpen] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        const fetchEvent = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/v1/events/${id}`, {
                    credentials: "include",
                });

                if (response.status === 401) {
                    setUser(null);
                    return;
                }

                if (response.status === 403) {
                    setEvent({
                        visibility: "PRIVATE",
                        participantIds: []
                    });

                    return;
                }

                if (!response.ok) {
                    throw new Error(response.statusText);
                }

                const data = await response.json();
                setEvent(data);
            } catch (error) {
                console.error(error);
                setEvent(null);
            }
        };

        fetchEvent();
    }, [id, setUser])

    const handleJoin = async () => {
        const response = await fetch(`http://localhost:8080/api/v1/events/${id}/joins`, {
            credentials: "include",
            method: "POST",
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

        if(response.ok) {
            setEvent(prev => ({
                ...prev,
                participantIds: [...prev.participantIds, user.id]
            }));
        }

    }

    const handleDeleteEvent = async (e) => {
        e.preventDefault();

        const response = await fetch(`http://localhost:8080/api/v1/events/${event.id}`, {
            method: "DELETE",
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

        navigate("/events");
    }

    const handleComponentDeleted = (componentId) => {
        setEvent(prev => ({
            ...prev,
            components: prev.components.filter(c => c.id !== componentId)
        }))
    }

    const handleKick = async (deleteId, e) => {
        const response = await fetch(`http://localhost:8080/api/v1/events/${event.id}/kicks/${deleteId}`, {
            credentials: "include",
            method: "DELETE",
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

        setEvent(prev => ({
            ...prev,
            participantIds: prev.participantIds.filter(participantId => participantId !== deleteId),
            participantPreviews: prev.participantPreviews.filter(preview => preview.userId !== deleteId)
        }));
    }

    const handleLeave = async () => {
        const response = await fetch(`http://localhost:8080/api/v1/events/${event.id}/leaves`, {
            credentials: "include",
            method: "DELETE",
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

        setEvent(prev => ({
            ...prev,
            participantIds: prev.participantIds.filter(participantId => participantId !== user.id),
            participantPreviews: prev.participantPreviews.filter(preview => preview.userId !== user.id)
        }));

        navigate("/events");
    }

    if(user===null) return <LoginPage />

    if(event===null) return (
        <>
            <Header/>
            Ивента не существует!
        </>
    )

    if (event.visibility === 'PRIVATE' && !event.participantIds.includes(user.id)) {
        return (
            <>
                У вас нет прав для этого ивента
            </>
        )
    }

    return (
        <div className="manage__event__page">
            <Header />
            <div className="manage__event__layout">
                <div className="manage__event__sidebar">
                    <div className="manage__event__sidebar__header">
                        <div className="manage__event__sidebar__header__info">
                            <p>{event.name}</p>
                            <p style={{height: "fit-content"}} className={event.visibility === 'PUBLIC' ? "public" : "private"}>{event.visibility}</p>
                        </div>
                        <p>Хост: @{event.hostUsername}</p>
                    </div>

                    <Divider/>

                    <div className="manage__event__sidebar__info">
                        {event.participantIds.includes(user.id) ?
                            <p className="you__take__a__part"> <FaCircleCheck/> Вы учавствуете </p> :
                            <button onClick={handleJoin} >Учавствовать</button>
                        }

                        <div className="manage__event__sidebar__participants">
                            <div className="manage__event__sidebar__participants__info">
                                <p> <FaUserGroup/> Участники</p>
                                <p>{event.participantIds.length} / {event.capacity}</p>
                            </div>
                            <div className="manage__event__sidebar__participants__items">
                                {event.participantPreviews.map((profile) => (
                                    <div className="manage__event__sidebar__participants__item">
                                        <Link
                                            className="manage__event__sidebar__participants__item__link"
                                            to={`/profile/${profile.userId}`}
                                        >
                                            <div className="manage__event__sidebar__participants__item__info">
                                                <p>{profile.firstname} {profile.lastname}</p>
                                                <p>@{profile.username}</p>
                                            </div>

                                        </Link>
                                        {(user.id === event.hostId && user.id !== profile.userId) && (<button
                                            className="manage__event__sidebar__participants__kick__button"
                                            onClick={(e) => handleKick(profile.userId, e)}
                                        >Выгнать</button>)}
                                    </div>


                                ))}
                            </div>
                        </div>
                    </div>

                    <Divider/>

                    <div className="manage__event__sidebar__buttons">
                        {event.hostId === user.id && (
                            <button
                                className="manage__event__sidebar__add__friends__button"
                                onClick={() => setIsInviteModalOpen(true)}>Добавить друзей</button>
                        )}

                        {event.hostId === user.id && (
                            <div className="manage__event__sidebar__update__buttons">
                                <Link className="manage__event__sidebar__edit_btn" to={`/events/${event.id}/edit`}> <BsFillPencilFill/> Редактировать</Link>
                                <button className="manage__event__sidebar__delete_btn" onClick={handleDeleteEvent}> <BsFillTrashFill/> Удалить</button>
                            </div>
                        )}
                    </div>

                    {event.hostId !== user.id && event.participantIds.includes(user.id) && (
                        <button className="manage__event__sidebar__leave_btn" onClick={handleLeave}>Покинуть событие</button>
                    )}


                </div>

                <div className="manage__event__components">
                    <div className="manage__event__components__header">
                        <div className="manage__event__components__header__text">
                            <p>Компоненты события</p>
                            <p>Организовывайте расписание своих событий в удобном формате</p>
                        </div>

                        {event.hostId === user.id && (
                            <Link className="manage__event__components__header__add__btn" to={`/events/${event.id}/components/create`}>+ Добавить</Link>
                        )}
                    </div>

                    <Divider/>

                    {event.components.length !== 0 ? event.components.map(({id, startTime, endTime, description, address}) => (
                        <EventComponent
                            key={id}
                            componentId={id}
                            event={event}
                            startTime={startTime}
                            endTime={endTime}
                            description={description}
                            address={address}
                            onDeleted={handleComponentDeleted}
                        />
                    )) : (
                        <div className="manage__event__components__empty__page">
                            <BsBox className="manage__event__components__empty__page__icon" size={50}/>
                            <p style={{fontSize: "24px", fontWeight: "bold"}}>Пока что здесь пусто</p>

                            {user.id === event.hostId && (
                                <>
                                    <p style={{fontSize: "16px", color: "var(--text-muted)"}}>Добавьте первый компонент, чтобы вместе со всеми спланировать все детали</p>
                                    <Link
                                        className="manage__event__components__empty__page__add__btn"
                                        style={{marginTop: "10px", fontSize: "20px"}}
                                        to={`/events/${event.id}/components/create`}> + Создать компонент</Link>
                                </>

                            )}
                        </div>
                    )}
                </div>


                <div className="manage__event__chat">
                    <div className="manage__event__chat__header">
                        <p>Чат события</p>
                        <p>Общайтесь с участниками</p>
                        <Divider />
                    </div>

                    <div className="manage__event__chat__form">
                        <input className="manage__event__chat__input" type="text" placeholder="Написать сообщение..."/>
                        <button className="manage__event__chat__send__button"><FiSend size={25}/></button>
                    </div>


                </div>
            </div>

            {isInviteModalOpen && (
                <InviteFriendsModalWindow
                    eventId={event.id}
                    eventName={event.name}
                    onClose={() => setIsInviteModalOpen(false)}
                />
            )}
        </div>
    )
}

export default ManageEventPage;