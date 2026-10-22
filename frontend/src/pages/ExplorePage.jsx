import Header from "../components/Header.jsx";
import {Link} from "react-router-dom";
import EventPreview from "../components/EventPreview.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";

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
            console.log("RENDER: ", events)
        }

        fetchUpcomingEvents();
    }, [])

    return (
        <>
            <Header />
            <div>
                <h1>Upcoming Events</h1>
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
            <div>
                <h1>Создайте свое первое событие прямо сейчас и приглашайте остальных посетить его!</h1>
                <Link to="/events">Создать Событие</Link>
            </div>
        </>
    )
}

export default ExplorePage