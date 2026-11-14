import {Link, NavLink} from "react-router-dom";
import Header from "../components/Header.jsx";
import LoginPage from "./auth/LoginPage.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import './MyEventsPage.css';
import { FaUserGroup} from "react-icons/fa6";
import { FaHeart } from "react-icons/fa6";
import { FaCalendarDays } from "react-icons/fa6";
import { SlMagnifier } from "react-icons/sl";
import { SlEnvolope } from "react-icons/sl";
import { FaArrowRightLong } from "react-icons/fa6";
import EventPreview from "../components/EventPreview.jsx";
import Pagination from "../components/Pagination.jsx";

const MyEventsPage = () => {
    const { user, setUser } = useContext(AuthContext);
    const [activeTab, setActiveTab] = useState( "created");
    const [events, setEvents] = useState([]);

    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const [dashboard, setDashboard] = useState(null);

    useEffect(() => {
        if (!user) return;
        const fetchEvents = async () => {
            try {
                const url = activeTab === "created" ?
                    `/api/v1/events/created/${user.id}` :
                    `/api/v1/events/joined/${user.id}`;

                const response = await fetch(
                    `http://localhost:8080${url}?page=${page-1}`, {
                        credentials: "include"
                    }
                );

                if (response.status === 401) {
                    setUser(null)
                    return null;
                }

                if (!response.ok) {
                    throw new Error(response.statusText);
                }

                const data = await response.json();
                setEvents(data.content);
                setTotalPages(data.totalPages);
            } catch (error) {
                console.error(error);
            }
        }
        fetchEvents();
    }, [activeTab, page, setUser, user]);

    useEffect(() => {
        if(!user) return;

        const fetchDashboard = async () => {
            const response = await fetch("http://localhost:8080/api/v1/events/dashboard", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include"
            });

            if(response.status === 401) {
                setUser(null)
                return;
            }

            if(!response.ok) {
                throw new Error(response.statusText);
            }

            const data = await response.json();
            setDashboard(data);
        }

        fetchDashboard();
    }, [user]);

    if (user === null) return <LoginPage />;
    if(dashboard === null) return <p>Загружаем статистику</p>;

    return (
        <div className="my__events__page">
            <Header />

            <div className="my__events__page__content">

                <div className="my-events-sidebar">
                    <NavLink className="my-events-sidebar-nav-link" to="/events">
                        <FaCalendarDays size={15} /> Мои события</NavLink>
                    <NavLink className="my-events-sidebar-nav-link" to="/">
                        <FaUserGroup size={15} /> Друзья</NavLink>
                    <NavLink className="my-events-sidebar-nav-link" to="/">
                        <FaHeart size={15} /> Избранные</NavLink>
                </div>


                <div className="my__events__page__layout">
                    <div style={{flex: 6}}>
                        <div className="my__events__page__header">
                            <div style={{display: "flex", justifyContent: "space-between", alignItems: "center"}}>
                                <div className="my__events__page__header__text">
                                    <p>Мои события</p>
                                    <p>Управляйте своими событиями</p>
                                </div>

                                <Link className="my__events__page__header__create__event__link" to="/events/create">Создать событие +</Link>
                            </div>

                            <div className="my__events__page__header__info">
                                <div>
                                    <FaCalendarDays size={25} />
                                    <div>
                                        <p>{dashboard.totalEvents}</p>
                                        <p>Всего событий</p>
                                    </div>
                                </div>

                                <div>
                                    <FaUserGroup size={25} />
                                    <div>
                                        <p>{dashboard.totalHostedEvents}</p>
                                        <p>Организованны мой</p>
                                    </div>
                                </div>
                            </div>

                            {/*Показывать только если есть события*/}
                            {dashboard.nextEvent && (
                                <div className="my__events__page__header__upcoming__event">
                                    <div className="my__events__page__header__upcoming__event__left">
                                        <div className="my__events__page__header__upcoming__event__info__header">
                                            <p>Ваше следующее событие</p>
                                            <p>{dashboard.nextEvent.name}</p>
                                        </div>

                                        <div className="my__events__page__header__upcoming__event__info">
                                            <p><FaCalendarDays size={15} /> дата</p>
                                            <p><FaUserGroup size={15} /> {dashboard.nextEvent.joined} участников</p>
                                        </div>
                                    </div>

                                    <div className="my__events__page__header__upcoming__event__right">
                                        <p>Осталось 3 дня</p>
                                        <Link className="upcoming__explore__link" to={`/events/${dashboard.nextEvent.id}`}>Подробнее</Link>
                                    </div>
                                </div>
                            )}

                        </div>

                        <div className="my__events__layout">
                            <div className="my__events__header">
                                <div style={{display: "flex", justifyContent: "space-between", alignItems: "center"}}>
                                    <div className="my__events__header__text">
                                        <p>Список событий</p>
                                    </div>

                                    <div className="my__events__header__tabs">
                                        <button
                                            onClick={() => {
                                                setActiveTab("created")
                                                setPage(1)
                                            }}
                                            className={activeTab === "created" ? "active" : ""}
                                        >Создал(а)</button>
                                        <button
                                            onClick={() => {
                                                setActiveTab("joined")
                                                setPage(1)
                                            }}
                                            className={activeTab === "joined" ? "active" : ""}
                                        >Вступил(а)</button>
                                    </div>
                                </div>

                                <div className="my__events__header__searchbar">
                                    <SlMagnifier  style={{ color: "gray" }} size={15} />
                                    <input placeholder="Название события" type="text"/>
                                </div>
                            </div>

                            <div className="my__events__grid">
                                {events.length > 0 ? (
                                    <div>
                                        {events.map(({id, name, capacity, joined, visibility, hostUsername}) => (
                                            <EventPreview
                                                key={id}
                                                id={id}
                                                name={name}
                                                capacity={capacity}
                                                joined={joined}
                                                visibility={visibility}
                                                hostUsername={hostUsername}
                                            />))}
                                    </div>
                                ) : (
                                    <div>
                                        <h1>Создайте свое первое событие прямо сейчас!</h1>
                                        <Link to="/events/create">Создать Событие</Link>
                                    </div>
                                )}

                                <Pagination
                                    page={page}
                                    totalPages={totalPages}
                                    onPageChange={setPage}
                                />
                            </div>
                        </div>
                    </div>

                    <div className="my__events__page__subinfo">
                        <div className="my__events__page__pending__invitations">
                            <p> <SlEnvolope style={{color: "var(--active-text)"}} size={20}/> Приглашения</p>
                            <div className="my__events__page__pending__invitations__info">
                                <div className="my__events__page__pending__invitations__info__text">
                                    <p>{dashboard.pendingInvitations}</p>
                                    <p>Ждут Вашего ответа</p>
                                </div>
                                <Link to={"/events/invites"}> Приглащения <FaArrowRightLong size={15}/> </Link>
                            </div>
                        </div>

                        <div className="my__events__page__upcoming__this__week">
                            <p> <FaCalendarDays style={{color: "var(--active-text)"}} size={20} /> События этой недели</p>
                            <div className="my__events__page__upcoming__this__week__events__list">
                                {dashboard.weeklyEvents.map(event => (
                                    <div>
                                        <p>{event.name}</p>
                                        <p>1 day later</p>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default MyEventsPage;