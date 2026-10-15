import {Link, useParams} from "react-router-dom";
import Header from "../components/Header.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import LoginPage from "./LoginPage.jsx";
import './ManageEventPage.css'
import EventComponent from "../components/EventComponent.jsx";
import Input from "../components/Input.jsx";

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
        <div>
            <Header />

            <div className="manage__event__page">
                <div className="manage__event__sidebar">
                    <h1>{event.name}</h1>
                    <p> {event.visibility === 'PUBLIC' ? 'Публичный' : 'Приватный'} ивент </p>
                    {event.participantIds.includes(user.id) ?
                        <p>Вы учавствуете</p> :
                        <button onClick={handleJoin} >Учавствовать</button>
                    }
                    <p>Список участников</p>
                    <div>
                        {event.participantIds.map((id) => (
                            <p key={id}>Пользователь {id}</p>
                        ))}
                    </div>

                    {}
                </div>

                <div className="manage__event__components">
                    {event.components.length !== 0 ? event.components.map(({id, startTime, endTime, description, address}) => (
                        <EventComponent
                            key={id}
                            startTime={startTime}
                            endTime={endTime}
                            description={description}
                            address={address}
                        />
                    )) : (<h1>У этого события нет компонент</h1>)}

                    {event.hostId === user.id ? (
                        <>
                            <h1>Создайте первый компонент события!</h1>
                            <Link to={`/events/${event.id}/components/create`}>Создать компонент</Link>
                        </>
                    ) : (<></>)}
                </div>


                <div className="manage__event__chat">
                    Here will be user chat ;)
                </div>
            </div>
        </div>
    )
}

export default ManageEventPage;