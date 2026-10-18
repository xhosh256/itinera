import Header from "../components/Header.jsx";
import {useContext, useEffect, useState} from "react";
import EventPreview from "../components/EventPreview.jsx";
import AuthContext from "../context/AuthContext.jsx";
import {useSearchParams} from "react-router-dom";

const SearchEventsPage = () => {

    const [searchParams] = useSearchParams();
    const name = searchParams.get("name");

    const {setUser} = useContext(AuthContext);
    const [events, setEvents] = useState([]);

    useEffect(() => {
        const fetchEvents = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/events/search?name=${name}`, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (response.status === 401) {
                setUser(null)
                return
            }

            if(!response.ok) {
                throw Error(response.statusText);
            }

            const data = await response.json();
            setEvents(data.content);
        }

        fetchEvents();
    }, [name, setUser]);

    return (
        <div>
            <Header />

            <div>
                <h1>Результаты поиска «{name}»</h1>
                {events.map((event) => (
                    <EventPreview
                        key={event.id}
                        id={event.id}
                        name={event.name}
                        capacity={event.capacity}
                        joined={event.joined}
                        visibility={event.visibility}
                    />
                ))}
            </div>

        </div>
    )
}

export default SearchEventsPage;