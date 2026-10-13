import {useParams} from "react-router-dom";
import Header from "../components/Header.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import LoginPage from "./LoginPage.jsx";
import RegisterPage from "./RegisterPage.jsx";

const ManageEventPage = () => {
    const { id } = useParams();
    const {user, setUser} = useContext(AuthContext);

    const [event, setEvent] = useState(null);

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

    if(user===null) return <LoginPage />
    if(event===null) return (
        <>
            <Header/>
            Ивента не существует!
        </>
    )

    return (
        <div>
            <Header />

            <h1>{event.name}</h1>
            <p>event id = {event.id}</p>
            <p>event capacity = {event.capacity}</p>
            <p>event visibility = {event.visibility}</p>
        </div>
    )
}

export default ManageEventPage;