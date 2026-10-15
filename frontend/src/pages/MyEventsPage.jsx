import {Link} from "react-router-dom";
import Header from "../components/Header.jsx";
import LoginPage from "./LoginPage.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import EventPreview from "../components/EventPreview.jsx";
import './MyEventsPage.css';

const MyEventsPage = () => {
    const { user, setUser } = useContext(AuthContext);
    const [activeTab, setActiveTab] = useState( "created");
    const [events, setEvents] = useState([]);

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const url = activeTab === "created" ?
                    "/api/v1/events/my" :
                    "/api/v1/events/joined";

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

    if (user === null) return <LoginPage />;

    return (
        <div>
            <Header />

            <div className="my__events__grid">
                <div>
                    <div>
                        <button
                            onClick={() => {setActiveTab("created")}}
                        >Созданные мной</button>
                        <button
                            onClick={() => {setActiveTab("joined")}}
                        >Принимаю участие</button>
                    </div>
                    <div className="events__block">
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

                                <Link to="/events/create">Создать новое Событие</Link>
                            </>
                        ) : (
                            <>
                                <h1>Создайте свое первое событие прямо сейчас!</h1>
                                <Link to="/events/create">Создать Событие</Link>
                            </>
                        )}
                    </div>
                </div>

                <div>
                    <h1>Upcoming Events</h1>

                    <label htmlFor="">Поиск</label>
                    <input type="text"/>
                </div>
            </div>
        </div>
    );
}

export default MyEventsPage;