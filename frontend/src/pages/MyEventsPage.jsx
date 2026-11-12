import {Link, NavLink} from "react-router-dom";
import Header from "../components/Header.jsx";
import LoginPage from "./auth/LoginPage.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import EventPreview from "../components/EventPreview.jsx";
import './MyEventsPage.css';
import Pagination from "../components/Pagination.jsx";
import {FaMagnifyingGlass, FaUserGroup} from "react-icons/fa6";
import { FaHeart } from "react-icons/fa6";
import { FaCalendarDays } from "react-icons/fa6";
import ItineraStamp from "../components/ItineraStamp.jsx";

const MyEventsPage = () => {
    const { user, setUser } = useContext(AuthContext);
    const [activeTab, setActiveTab] = useState( "created");
    const [events, setEvents] = useState([]);

    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

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
                    return <LoginPage />
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

    if (user === null) return <LoginPage />;

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
                    <div>
                        <div className="my__events__page__header">
                            <div className="my__events__page__header__text">
                                <p>Мои события</p>
                                <p>Управляйте своими событиями</p>
                            </div>
                            <div className="my__events__page__header__info">
                                <div>
                                    <FaCalendarDays size={25} />
                                    <div>
                                        <p>0</p>
                                        <p>Всего событий</p>
                                    </div>
                                </div>

                                <div>
                                    <FaUserGroup size={25} />
                                    <div>
                                        <p>0</p>
                                        <p>Организованны мой</p>
                                    </div>
                                </div>
                            </div>

                            {/*Показывать только если есть события*/}
                            <div className="my__events__page__header__upcoming__event">
                                <div className="my__events__page__header__upcoming__event__left">
                                    <div className="my__events__page__header__upcoming__event__info__header">
                                        <p>Ваше следующее событие</p>
                                        <p>Название события</p>
                                    </div>

                                    <div className="my__events__page__header__upcoming__event__info">
                                        <p><FaCalendarDays size={15} /> дата</p>
                                        <p><FaUserGroup size={15} /> 0 участников</p>
                                    </div>
                                </div>

                                <div className="my__events__page__header__upcoming__event__right">
                                    <p>Осталось 3 дня</p>
                                    <Link className="upcoming__explore__link" to="/events/1">Подробнее</Link>
                                </div>
                            </div>
                        </div>

                        <div className="my__events__layout">
                            <div className="my__events__header">
                                <div>
                                    <div className="my__events__header__text">
                                        Мои события
                                    </div>

                                    <div className="my__events__header__tabs">

                                    </div>
                                </div>

                                <div className="my__events__header__searchbar">
                                    <FaMagnifyingGlass size={20} />
                                    <input type="text"/>
                                </div>
                            </div>

                            <div className="my__events__grid">

                            </div>
                        </div>
                    </div>


                </div>

                <div className="my__events__page__subinfo">
                    <div className="my__events__page__pending__invitations">
                        123
                    </div>

                    <div className="my__events__page__upcoming__this__week">

                    </div>
                </div>
            </div>

            {/*<div className="my__events__grid">*/}
            {/*    <div>*/}
            {/*        <div>*/}
            {/*            <Link to={"/events/invites"}>Приглащения</Link>*/}
            {/*            <br/>*/}
            {/*            <button*/}
            {/*                onClick={() => {*/}
            {/*                    setActiveTab("created")*/}
            {/*                    setPage(1)*/}
            {/*                }}*/}
            {/*                className={activeTab === "created" ? "active" : ""}*/}
            {/*            >Созданные мной</button>*/}
            {/*            <button*/}
            {/*                onClick={() => {*/}
            {/*                    setActiveTab("joined")*/}
            {/*                    setPage(1)*/}
            {/*                }}*/}
            {/*                className={activeTab === "joined" ? "active" : ""}*/}
            {/*            >Принимаю участие</button>*/}
            {/*        </div>*/}
            {/*        <div className="events__block">*/}
            {/*            {events.length > 0 ? (*/}
            {/*                <>*/}
            {/*                    {events.map(({id, name, capacity, joined, visibility, hostUsername}) => (*/}
            {/*                        <EventPreview*/}
            {/*                            key={id}*/}
            {/*                            id={id}*/}
            {/*                            name={name}*/}
            {/*                            capacity={capacity}*/}
            {/*                            joined={joined}*/}
            {/*                            visibility={visibility}*/}
            {/*                            hostUsername={hostUsername}*/}
            {/*                        />))}*/}

            {/*                    <Link to="/events/create">Создать новое Событие</Link>*/}
            {/*                </>*/}
            {/*            ) : (*/}
            {/*                <>*/}
            {/*                    <h1>Создайте свое первое событие прямо сейчас!</h1>*/}
            {/*                    <Link to="/events/create">Создать Событие</Link>*/}
            {/*                </>*/}
            {/*            )}*/}

            {/*            <Pagination*/}
            {/*                page={page}*/}
            {/*                totalPages={totalPages}*/}
            {/*                onPageChange={setPage}*/}
            {/*            />*/}
            {/*        </div>*/}
            {/*    </div>*/}
            {/*</div>*/}
        </div>
    );
}

export default MyEventsPage;