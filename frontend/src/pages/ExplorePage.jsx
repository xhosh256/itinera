import Header from "../components/Header.jsx";
import {Link} from "react-router-dom";
import EventPreview from "../components/EventPreview.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import './ExplorePage.css';
import ItineraStamp from "../components/ItineraStamp.jsx";

const ExplorePage = () => {

    const [upcomingEvents, setUpcomingEvents] = useState([]);
    const {setUser} = useContext(AuthContext);

    useEffect(() => {
        const fetchUpcomingEvents = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/events/upcoming`, {
                credentials: "include",
                method: "GET",
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

            const events = await response.json();
            setUpcomingEvents(events.content);
        }

        fetchUpcomingEvents();
    }, [])

    return (
        <div className="explore__page">
            <Header />

            <div className="explore__page__layout">
                <div className="upcoming__events__layout">
                    <h1>Близжайшие события</h1>

                    <div className="upcoming__events__items">
                        {upcomingEvents.map(({id, name, capacity, joined, visibility}) => (
                            <EventPreview
                                key={id}
                                id={id}
                                name={name}
                                capacity={capacity}
                                joined={joined}
                                visibility={visibility}
                            />
                        ))}
                    </div>


                </div>

                <div className="description__layout">
                    <ItineraStamp>ITINERA</ItineraStamp>
                    <p className="p2">Твои планы начинаются здесь</p>
                    <p className="p3">Создай событие, пригласи друзей и организуй встречу в пару кликов</p>
                    <Link to="/events" className="create__event__link">+ Создать событие</Link>
                </div>

                <p className="copyright">© 2026 Itenera</p>
            </div>


        </div>
    )
}

export default ExplorePage